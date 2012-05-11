package com.kreig133.daogenerator.db.extractors.in;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.db.JDBCConnector;
import com.kreig133.daogenerator.db.extractors.Extractor;
import com.kreig133.daogenerator.jaxb.*;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static com.kreig133.daogenerator.db.extractors.SqlTypeHelper.getSqlTypeFromResultSet;

/**
 * @author eshangareev
 * @version 1.0
 */
public class SpInputParameterExtractor extends InputParameterExtractor {

    //<editor-fold desc="instance">
    private final static SpInputParameterExtractor INSTANCE = new SpInputParameterExtractor();

    private SpInputParameterExtractor() {
    }

    static InputParameterExtractor instance() {
        return INSTANCE;
    }
    //</editor-fold>

    //<editor-fold desc="SQL-Query">
    private static final String GET_INPUT_PARAMETRS_QUERY =
            "SELECT * FROM  INFORMATION_SCHEMA.PARAMETERS WHERE SPECIFIC_NAME = ? ORDER BY ORDINAL_POSITION";

    private static final String GET_SP_TEXT = "{CALL sp_helptext(?)}";
    //</editor-fold>

    //<editor-fold desc="SQL-Column">
    protected static final String S_EXECUTE = "sExecute";

    public static final String PARAMETER_NAME_COLUMN    = "PARAMETER_NAME";

    public static final String PARAMETER_MODE           = "PARAMETER_MODE";
    //</editor-fold>

    @Nullable
    private static String spText = null;

    @NotNull
    DaoMethod fillTestValuesByInsertedQuery( @NotNull DaoMethod daoMethod ) {
        List<ParameterType> inputParametrs = daoMethod.getInputParametrs().getParameter();
        String query = daoMethod.getCommon().getQuery();

        if( ! Utils.stringContainsMoreThanOneWord( query ) ) return daoMethod;

        for ( ParameterType inputParametr : inputParametrs ) {
            String parameterValueFromQuery = getParameterValueFromQuery( inputParametr, query );
            inputParametr.setTestValue(
                    NOT_FOUNDED.equals( parameterValueFromQuery ) ?
                            getTestValueFromDefaultValue( inputParametr ) :
                            parameterValueFromQuery
            );
        }
        return daoMethod;
    }

    @NotNull
    @Override
    public DaoMethod extractInputParams( @NotNull DaoMethod daoMethod ) {
        final List<ParameterType> result = new ArrayList<ParameterType>();

        String spName = daoMethod.getCommon().getSpName();

        getInputValuesFromInformationSchema( result, spName );

        fillParameterTypesByInfoFromSpText( result, spName );

        daoMethod.getInputParametrs().getParameter().clear();
        daoMethod.getInputParametrs().getParameter().addAll( result );

        return fillTestValuesByInsertedQuery( daoMethod );
    }

    //<editor-fold desc="Получение данных из INFORMATION_SCHEMA">
    protected void getInputValuesFromInformationSchema( @NotNull List<ParameterType> result, @NotNull String spName ) {
        try {
            final PreparedStatement preparedStatement =
                    JDBCConnector.instance().connectToDB().prepareStatement( GET_INPUT_PARAMETRS_QUERY );

            preparedStatement.setString( 1, spName );

            final ResultSet resultSet = preparedStatement.executeQuery();

            while ( resultSet.next() ){
                result.add( extractDataFromResultSetRow( resultSet ) );
            }
        } catch ( SQLException e ) {
            throw new RuntimeException( "Не удалось получить параметры для хранимой процедуры " + spName, e );
        }
    }

    @NotNull
    protected ParameterType extractDataFromResultSetRow( @NotNull ResultSet resultSet ) throws SQLException {
        final ParameterType parameterType = new ParameterType();

        parameterType.setName   ( getParameterNameFromResultSet( resultSet ) );
        parameterType.setSqlType( getSqlTypeFromResultSet( resultSet ) );
        parameterType.setType   ( JavaType.getBySqlType( parameterType.getSqlType() ) );
        parameterType.setInOut  ( InOutType.getByName( resultSet.getString( PARAMETER_MODE ) ) );
        parameterType.setRenameTo( Utils. convertPBNameToName( parameterType.getName() ) );
        return parameterType;
    }

    private String getParameterNameFromResultSet( @NotNull ResultSet resultSet ) throws SQLException {
        String result = resultSet.getString( PARAMETER_NAME_COLUMN );

        return result.startsWith( "@" ) ? result.substring( 1 ) : result;
    }
    //</editor-fold>

    //<editor-fold desc="Заполнение данными из текста хранимки">
    private void fillParameterTypesByInfoFromSpText( @NotNull List<ParameterType> result, String spName ) {
        spText = getSPText( spName );

        fillParameterByInfoFromSpText( result, getDefinitionFromSpText( spText ) );
    }

    private void fillParameterByInfoFromSpText( @NotNull List<ParameterType> result, String storeProcedureDefinition ) {
        for ( ParameterType parameterType : result ) {
            fillDefaultValue( parameterType, storeProcedureDefinition );
            fillComments( parameterType, storeProcedureDefinition );
            fillTestValue( parameterType );
        }
    }

    private void fillDefaultValue( @NotNull ParameterType parameterType, String storeProcedureDefinition ) {
        String parameterValueFromQuery =
                SelectInputParameterExtractor.instance().
                        getParameterValueFromQuery( parameterType, storeProcedureDefinition );
        if ( ! NOT_FOUNDED.equals( parameterValueFromQuery ) ) {
            String defaultValue = parameterType.getDefaultValue();
            if( StringUtils.isBlank( defaultValue ) ) {
                parameterType.setDefaultValue( parameterValueFromQuery );
            }
        }
    }

    private void fillComments( @NotNull ParameterType type, String storeProcedureDefinition ) {
        if ( type.getComment() != null && ! type.getComment().trim().equals( "" ) ) {
            return;
        }
        final Matcher matcher = getCommentPattern( type.getName() ).matcher( storeProcedureDefinition );
        if ( matcher.find() ) {
            type.setComment( matcher.group( 1 ).replaceAll( "[\n\t\r]", " " ).replaceAll( "\\s+", " " ).trim() );
        }
    }


    private void fillTestValue( @NotNull ParameterType parameterType ) {
        parameterType.setTestValue(
                getTestValueFromDefaultValue( parameterType )
        );
    }
    //</editor-fold>

    @Nullable
    public static String getSPText(){
        return spText;
    }

    protected String getSPText( String spName ){
        final Connection connection = JDBCConnector.instance().connectToDB();

        try {
            final CallableStatement callableStatement = connection.prepareCall( GET_SP_TEXT );

            callableStatement.setString( 1, spName );

            final ResultSet resultSet = callableStatement.executeQuery();

            StringBuilder builder = new StringBuilder();

            while ( resultSet.next() ) {
                builder.append( resultSet.getString( 1 ) );
            }
            return builder.toString();
        } catch ( SQLException e ) {
            throw new RuntimeException( "Не удалось получить текст хранимки" );
        }
    }

    private Pattern getCommentPattern( String parameterName ) {
        return Pattern.compile( String.format( "(?isu)@%s\\s+[^@]+?--([^@]+)", parameterName ) );
    }

    @NotNull
    protected String getDefinitionFromSpText( String spText ) {
        final Matcher matcher = Pattern.compile(
                "(?isu)create\\s+procedure(.*?(--[^\\n]*\\bas\\b.*?)*)\\bas\\b"
        ).matcher( spText );
        return matcher.find() ? matcher.group( 1 ) : "";
    }

    @NotNull
    protected String getParentSpName( @NotNull ResultSet resultSet ) throws SQLException {
        String sExecute = resultSet.getString( S_EXECUTE );
        Matcher matcher = Pattern.compile( "(?isu)insert into.+?exec(ute)?\\s*(dbo.)?(\\w+)\\b" ).matcher( sExecute );

        return matcher.find() ? matcher.group( 3 ) : NOT_FOUNDED;
    }

    private String getTestValueFromDefaultValue( @NotNull ParameterType parameterType ) {
        if( parameterType.getDefaultValue() == null || "null".equalsIgnoreCase( parameterType.getDefaultValue() )){
            return "null";
        }
        if ( parameterType.getDefaultValue().trim().equals( "" ) && parameterType.getType() != JavaType.STRING ) {
            return "null";
        }
        return parameterType.getDefaultValue();
    }

    public boolean checkSPName( @NotNull String query ) {
        checkArgument( determineQueryType( query ) != SelectType.CALL,
                "Нельзя проверить название хранимой процедуры для SQL-запроса" );
        return ! ( getStoreProcedureName( query ) == null || "".equals( getStoreProcedureName( query ) ) );
    }
}