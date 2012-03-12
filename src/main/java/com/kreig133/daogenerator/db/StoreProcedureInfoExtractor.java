package com.kreig133.daogenerator.db;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.jaxb.InOutType;
import com.kreig133.daogenerator.jaxb.JavaType;
import com.kreig133.daogenerator.jaxb.ParameterType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author eshangareev
 * @version 1.0
 */
public class StoreProcedureInfoExtractor {
    
    private static final String GET_INPUT_PARAMETRS_QUERY =
            "SELECT * FROM  INFORMATION_SCHEMA.PARAMETERS WHERE SPECIFIC_NAME = ? ORDER BY ORDINAL_POSITION";

    private static final String GET_SP_TEXT = "{CALL sp_helptext(?)}";

    public static final String PARAMETER_NAME_COLUMN    = "PARAMETER_NAME";
    public static final String DATA_TYPE_COLUMN         = "DATA_TYPE";
    public static final String CHARACTER_MAXIMUM_LENGTH = "CHARACTER_MAXIMUM_LENGTH";
    public static final String PARAMETER_MODE           = "PARAMETER_MODE";

    private static String spText = null;

    public static List<ParameterType> getInputParametrsForSP( String spName )  {
        final List<ParameterType> result = new ArrayList<ParameterType>();

        try {
            final Connection connection = JDBCConnector.connectToDB();
            final PreparedStatement preparedStatement = connection.prepareStatement( GET_INPUT_PARAMETRS_QUERY );

            preparedStatement.setString( 1, spName );

            final ResultSet resultSet = preparedStatement.executeQuery();

            while ( resultSet.next() ){
                result.add( extractDataFromResultSetRow( resultSet ) );
            }

        } catch ( SQLException e ) {
            throw new RuntimeException( "Не удалось получить параметры для хранимой процедуры " + spName, e );
        }

        getSPText( spName );

        for ( ParameterType parameterType : result ) {
            fillDefaultValues( parameterType, getSPText() );
            parameterType.setTestValue( parameterType.getDefaultValue() );
        }


        return result;
    }

    public static ParameterType extractDataFromResultSetRow( ResultSet resultSet ) throws SQLException {

        final ParameterType parameterType = new ParameterType();

        parameterType.setName   ( getParameterNameFromResultSet( resultSet ) );
        parameterType.setSqlType( getSqlTypeFromResultSet( resultSet ) );
        parameterType.setType   ( JavaType.getBySqlType( resultSet.getString( DATA_TYPE_COLUMN ) ) );
        parameterType.setInOut  ( InOutType.getByName( resultSet.getString( PARAMETER_MODE ) ) );
        parameterType.setRenameTo( Utils. convertPBNameToName( parameterType.getName() ) );
        return parameterType;
    }

    private static String getParameterNameFromResultSet( ResultSet resultSet ) throws SQLException {
        String result = resultSet.getString( PARAMETER_NAME_COLUMN );

        if( result.startsWith( "@" )){
            return result.substring( 1 );
        }
        return result;
    }

    private static String getSqlTypeFromResultSet( ResultSet resultSet ) throws SQLException {
        String sqlType = resultSet.getString( DATA_TYPE_COLUMN );

        if( JavaType.getBySqlType( sqlType ) == JavaType.STRING ){
            return sqlType + "(" + resultSet.getString( CHARACTER_MAXIMUM_LENGTH ) + ")";
        }

        return sqlType;
        //TODO добавить для Double
//        if( JavaType.getBySqlType( sqlType ))

    }

    public static String getSPText(){
        return spText;
    }


    protected static void getSPText( String spName ){
        final Connection connection = JDBCConnector.connectToDB();

        try {
            final CallableStatement callableStatement = connection.prepareCall( GET_SP_TEXT );

            callableStatement.setString( 1, spName );

            final ResultSet resultSet = callableStatement.executeQuery();

            StringBuilder builder = new StringBuilder();

            while ( resultSet.next() ) {
                builder.append( resultSet.getString( 1 ) );
            }
            spText = builder.toString();
        } catch ( SQLException e ) {
            throw new RuntimeException( "Не удалось получить текст хранимки" );
        }
    }

    public static void fillDefaultValues( ParameterType type, String spText ) {
        String storeProcedureDefinition = getDefinitionFormSpText( spText );

        switch ( type.getType() ){
            case LONG:
            case BYTE:
            case DOUBLE:
                type.setDefaultValue( getDefaultValueForNumberFromSpDefinition( type, storeProcedureDefinition ) );
                break;
            case STRING:
                type.setDefaultValue( getDefaultValueForStringFromSpDefinition( type, storeProcedureDefinition ) );
                break;
            default:
                System.out.println( type.getName() + "type is "+ type.getType().value() );
        }
    }

    private static String getDefaultValueForStringFromSpDefinition( ParameterType type, String storeProcedureDefinition ) {
        if ( getNullPatternForParameter( type.getName() ).matcher( storeProcedureDefinition ).find() ){
            return NULL;
        }
        final Matcher matcher = getStringPatternForParameter( type.getName() ).matcher( storeProcedureDefinition );
        if( matcher.find() ){
            return matcher.group( 1 );
        } else {
            return "";
        }
    }

    private static String getDefaultValueForNumberFromSpDefinition( ParameterType type, String storeProcedureDefinition ) {
        if ( getNullPatternForParameter( type.getName() ).matcher( storeProcedureDefinition ).find() ){
            return NULL;
        }
        final Matcher matcher = getNumberPatternForParameter( type.getName() ).matcher( storeProcedureDefinition );
        if( matcher.find() ){
            return matcher.group( 1 );
        } else {
            return "";
        }
    }

    private static Pattern getStringPatternForParameter( String name ) {
        return Pattern.compile( String.format( "(?isu)@%s\\s+[^@]+?=\\s*['\"](.*?)['\"]", name ) );
    }

    private static Pattern getNullPatternForParameter( String parameterName ) {
        return Pattern.compile( String.format( "(?isu)@%s\\s+[^@]+?=\\s*null\\b", parameterName) );
    }
    private static Pattern getNumberPatternForParameter( String parameterName ) {
        return Pattern.compile( String.format( "(?isu)@%s\\s+[^@]+?=\\s*([\\d\\.]+)\\b", parameterName ) );
    }

    private static String getDefinitionFormSpText( String spText ) {
        //TODO есть косяк ( если в комментариях встретися \bAS\b, то сработает неправильно
        //TODO Есть вариант выпиливать предварительно комментарии

        final Matcher matcher = Pattern.compile( "(?isu)create\\s+procedure(.*?)\\bas\\b" ).matcher( spText );
        if( matcher.find() ){
            return matcher.group( 1 );
        } else {
            return null;
        }
    }

    public static final String NULL = "null";
}
