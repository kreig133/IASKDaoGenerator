package com.kreig133.daogenerator.sql;

import com.kreig133.daogenerator.common.strategy.FuctionalObject;
import com.kreig133.daogenerator.common.strategy.FunctionalObjectWithoutFilter;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;

import static com.kreig133.daogenerator.common.StringBuilderUtils.insertEscapedParamName;
import static com.kreig133.daogenerator.common.StringBuilderUtils.iterateForParameterList;
import static com.kreig133.daogenerator.sql.SqlUtils.getTestValue;

/**
 * @author eshangareev
 * @version 1.0
 */
public class ProcedureCallCreator {
    protected static String generateProcedureCall(
            DaoMethod daoMethod,
            FuctionalObject functionalObject
    ) {
        StringBuilder myBatisQuery      = new StringBuilder();

        myBatisQuery.append( "{CALL " ).append( daoMethod.getCommon().getSpName() ).append( "(" );

        if( !daoMethod.getInputParametrs().getParameter().isEmpty() ){
            myBatisQuery.append( "\n" );
        }

        iterateForParameterList( myBatisQuery, daoMethod.getInputParametrs().getParameter(), functionalObject );

        myBatisQuery.append( ")}" );

        return myBatisQuery.toString();
    }
    
    
    public static String generateProcedureCall( DaoMethod daoMethod, boolean forTest ){
        return ProcedureCallCreator.generateProcedureCall(
                daoMethod,
                forTest ?
                    new FunctionalObjectWithoutFilter() {
                        @Override
                        public void writeString( StringBuilder builder, ParameterType p ) {
                            builder.append( "@" ).append( p.getName() ).append( " = " ).append( getTestValue( p ) );
                        }
                    }:
                    new FunctionalObjectWithoutFilter() {
                        @Override
                        public void writeString( StringBuilder builder, ParameterType p ) {
                            insertEscapedParamName( builder, p.getName() );
                        }
                    }
        );
    }
}
