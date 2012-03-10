package com.kreig133.daogenerator.sql;

import com.kreig133.daogenerator.common.strategy.FunctionalObjectWithoutFilter;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;

import static com.kreig133.daogenerator.common.StringBuilderUtils.insertEscapedParamName;
import static com.kreig133.daogenerator.common.StringBuilderUtils.iterateForParameterList;

/**
 * @author eshangareev
 * @version 1.0
 */
public class ProcedureCallCreator {
    public static String generateProcedureCall(
        DaoMethod daoMethod
    ) {
        StringBuilder myBatisQuery      = new StringBuilder();

        myBatisQuery.append( "{CALL " ).append( daoMethod.getCommon().getSpName() ).append( "(" );

        if( !daoMethod.getInputParametrs().getParameter().isEmpty() ){
            myBatisQuery.append( "\n" );
        }

        iterateForParameterList( myBatisQuery, daoMethod.getInputParametrs().getParameter(),
                new FunctionalObjectWithoutFilter() {
                    @Override
                    public void writeString( StringBuilder builder, ParameterType p ) {
                        insertEscapedParamName( builder, p.getName() );
                    }
                } );

        myBatisQuery.append( ")}" );

        return myBatisQuery.toString();
    }
}
