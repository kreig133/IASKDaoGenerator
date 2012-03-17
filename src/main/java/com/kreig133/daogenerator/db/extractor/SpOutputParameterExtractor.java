package com.kreig133.daogenerator.db.extractor;

import com.kreig133.daogenerator.db.JDBCConnector;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.sql.SqlQueryCreator;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author kreig133
 * @version 1.0
 */
public class SpOutputParameterExtractor extends OutputParameterExtractor{
    @Override
    protected ResultSet getResultSet( DaoMethod daoMethod ) throws SQLException {
        final String query = SqlQueryCreator.createQueries( daoMethod, true );

        assert query != null;

        final Connection connection = JDBCConnector.connectToDB();
        final CallableStatement callableStatement = connection.prepareCall( query );
        connection.createStatement().execute( "SET NOCOUNT ON;" );
        ResultSet resultSet = null;
        if( callableStatement.execute() ){
            resultSet = callableStatement.getResultSet();
        }
        connection.createStatement().execute( "SET NOCOUNT OFF;" );

        fillJdbcTypeForInputParameters( callableStatement.getParameterMetaData(), daoMethod );

        return resultSet;
    }
}
