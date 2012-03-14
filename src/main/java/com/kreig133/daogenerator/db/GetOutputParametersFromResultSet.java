package com.kreig133.daogenerator.db;

import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.JavaType;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.jaxb.ParametersType;
import com.kreig133.daogenerator.sql.SqlQueryCreator;
import com.kreig133.daogenerator.sql.SqlQueryParser;
import com.kreig133.daogenerator.sql.SqlUtils;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class GetOutputParametersFromResultSet {

    public static DaoMethod getOutputParameters( final DaoMethod daoMethod ){
        if ( daoMethod.getCommon().getQuery() == null || "".equals( daoMethod.getCommon().getQuery() ) ){
            return getOutputParameters( daoMethod, new Action() {
                @Override
                public ResultSet getResultSet() throws SQLException {
                    final String query = SqlQueryCreator.createQueries( daoMethod, true );
                    assert query != null;

                    final Connection connection = JDBCConnector.connectToDB();
                    final CallableStatement callableStatement = connection.prepareCall( query );
                    connection.createStatement().execute( "SET NOCOUNT ON;" );
                    final ResultSet resultSet = callableStatement.executeQuery();
                    connection.createStatement().execute( "SET NOCOUNT OFF;" );
                    return resultSet;
                }
            } );
        } else {
            return getOutputParameters( daoMethod, new Action() {
                @Override
                public ResultSet getResultSet() throws SQLException {
                    final String query =
                            SqlQueryParser.getQueryStringWithoutMetaData( daoMethod.getCommon().getQuery() );
                    assert query != null;

                    final Connection connection = JDBCConnector.connectToDB();
                    final PreparedStatement statement = connection.prepareStatement( query );
                    final List<ParameterType> parameterTypes = daoMethod.getInputParametrs().getParameter();

                    for ( int i = 0; i < parameterTypes.size(); i++ ) {
                        statement.setString( i + 1, SqlUtils.getTestValue( parameterTypes.get( i ) ) );
                    }

                    return statement.executeQuery();
                }
            } );
        }
    }
    protected static DaoMethod getOutputParameters( final DaoMethod daoMethod, Action action ){

        try {
            final ResultSet resultSet = action.getResultSet();

            final ResultSetMetaData metaData = resultSet.getMetaData();
            final List<ParameterType> parameterTypes = new LinkedList<ParameterType>();
            for ( int i = 1; i <= metaData.getColumnCount(); i++ ) {
                final ParameterType parameterType = new ParameterType();
                parameterType.setName( metaData.getColumnName( i ) );
                parameterType.setRenameTo( parameterType.getName() );
                parameterType.setSqlType( metaData.getColumnTypeName( i ) );
                parameterType.setType( JavaType.getBySqlType( metaData.getColumnTypeName( i ) ) );

                parameterTypes.add( parameterType );
            }

            daoMethod.setOutputParametrs( new ParametersType() );
            daoMethod.getOutputParametrs().getParameter().addAll( parameterTypes );

        } catch ( SQLException e ) {
            e.printStackTrace(); // TODO
        }

        return daoMethod;
    }

    interface Action{
        ResultSet getResultSet() throws SQLException;
    }
}

