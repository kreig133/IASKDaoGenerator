package com.kreig133.daogenerator.db.extractors.out;

import com.kreig133.daogenerator.db.JDBCConnector;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.sql.creators.QueryCreatorFabric;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.kreig133.daogenerator.db.JBDCTypeIdConverter.fillJdbcTypeForInputParameters;

/**
 * @author kreig133
 * @version 1.0
 */
public class ResultSetGetterForSp implements ResultSetGetter{

    @Nullable
    @Override
    public ResultSet getResultSet( @NotNull DaoMethod daoMethod ) throws SQLException {
        final String query = QueryCreatorFabric.newInstance( daoMethod ).generateExecuteQuery( daoMethod, true );

        assert query != null;

        final Connection connection = JDBCConnector.instance().connectToDB();

        assert connection != null;
        final CallableStatement callableStatement = connection.prepareCall( query );

        connection.createStatement().execute( "SET NOCOUNT ON;" );

        ResultSet resultSet = callableStatement.execute() ? callableStatement.getResultSet() : null;

        connection.createStatement().execute( "SET NOCOUNT OFF;" );

        fillJdbcTypeForInputParameters( callableStatement.getParameterMetaData(), daoMethod );

        return resultSet;
    }
}
