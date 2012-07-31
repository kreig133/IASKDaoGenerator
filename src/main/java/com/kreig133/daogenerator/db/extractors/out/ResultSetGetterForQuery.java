package com.kreig133.daogenerator.db.extractors.out;

import com.kreig133.daogenerator.db.JDBCConnector;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.sql.creators.QueryCreator;
import com.kreig133.daogenerator.sql.test.TestValueByStringGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author kreig133
 * @version 1.0
 */
public class ResultSetGetterForQuery implements  ResultSetGetter{

    @Nullable
    @Override
    public ResultSet getResultSetAndFillJdbcTypeIfNeed(
            @NotNull DaoMethod daoMethod, @NotNull Connection connection
    ) throws SQLException {

        final String query =
                QueryCreator.getQueryStringWithoutMetaData( daoMethod.getCommon().getQuery() );

        assert query != null;

        final PreparedStatement statement = connection.prepareStatement( query );

        List<String> names = QueryCreator.getListOfParametrNames( daoMethod.getCommon().getQuery() );

        for ( int i = 0; i < names.size(); i++ ) {
            ParameterType paramter = daoMethod.getInputParametrs().getParameterByName( names.get( i ) );

            assert paramter != null;

            statement.setString( i + 1, TestValueByStringGenerator.newInstance( paramter ).getTestValue( paramter ) );
        }

        return statement.execute() ? statement.getResultSet() : null ;
    }
}
