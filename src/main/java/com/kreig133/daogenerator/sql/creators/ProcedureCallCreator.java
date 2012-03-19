package com.kreig133.daogenerator.sql.creators;

import com.kreig133.daogenerator.common.FuctionalObject;
import com.kreig133.daogenerator.common.FunctionalObjectWithoutFilter;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.jaxb.SelectType;
import com.kreig133.daogenerator.sql.test.TestValueByStringGenerator;

import static com.kreig133.daogenerator.common.Utils.iterateForParameterList;

/**
 * @author eshangareev
 * @version 1.0
 */
public class ProcedureCallCreator extends CommonCallCreator{

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

    @Override
    public String generateExecuteQuery( final DaoMethod daoMethod, boolean forTest ) {
        return ProcedureCallCreator.generateProcedureCall(
                daoMethod,
                forTest ?
                        new FunctionalObjectWithoutFilter() {
                            @Override
                            public void writeString( StringBuilder builder, ParameterType p ) {
                                insertParameterName( builder, p ).append( TestValueByStringGenerator.newInstance( p )
                                        .getTestValue( p ) );
                            }
                        } :
                        new FunctionalObjectWithoutFilter() {
                            @Override
                            public void writeString( StringBuilder builder, ParameterType p ) {
                                final SelectType type = daoMethod.getSelectType();
                                insertEscapedParamName(
                                        insertParameterName( builder, p ),
                                        p,
                                        type == SelectType.SELECT || type == SelectType.CALL
                                );
                            }
                        }
        );
    }
}
