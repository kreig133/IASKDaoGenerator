package com.kreig133.daogenerator.db;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.JavaType;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.jaxb.ParametersType;
import com.kreig133.daogenerator.sql.SqlQueryCreator;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class GetOutputParametersFromResultSet {

    public static DaoMethod getOutputParameters( DaoMethod daoMethod ){
        final String queries = SqlQueryCreator.createQueries( daoMethod, true );

        assert queries != null;
        System.out.println();
        System.out.println( queries );
        System.out.println();
        final Connection connection = JDBCConnector.connectToDB();

        try {
            final CallableStatement callableStatement = connection.prepareCall( queries );
            connection.createStatement().execute( "SET NOCOUNT ON;" );
            final ResultSet resultSet = callableStatement.executeQuery();
            connection.createStatement().execute( "SET NOCOUNT OFF;" );

            final ResultSetMetaData metaData = resultSet.getMetaData();
            final List<ParameterType> parameterTypes = new LinkedList<ParameterType>();
            for ( int i = 1; i <= metaData.getColumnCount(); i++ ) {
                final ParameterType parameterType = new ParameterType();
                parameterType.setName( metaData.getColumnName( i ) );
                parameterType.setRenameTo( Utils. convertPBNameToName( parameterType.getName() ) );
                parameterType.setSqlType( metaData.getColumnTypeName( i ) );
                parameterType.setType( JavaType.getBySqlType( metaData.getColumnTypeName( i )) );

                parameterTypes.add( parameterType );
            }

            daoMethod.setOutputParametrs( new ParametersType() );
            daoMethod.getOutputParametrs().getParameter().addAll( parameterTypes );

        } catch ( SQLException e ) {
            e.printStackTrace(); // TODO
        }

        return daoMethod;
    }
}

