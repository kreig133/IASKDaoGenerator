package com.kreig133.daogenerator.db;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.common.settings.EmptyOperationSettingsImpl;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.jaxb.InOutType;
import com.kreig133.daogenerator.jaxb.JavaType;
import com.kreig133.daogenerator.jaxb.ParameterType;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class StoreProcedureInfoExtractor {
    
    private static final String GET_INPUT_PARAMETRS_QUERY =
            "SELECT * FROM  INFORMATION_SCHEMA.PARAMETERS WHERE SPECIFIC_NAME = ? ORDER BY ORDINAL_POSITION";

    private static final String GET_SP_TEXT = "{CALL sp_helptext(?)}";

    public static final String PARAMETER_NAME_COLUMN = "PARAMETER_NAME";
    public static final String DATA_TYPE_COLUMN      = "DATA_TYPE";
    private static final String CHARACTER_MAXIMUM_LENGTH = "CHARACTER_MAXIMUM_LENGTH";
    private static final String PARAMETER_MODE = "PARAMETER_MODE";

    public static List<ParameterType> getInputParametrsForSP( String spName )  {
        final List<ParameterType> result = new ArrayList<ParameterType>();

        try {
            final Connection connection = JDBCConnector.connectToDB(
                    new EmptyOperationSettingsImpl() {
                        @Override
                        public Type getType() {
                            return Type.IASK;
                        }
                    }
            );
            final PreparedStatement preparedStatement = connection.prepareStatement( GET_INPUT_PARAMETRS_QUERY );

            preparedStatement.setString( 1, spName );

            final ResultSet resultSet = preparedStatement.executeQuery();

            while ( resultSet.next() ){
                result.add( extractDataFromResultSetRow( resultSet ) );
            }

        } catch ( SQLException e ) {
            throw new RuntimeException( "Не удалось получить параметры для хранимой процедуры", e );
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
            return new String( result.substring( 1 ) );
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

    public static String getSPText( String spName ){
        final Connection connection = JDBCConnector.connectToDB(
                new EmptyOperationSettingsImpl() {
                    @Override
                    public Type getType() {
                        return Type.IASK;
                    }
                }
        );

        String result = null;

        try {
            final CallableStatement callableStatement = connection.prepareCall( GET_SP_TEXT );

            callableStatement.setString( 1, spName );

            final ResultSet resultSet = callableStatement.executeQuery();

            StringBuilder builder = new StringBuilder();

            while ( resultSet.next() ) {
                builder.append( resultSet.getString( 1 ) );
            }
            result = builder.toString();
        } catch ( SQLException e ) {
            throw new RuntimeException( "Не удалось получить текст хранимки" );
        }

        return result;
    }
}
