package com.kreig133.daogenerator.sql.wrappers;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.strategy.FunctionalObjectWithoutFilter;
import com.kreig133.daogenerator.parameter.Parameter;

import java.util.List;
import java.util.Map;

import static com.kreig133.daogenerator.common.StringBufferUtils.insertTabs;
import static com.kreig133.daogenerator.common.StringBufferUtils.iterateForParameterList;

/**
 * @author eshangareev
 * @version 1.0
 */
public class GenerateGenerator extends CommonWrapperGenerator{

    public static void generateWrapper( FunctionSettings functionSettings ) {
        functionSettings.setMyBatisQuery   ( generateWrapper( functionSettings, false ) );
    }

    public static String generateWrapper( FunctionSettings functionSettings, boolean forTests ) {
        final List<Parameter> outputParametrs  = functionSettings.getOutputParameterList();
        final List<Parameter> inputParametrs   = functionSettings.getInputParameterList();


        StringBuilder builder      = new StringBuilder();

        builder.append( "create table #TempTableForNamedResultSet(\n" );

        iterateForParameterList( builder, outputParametrs, new FunctionalObjectWithoutFilter() {
            @Override
            public void writeString( StringBuilder builder, Parameter p ) {
                builder.append( p.getName() ).append( " " ).append( p.getSqlType() ).append( " " ).append( "NULL" );
            }
        } );

        builder.append( ");\n" );
        builder.append( "insert into #TempTableForNamedResultSet\n" );
        insertTabs( builder, 1 ).append( "exec " ).append(  functionSettings.getNameForCall() ).append( "\n" );

        if( !forTests ){
            iterateForParameterList( builder, inputParametrs, 2, new FunctionalObjectWithoutFilter() {
                @Override
                public void writeString( StringBuilder builder, Parameter p ) {
                    declareInTypeParamInProcedure( builder, p );
                }
            } );
        } else {
            iterateForParameterList( builder, inputParametrs, 2, new FunctionalObjectWithoutFilter() {
                @Override
                public void writeString( StringBuilder builder, Parameter p ) {
                    builder.append( " ?" );
                }
            } );
        }

        builder.append( ";\n" );
        builder.append( "SELECT * FROM #TempTableForNamedResultSet" );

        return builder.toString();
    }
}
