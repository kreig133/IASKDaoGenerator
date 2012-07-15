package com.kreig133.daogenerator.db.extractors.out;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.db.extractors.Extractor;
import com.kreig133.daogenerator.db.extractors.SqlTypeHelper;
import com.kreig133.daogenerator.jaxb.*;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author kreig133
 * @version 1.0
 */
public class OutputParameterExtractor extends Extractor{

    private static final OutputParameterExtractor INSTANCE = new OutputParameterExtractor();

    private OutputParameterExtractor() {
    }

    @NotNull
    public static OutputParameterExtractor instance( @NotNull SelectType type ){
        return INSTANCE;
    }

    @NotNull
    public DaoMethod getOutputParameters( @NotNull final DaoMethod daoMethod ){
        ResultSet resultSet = null;
        try {
            try {
                resultSet = ResultSetGetter.Factory.get( daoMethod.getSelectType() )
                        .getResultSetAndFillJdbcTypeIfNeed( daoMethod );

                daoMethod.setOutputParametrs( new ParametersType() );
                if( resultSet != null ){
                    final ResultSetMetaData metaData = resultSet.getMetaData();
                    final List<ParameterType> parameterTypes = new LinkedList<ParameterType>();

                    for ( int i = 1; i <= metaData.getColumnCount(); i++ ) {
                        final ParameterType parameterType = new ParameterType();
                        parameterType.setName( metaData.getColumnName( i ) );
                        parameterType.setRenameTo( Utils.convertPBNameToName( parameterType.getName() ) );
                        parameterType.setSqlType( SqlTypeHelper.getSqlTypeFromResultSet( metaData, i ) );
                        parameterType.setType( JavaType.getBySqlType( metaData.getColumnTypeName( i ) ) );

                        parameterTypes.add( parameterType );
                    }

                    daoMethod.getOutputParametrs().getParameter().addAll( parameterTypes );
                }
            } finally {
                if ( resultSet != null ) {
                    resultSet.close();
                }
            }
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
        return daoMethod;
    }
}
