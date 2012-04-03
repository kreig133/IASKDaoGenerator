package com.kreig133.daogenerator.db.extractors;

import com.google.common.base.Preconditions;
import com.kreig133.daogenerator.db.JDBCConnector;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.jaxb.ParametersType;
import com.kreig133.daogenerator.jaxb.SelectType;
import org.apache.commons.lang.StringUtils;
import org.intellij.lang.annotations.Language;

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
    protected String regex = "(?u)([\"\\[]?\\w+[\"\\]]?)?%s\\s*=\\s*(('(.+?)')|(\\w+))";
    @Language("RegExp")
    protected String columnName = "\\b([@#\\w&&[\\D]][\\w\\$@#]*)\\b";
    @Language("RegExp")
    protected String quotedColumnName = "\"(.+?)\"";
    @Language("RegExp")
    protected String columnNameInBrackets = "\\[(.+?)\\]";
    @Language( "RegExp" )
    protected String testValues = "(?i)(([-\\d\\.]+)|(null)|('.+?'))\\s*[,\\)]";
    @Language("RegExp")
    protected String insertRE =
            "(?isu)insert\\b\\s*\\binto\\b\\s*.+?\\s*(\\(\\s*(.+?)\\s*\\))?\\s*\\bvalues\\b\\s*\\((.+?\\))";

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
                .replaceAll( "``", "" );
    }

    protected String prepareAsInsertQueryIfNeed( String query ) {

        if( Extractor.determineQueryType( query ) != SelectType.INSERT ) return query;

        Matcher matcher = Pattern.compile( insertRE ).matcher( query );

        Preconditions.checkState( matcher.find(), "Ошибка в запросе и/или в генераторе!" );

        String columnNamesString = matcher.group( 2 );
        String[] testValues = getTestValues( matcher.group( 3 ) );
        if ( columnNamesString != null ) {
            String[] columnNames = columnNamesString.split( "\\s*,\\s*" );

            assert columnNames.length == testValues.length;

            List<String> columns = new ArrayList<String>();
            for ( int i = 0; i < columnNames.length; i++ ) {
                String input = columnNames[ i ];

                if ( input.matches( quotedColumnName ) || input.matches( columnNameInBrackets ) ) {
                    columnNames[ i ] = input.substring( 1, input.length() - 1 );
                    columns.add( columnNames[ i ] );
                }
                if ( input.matches( columnName ) ) {
                    columns.add( input );
                }
            }
            List<ParameterType> actualParamterList = getActualParameterList(
                    columns.iterator(),
                    getColumnsFromDbByTableName( getTableName( query ) )
            );

            assert actualParamterList.size() <= columnNames.length;

            for ( int i = 0; i < columnNames.length; i++ ) {
                String columnName = columnNames[ i ];
                ParameterType parameterByName =
                        ParametersType.getParameterByName( columnName, actualParamterList );
                if ( parameterByName != null ) {
                    testValues[ i ] = formatTestValue( testValues[ i ], parameterByName );
                }
            }
        } else {
            List<ParameterType> columnsFromDbByTableName = getColumnsFromDbByTableName( getTableName( query ) );

            assert testValues.length == columnsFromDbByTableName.size();

            for ( int i = 0; i < testValues.length; i++ ) {
                testValues[ i ] = formatTestValue( testValues[ i ], columnsFromDbByTableName.get( i ) );
            }
        }
        String join = StringUtils.join( testValues, ",\n\t" );

        return query.replaceAll( "(?isu)\\bvalues\\b\\s*\\((.+?)\\)", String.format( "values(\n\t%s\n)", join ) );
    }

    private String formatTestValue( String testValue, ParameterType parameterByName ) {
        return String.format( "\\${%s;%s;%s}",
                parameterByName.getName(),
                parameterByName.getSqlType(),
                testValue
        );
    }

    private String[] getTestValues( String string ) {
        List<String> testValueList = new ArrayList<String>();
        Matcher testValuesMatcher = Pattern.compile( this.testValues ).matcher( string );
        while ( testValuesMatcher.find() ) {
            testValueList.add( testValuesMatcher.group( 1 ) );
        }
        String[] testValues = new String[ testValueList.size() ];
        testValueList.toArray( testValues );

        return testValues;
    }

    private String replaceTestVaulesByDaoGeneratorFormatedInfoString( String query, List<ParameterType> result ) {
        for ( ParameterType parameterType : result ) {
            String pattern = String.format( "(?i)(%s.*?)=\\s*(('.+?')|\\w+)", parameterType.getName() );
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
                columnsWithValue.put( matcher.group( 2 ),
                        matcher.group( 5 ) == null ? matcher.group( 6 ) : matcher.group( 5 ) );
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