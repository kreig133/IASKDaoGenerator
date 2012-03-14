package com.kreig133.daogenerator.sql;

import com.kreig133.daogenerator.common.strategy.FuctionalObject;
import com.kreig133.daogenerator.common.strategy.FunctionalObjectWithoutFilter;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.jaxb.SelectType;

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
    
    
    public static String generateProcedureCall( final DaoMethod daoMethod, boolean forTest ){
        return ProcedureCallCreator.generateProcedureCall(
                daoMethod,
                forTest ?
                    new FunctionalObjectWithoutFilter() {
                        @Override
                        public void writeString( StringBuilder builder, ParameterType p ) {
                            insertParameterName( builder, p ).append( getTestValue( p ) );
                        }
                    }:
                    new FunctionalObjectWithoutFilter() {
                        @Override
                        public void writeString( StringBuilder builder, ParameterType p ) {
                            final SelectType type = daoMethod.getCommon().getConfiguration().getType();
                            insertEscapedParamName(
                                    insertParameterName( builder, p ),
                                    p,
                                    type == SelectType.SELECT || type == SelectType.CALL
                            );
                        }
                    }
        );
    }

    private static StringBuilder insertParameterName( StringBuilder builder, ParameterType p ) {
        return builder.append( "@" ).append( p.getName() ).append( " = " );
    }
}
