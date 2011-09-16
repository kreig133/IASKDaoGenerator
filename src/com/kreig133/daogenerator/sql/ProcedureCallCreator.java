package com.kreig133.daogenerator.sql;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.strategy.FunctionalObjectWithoutFilter;
import com.kreig133.daogenerator.parameter.Parameter;

import static com.kreig133.daogenerator.common.Utils.iterateForParameterList;

/**
 * @author eshangareev
 * @version 1.0
 */
public class ProcedureCallCreator {
    public static void generateProcedureCall(
        FunctionSettings functionSettings
    ) {
        StringBuilder myBatisQuery      = new StringBuilder();

        myBatisQuery.append( "        {CALL " ).append( functionSettings.getNameForCall() ).append( "(\n" );

        iterateForParameterList( myBatisQuery, functionSettings.getInputParameterList(), 3,
                new FunctionalObjectWithoutFilter() {
                    @Override
                    public void writeString( StringBuilder builder, Parameter p ) {
                        builder.append( "#{" ).append( p.getName() ).append( "}" );
                    }
                } );

        myBatisQuery    .append( "        )}" );

        functionSettings.setMyBatisQuery( myBatisQuery.toString() );
    }
}
