package com.kreig133.daogenerator.sql.creators;

import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.jaxb.SelectType;

/**
 * @author kreig133
 * @version 1.0
 */
public abstract class QueryCreator {

    public abstract String generateExecuteQuery (
            DaoMethod daoMethod,
            boolean forTest
    );

    public static QueryCreator newInstance( DaoMethod daoMethod ){
        if( daoMethod.getSelectType() == SelectType.CALL ){
            if(
                    daoMethod.getOutputParametrs().getIndexOfUnnamedParameters().size() > 1 ||
                    daoMethod.getOutputParametrs().containsSameNames()
            ) {
                return new WrapperGenerator();
            }
            return  new ProcedureCallCreator();
        }
        
        return new SelectQueryCreator();
    }

    protected String getEscapedParamName( ParameterType parameterType, boolean fullFormat ){
        return fullFormat ?
                String.format(
                        "#{%s, mode=%s, jdbcType=%s}",
                        parameterType.getRenameTo(),
                        parameterType.getInOut(),
                        parameterType.getJdbcType()
                ):
                String.format( "#{%s}", parameterType.getRenameTo() ) ;
    }
}
