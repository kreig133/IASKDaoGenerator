package com.kreig133.daogenerator.db.extractors;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.kreig133.daogenerator.db.JDBCConnector;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.jaxb.ParametersType;
import com.kreig133.daogenerator.jaxb.SelectType;
import org.apache.commons.lang.StringUtils;
import org.intellij.lang.annotations.Language;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.kreig133.daogenerator.db.extractors.TableNameHelper.getTableName;

/**
 * @author eshangareev
 * @version 1.0
 */
public class QueryPreparator {

    @Language("RegExp")
    protected String regex = "(?u)%s\\s*=\\s*(('(.+?)')|(\\w+))";
    @Language("RegExp")
    protected String columnName = "\\b([@#\\w&&[\\D]][\\w\\$@#]*)\\b";
    @Language("RegExp")
    protected String quotedColumnName = "\"(.+?)\"";
    @Language("RegExp")
    protected String columnNameInBrackets = "\\[(.+?)\\]";
    @Language( "RegExp" )
    protected String value = "(?i)(([-\\d\\.]+)|(null)|('.+?'))\\s*[,\\)]";

    protected String[] subPatterns = { columnName, quotedColumnName, columnNameInBrackets };

    public String prepareQuery( String query ) {
        List<ParameterType> result = getActualParameterList(
                getColumnsFromQuery( query ).keySet().iterator(),
                getColumnsFromDbByTableName( getTableName( query ) )
        );

        query = replaceTestVaulesByDaoGeneratorFormatedInfoString( query, result );

        return prepareAsInsertQueryIfNeed( query )
                .replaceAll( "(\\$\\{.+?;)'(.+?\\})", "$1$2" )
                .replaceAll( "(\\$\\{.+?)'\\}", "$1}" )
                .replaceAll( "#@!", "" );
    }

    protected String prepareAsInsertQueryIfNeed( String query ) {
        if( Extractor.determineQueryType( query ) != SelectType.INSERT ) return query;
        @Language("RegExp")
        String regExp = "(?isu)insert\\b\\s*\\binto\\b\\s*.+?\\s*\\((.+?)\\)?\\s*\\bvalues\\b\\s*\\((.+?\\))";
        Matcher matcher = Pattern.compile( regExp ).matcher( query );
        if( ! matcher.find() ) {
            throw new RuntimeException( "Ошибка в запросе и/или в генераторе!" );
        }
        if ( matcher.group( 1 ) != null ) {
            String[] columnValues = matcher.group( 1 ).split( "," );

            String[] testValues = new String[ columnValues.length ];
            Matcher matcher1 = Pattern.compile( value ).matcher( matcher.group( 2 ) );
            int j = 0;
            for (; matcher1.find();j++ ) {
                testValues[ j ] = matcher1.group( 1 );

            }

            assert columnValues.length == j;

            List<String> columns = new ArrayList<String>();
            for ( int i = 0; i < columnValues.length; i++ ) {
                testValues[ i ] = testValues[ i ].trim();
                columnValues[ i ] = columnValues[ i ].trim();
                String input = columnValues[ i ];

                if ( input.matches( quotedColumnName ) || input.matches( columnNameInBrackets ) ) {
                    columnValues[ i ] = input.substring( 1, input.length() - 1 );
                    columns.add( columnValues[ i ] );
                }
                if ( input.matches( columnName ) ) {
                    columns.add( input );
                }
            }

            List<ParameterType> actualParamterList = getActualParameterList(
                    columns.iterator(),
                    getColumnsFromDbByTableName( getTableName( query ) )
            );

            assert actualParamterList.size() <= columnValues.length;

            for ( int i = 0; i < columnValues.length; i++ ) {
                String columnValue = columnValues[ i ];
                ParameterType parameterByName =
                        ParametersType.getParameterByName( columnValue.trim(), actualParamterList );
                if ( parameterByName != null ) {
                    testValues[ i ] = String.format( "\\${%s;%s;%s}",
                            parameterByName.getName(),
                            parameterByName.getSqlType(),
                            testValues[ i ].trim()
                    );
                }
            }

            String join = StringUtils.join( testValues, ",\n\t" );

            return query.replaceAll( "(?isu)\\bvalues\\b\\s*\\((.+?)\\)", String.format( "values(\n\t%s\n)", join ) );
        }
        throw new RuntimeException( "ДОДЕЛАТЬ БЫ НАДО БЫ!!" ); //TODO
    }

    private String replaceTestVaulesByDaoGeneratorFormatedInfoString( String query, List<ParameterType> result ) {
        for ( ParameterType parameterType : result ) {
            String pattern = String.format( "(%s.*?)=\\s*(('.+?')|\\w+)", parameterType.getName() );
            String replacement = String.format(
                    "$1= \\${%s;%s;$2}",
                    parameterType.getName(),
                    parameterType.getSqlType()
            );
            query = query.replaceAll( pattern, replacement );
        }
        return query;
    }

    private List<ParameterType> getActualParameterList(
            Iterator<String> columnsFromQuery,
            List<ParameterType> columnsFromDbByTableName
    ) {
        List<ParameterType> result = new ArrayList<ParameterType>();
        while ( columnsFromQuery.hasNext() ) {
            String column = columnsFromQuery.next();
            ParameterType parameterByName = ParametersType.getParameterByName( column.trim(), columnsFromDbByTableName );

            if( parameterByName == null ) {
                parameterByName = new ParameterType();
                parameterByName.setName( column );
                parameterByName.setSqlType( "!!ERROR!!" );
            }

            result.add( parameterByName );
        }
        return result;
    }

    protected Map<String, String> getColumnsFromQuery( DaoMethod daoMethod ) {
        return getColumnsFromQuery( daoMethod.getCommon().getQuery() );
    }

    protected Map<String, String> getColumnsFromQuery( String query ) {
        Map<String, String> columnsWithValue = new HashMap<String, String>();
        for ( String subPattern : subPatterns ) {
            Matcher matcher = Pattern.compile( String.format( regex, subPattern ) ).matcher( query );
            while ( matcher.find() ) {
                columnsWithValue.put( matcher.group( 1 ),
                        matcher.group( 4 ) == null ? matcher.group( 5 ) : matcher.group( 4 ) );
            }
        }
        return columnsWithValue;
    }

    protected List<ParameterType> getColumnsFromDbByTableName( String tableName ) {
        try {
            Connection connection = JDBCConnector.instance().connectToDB();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ? ORDER BY ORDINAL_POSITION" );
            statement.setString( 1, tableName );

            ResultSet resultSet = statement.executeQuery();
            List<ParameterType> parameterTypeList = new ArrayList<ParameterType>();
            while ( resultSet.next() ) {
                ParameterType type = new ParameterType();
                type.setSqlType( SqlTypeHelper.getSqlTypeFromResultSet( resultSet ) );
                type.setName( resultSet.getString( "COLUMN_NAME" ) );
                parameterTypeList.add( type );
            }
            return parameterTypeList;
        } catch ( Exception e ) {
            e.printStackTrace();
            return new ArrayList<ParameterType>();
        }
    }

    private static QueryPreparator INSTANCE;

    public static QueryPreparator instance(){
        if ( INSTANCE == null ) {
            INSTANCE = new QueryPreparator();
        }
        return INSTANCE;
    }
}