package com.kreig133.daogenerator.sql.wrappers;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.strategy.FunctionalObjectWithoutFilter;
import com.kreig133.daogenerator.parameter.Parameter;

import java.util.List;

import static com.kreig133.daogenerator.common.Utils.iterateForParameterList;

/**
 * @author eshangareev
 * @version 1.0
 */
public class GenerateGenerator extends CommonWrapperGenerator{

    public static void generateWrapper( FunctionSettings functionSettings ) {
        final List<Parameter> outputParametrs  = functionSettings.getOutputParameterList();
        final List<Parameter> inputParametrs   = functionSettings.getInputParameterList();


        StringBuilder myBatisQuery      = new StringBuilder();
        StringBuilder queryForTesting   = new StringBuilder();

        myBatisQuery.append( "create table #TempTableForNamedResultSet(\n" );

        iterateForParameterList( myBatisQuery, outputParametrs, new FunctionalObjectWithoutFilter() {
            @Override
            public void writeString( StringBuilder builder, Parameter p ) {
                builder.append( p.getName() ).append( " " ).append( p.getSqlType() ).append( " " ).append( "NULL" );
            }
        } );

        myBatisQuery.append( ");\n" );
        myBatisQuery.append( "insert into #TempTableForNamedResultSet\n" );
        myBatisQuery.append( "     exec " ).append(  functionSettings.getNameForCall() ).append( "\n" );

        queryForTesting.append( myBatisQuery.toString() );

        iterateForParameterList( myBatisQuery, inputParametrs, 2, new FunctionalObjectWithoutFilter() {
            @Override
            public void writeString( StringBuilder builder, Parameter p ) {
                declareInTypeParamInProcedure( builder, p );
            }
        } );

        iterateForParameterList( queryForTesting, inputParametrs, 2, new FunctionalObjectWithoutFilter() {
            @Override
            public void writeString( StringBuilder builder, Parameter p ) {
                builder.append( " ?" );
            }
        } );

        myBatisQuery.append( ";\n" );
        queryForTesting.append( ";\n" );
        myBatisQuery.append( "SELECT * FROM #TempTableForNamedResultSet" );
        queryForTesting.append( "SELECT * FROM #TempTableForNamedResultSet" );

        functionSettings.setMyBatisQuery   ( myBatisQuery   .toString() );
        functionSettings.appendToQueryForTesting( queryForTesting.toString() );
    }
}
