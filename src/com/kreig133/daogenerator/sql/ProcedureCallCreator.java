package com.kreig133.daogenerator.sql;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.strategy.FunctionalObjectWithoutFilter;
import com.kreig133.daogenerator.parameter.Parameter;

import java.util.List;

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
        StringBuilder queryForTesting   = new StringBuilder();

        myBatisQuery.append( "        {CALL " ).append( functionSettings.getFunctionName() ).append( "(\n" );
        queryForTesting.append( myBatisQuery.toString() );

        iterateForParameterList( myBatisQuery, functionSettings.getInputParameterList(), 3,
                new FunctionalObjectWithoutFilter() {
                    @Override
                    public void writeString( StringBuilder builder, Parameter p ) {
                        builder.append( "#{" ).append( p.getName() ).append( "}" );
                    }
                } );

        iterateForParameterList( queryForTesting, functionSettings.getInputParameterList(), 3, new FunctionalObjectWithoutFilter() {
            @Override
            public void writeString( StringBuilder builder, Parameter p ) {
                builder.append( " ?" );
            }
        } );

        myBatisQuery    .append( "        )}" );
        queryForTesting .append( "        )}" );

        functionSettings.setMyBatisQuery    ( myBatisQuery      .toString() );
        functionSettings.setQueryForTesting ( queryForTesting   .toString() );
    }
}
