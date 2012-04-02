package com.kreig133.daogenerator.db.extractors;

import com.kreig133.daogenerator.db.JDBCConnector;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.jaxb.ParametersType;
import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    protected String[] subPatterns = { columnName, quotedColumnName, columnNameInBrackets };

    public String prepareQuery( String query ) {
        Map<String, String> columnsFromQuery = getColumnsFromQuery( query );
        List<ParameterType> columnsFromDbByTableName = getColumnsFromDbByTableName( getTableName( query ) );

        List<ParameterType> result = new ArrayList<ParameterType>();
        for ( String column : columnsFromQuery.keySet() ) {
            ParameterType parameterByName = ParametersType.getParameterByName( column, columnsFromDbByTableName );

            if( parameterByName == null ) {
                parameterByName = new ParameterType();
                parameterByName.setName( column );
            }

            parameterByName.setTestValue( columnsFromQuery.get( column ) );
            result.add( parameterByName );
        }

        for ( ParameterType parameterType : result ) {
            String format = String.format( "(%s.*?)=\\s*(('.+?')|\\w+)", parameterType.getName() );
            String format1 = String.format(
                    "$1= \\${%s;%s;$2}",
                    parameterType.getName(),
                    parameterType.getSqlType() == null ? "!!ERROR!!" : parameterType.getSqlType()
            );
            query = query.replaceAll( format, format1 ).replaceAll( ";'", ";" ).replaceAll( "'\\}", "}" );
        }
        return query;
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
