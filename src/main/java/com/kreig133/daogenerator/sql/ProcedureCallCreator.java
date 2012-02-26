package com.kreig133.daogenerator.sql;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.strategy.FunctionalObjectWithoutFilter;
import com.kreig133.daogenerator.parameter.Parameter;

import static com.kreig133.daogenerator.common.StringBuilderUtils.insertEscapedParamName;
import static com.kreig133.daogenerator.common.StringBuilderUtils.iterateForParameterList;

/**
 * @author eshangareev
 * @version 1.0
 */
public class ProcedureCallCreator {
    public static void generateProcedureCall(
        FunctionSettings functionSettings
    ) {
        StringBuilder myBatisQuery      = new StringBuilder();

        myBatisQuery.append( "{CALL " ).append( functionSettings.getNameForCall() ).append( "(" );

        if( !functionSettings.getInputParameterList().isEmpty() ){
            myBatisQuery.append( "\n" );
        }

        iterateForParameterList( myBatisQuery, functionSettings.getInputParameterList(),
                new FunctionalObjectWithoutFilter() {
                    @Override
                    public void writeString( StringBuilder builder, Parameter p ) {
                        insertEscapedParamName( builder, p.getName() );
                    }
        } );

        myBatisQuery    .append( ")}" );

        functionSettings.setMyBatisQuery( myBatisQuery.toString() );
    }
}
