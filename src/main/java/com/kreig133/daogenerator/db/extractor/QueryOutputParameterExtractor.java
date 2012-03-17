package com.kreig133.daogenerator.db.extractor;

import com.kreig133.daogenerator.db.JDBCConnector;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.sql.SqlQueryParser;
import com.kreig133.daogenerator.sql.SqlUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author kreig133
 * @version 1.0
 */
public class QueryOutputParameterExtractor extends OutputParameterExtractor{

    @Override
    protected ResultSet getResultSet( DaoMethod daoMethod ) throws SQLException {
        final String query =
                SqlQueryParser.getQueryStringWithoutMetaData( daoMethod.getCommon().getQuery() );

        assert query != null;

        final Connection connection = JDBCConnector.connectToDB();
        final PreparedStatement statement = connection.prepareStatement( query );
        final List<ParameterType> parameterTypes = daoMethod.getInputParametrs().getParameter();

        List<String> names = SqlQueryParser.getListOfParametrNames( daoMethod.getCommon().getQuery() );

        for ( int i = 0; i < names.size(); i++ ) {
            ParameterType parameterType = null;
            for ( ParameterType type : parameterTypes ) {
                if ( type.getName().equals( names.get( i ) ) ) {
                    parameterType = type;
                    break;
                }
            }

            statement.setString( i + 1, SqlUtils.getTestValue( parameterType ) );
        }

        ResultSet resultSet = null;
        if( statement.execute() ){
            resultSet = statement.getResultSet();
        }

        return resultSet ;
    }
}
