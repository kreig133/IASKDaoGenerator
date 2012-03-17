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
            //TODO проверить, что не нужна обертка
            return  new ProcedureCallCreator();

        }
        
        return new SelectQueryCreator();
    }

    protected void insertEscapedParamName(StringBuilder builder, ParameterType parameterType, boolean fullFormat ){
        builder.append( "#{" ).append( parameterType.getRenameTo() );
        if ( fullFormat ) {
            builder.append( ", mode=" ).append( parameterType.getInOut())
                    .append( ", jdbcType=" ).append( parameterType.getJdbcType() );
        }
        builder.append( "}" );
    }

}
