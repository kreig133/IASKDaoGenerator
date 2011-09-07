package com.kreig133.daogenerator.mybatis.wrappers;

import com.kreig133.daogenerator.Settings;
import com.kreig133.daogenerator.mybatis.wrappers.strategy.FuctionalObject;
import com.kreig133.daogenerator.parameter.Parameter;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class GenerateGenerator extends CommonWrapperGenerator{

    public static String generateWrapper( Settings settings ) {
        final List<Parameter> inputParametrs    = settings.getInputParameterList();
        final List<Parameter> outputParametrs   = settings.getOutputParameterList();
        final String          name              = settings.getFunctionName();

        StringBuilder builder = new StringBuilder();

        builder.append( "create table #TempTableForNamedResultSet(\n" );

        iterateForParametrList( builder, outputParametrs, new FuctionalObject() {
            @Override
            public void writeString( StringBuilder builder, Parameter p ) {
                builder.append( p.getName() );
                builder.append( " " );
                builder.append( p.getSqlType() );
                builder.append( " " );
                builder.append( "NULL" );
            }
            @Override
            public boolean filter( Parameter p ) {
                return true;
            }
        } );

        builder.append( ");\n" );
        builder.append( "insert into #TempTableForNamedResultSet\n" );
        builder.append( "     exec " );
        builder.append( name );
        builder.append( "\n" );

        iterateForParametrList( builder, outputParametrs, new FuctionalObject() {
            @Override
            public void writeString( StringBuilder builder, Parameter p ) {
                declareInTypeParamInProcedure( builder, p );
            }
            @Override
            public boolean filter( Parameter p ) {
                return true;
            }
        } );

        builder.append( ";\n" );
        builder.append( "SELECT * FROM #TempTableForNamedResultSet" );

        return builder.toString();
    }
}
