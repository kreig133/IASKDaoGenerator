package com.kreig133.daogenerator.db.extractors.in;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.db.JDBCConnector;
import com.kreig133.daogenerator.db.extractors.Extractor;
import com.kreig133.daogenerator.jaxb.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.*;
import static com.kreig133.daogenerator.db.extractors.SqlTypeHelper.getSqlTypeFromResultSet;

/**
 * @author eshangareev
 * @version 1.0
 */
public class SpInputParameterExtractor extends InputParameterExtractor {

    private static SpInputParameterExtractor INSTANCE;

    static InputParameterExtractor instance() {
        if ( INSTANCE == null ) {
            INSTANCE = new SpInputParameterExtractor();
        }
        return INSTANCE;
    }

    private static final String GET_INPUT_PARAMETRS_QUERY =
            "SELECT * FROM  INFORMATION_SCHEMA.PARAMETERS WHERE SPECIFIC_NAME = ? ORDER BY ORDINAL_POSITION";

    private static final String GET_SP_TEXT = "{CALL sp_helptext(?)}";

    public static final String PARAMETER_NAME_COLUMN    = "PARAMETER_NAME";

    public static final String PARAMETER_MODE           = "PARAMETER_MODE";

    private static String spText = null;

    public ParameterType extractDataFromResultSetRow( ResultSet resultSet ) throws SQLException {
        final ParameterType parameterType = new ParameterType();

        parameterType.setName   ( getParameterNameFromResultSet( resultSet ) );
        parameterType.setSqlType( getSqlTypeFromResultSet( resultSet ) );
        parameterType.setType   ( JavaType.getBySqlType( parameterType.getSqlType() ) );
        parameterType.setInOut  ( InOutType.getByName( resultSet.getString( PARAMETER_MODE ) ) );
        parameterType.setRenameTo( Utils. convertPBNameToName( parameterType.getName() ) );
        return parameterType;
    }

    private String getParameterNameFromResultSet( ResultSet resultSet ) throws SQLException {
        String result = resultSet.getString( PARAMETER_NAME_COLUMN );

        return result.startsWith( "@" ) ? result.substring( 1 ) : result;
    }

    public static String getSPText(){
        return spText;
    }

    protected void getSPText( String spName ){
        final Connection connection = JDBCConnector.instance().connectToDB();

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

    private void fillComments( ParameterType type, String storeProcedureDefinition ) {
        final Matcher matcher = getCommentPattern( type.getName() ).matcher( storeProcedureDefinition );
        if ( matcher.find() ) {
            type.setComment( matcher.group( 1 ).replaceAll( "[\n\t\r]", " " ).replaceAll( "\\s+", " " ).trim() );
        }
    }

    private Pattern getCommentPattern( String parameterName ) {
        return Pattern.compile( String.format( "(?isu)@%s\\s+[^@]+?--([^@]+)", parameterName ) );
    }

    private String getDefinitionFormSpText( String spText ) {
        //TODO есть косяк ( если в комментариях встретися \bAS\b, то сработает неправильно
        //TODO Есть вариант выпиливать предварительно комментарии
        final Matcher matcher = Pattern.compile( "(?isu)create\\s+procedure(.*?)\\bas\\b" ).matcher( spText );
        return matcher.find() ? matcher.group( 1 ) : "";
    }

    @Override
    public DaoMethod extractInputParams( DaoMethod daoMethod ) {
        final List<ParameterType> result = new ArrayList<ParameterType>();
        String spName = daoMethod.getCommon().getSpName();

        try {
            final Connection connection = JDBCConnector.instance().connectToDB();
            final PreparedStatement preparedStatement = connection.prepareStatement( GET_INPUT_PARAMETRS_QUERY );

            assert spName != null;

            preparedStatement.setString( 1, spName );

            final ResultSet resultSet = preparedStatement.executeQuery();

            while ( resultSet.next() ){
                result.add( extractDataFromResultSetRow( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new RuntimeException( "Не удалось получить параметры для хранимой процедуры " + spName, e );
        }

        getSPText( spName );

        String storeProcedureDefinition = getDefinitionFormSpText( spText );
        for ( ParameterType parameterType : result ) {
            String parameterValueFromQuery =
                    SelectInputParameterExtractor.instance().
                    getParameterValueFromQuery( parameterType, storeProcedureDefinition );
            if ( ! NOT_FOUNDED.equals( parameterValueFromQuery ) ) {
                parameterType.setDefaultValue( parameterValueFromQuery );
            }
            fillComments( parameterType, storeProcedureDefinition );
            parameterType.setTestValue(
                    parameterType.getDefaultValue() == null || "".equals( parameterType.getDefaultValue() ) ?
                            ParameterType.getDefaultTestValue( parameterType ) :
                            parameterType.getDefaultValue()
            );
        }
        daoMethod.getInputParametrs().getParameter().clear();
        daoMethod.getInputParametrs().getParameter().addAll( result );
        return daoMethod;
    }

    @Override
    public DaoMethod fillTestValuesByInsertedQuery( DaoMethod daoMethod ) {
        List<ParameterType> inputParametrs = daoMethod.getInputParametrs().getParameter();
        String query = daoMethod.getCommon().getQuery();

        if( ! Utils.stringContainsMoreThanOneWord( query ) ) return daoMethod;

        for ( ParameterType inputParametr : inputParametrs ) {
            String parameterValueFromQuery = getParameterValueFromQuery( inputParametr, query );
            inputParametr.setTestValue(
                    NOT_FOUNDED.equals( parameterValueFromQuery ) ?
                            inputParametr.getDefaultValue() :
                            parameterValueFromQuery
            );
        }
        return daoMethod;
    }

    @Override
    public DaoMethod fillMethodName( DaoMethod daoMethod ) {
        daoMethod.getCommon().setMethodName(
                Utils.convertPBNameToName(
                        Extractor.getStoreProcedureName( daoMethod.getCommon().getQuery() )
                )
        );
        return daoMethod;
    }

    public boolean checkSPName( String query ) {
        checkArgument( determineQueryType( query ) != SelectType.CALL,
                "Нельзя проверить название хранимой процедуры для SQL-запроса" );
        return ! ( getStoreProcedureName( query ) == null || "".equals( getStoreProcedureName( query ) ) );
    }
}