package com.kreig133.daogenerator.db.extractors.out;

import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.sql.creators.QueryCreatorFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author kreig133
 * @version 1.0
 */
public class ResultSetGetterForSp implements ResultSetGetter{

    @Nullable
    @Override
    public ResultSet getResultSetAndFillJdbcTypeIfNeed(
            @NotNull DaoMethod daoMethod, @NotNull Connection connection
    ) throws SQLException {
        final String query = QueryCreatorFactory.newInstance( daoMethod ).generateExecuteQuery( daoMethod, true );

        assert query != null;

        final CallableStatement callableStatement = connection.prepareCall(
                query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY
        );

        return callableStatement.execute() ? callableStatement.getResultSet() : null;
    }
}
