package com.kreig133.daogenerator.db.extractor;

import com.kreig133.daogenerator.db.JBDCTypeIdConverter;
import com.kreig133.daogenerator.jaxb.*;
import com.kreig133.daogenerator.sql.SqlQueryCreator;
import com.kreig133.daogenerator.sql.SqlQueryParser;
import com.kreig133.daogenerator.sql.SqlUtils;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author kreig133
 * @version 1.0
 */
public abstract class OutputParameterExtractor {

    protected void fillJdbcTypeForInputParameters( ParameterMetaData parameterMetaData, DaoMethod daoMethod )
            throws SQLException {
        for ( ParameterType p : daoMethod.getInputParametrs().getParameter() ) {
            p.setJdbcType( JBDCTypeIdConverter.getJdbcTypeNameById( parameterMetaData.getParameterType(
                    daoMethod.getInputParametrs().getParameter().indexOf( p ) + 1
            ) ) );
        }
    }

    public DaoMethod getOutputParameters( final DaoMethod daoMethod ){

        try {
            final ResultSet resultSet = getResultSet( daoMethod );
            if( resultSet != null ){

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
            }
        } catch ( SQLException e ) {
            e.printStackTrace(); // TODO
        }

        return daoMethod;
    }

    protected abstract ResultSet getResultSet( DaoMethod daoMethod ) throws SQLException;
}
