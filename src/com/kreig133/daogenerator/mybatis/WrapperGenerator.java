package com.kreig133.daogenerator.mybatis;

import com.kreig133.daogenerator.Settings;
import com.kreig133.daogenerator.parametr.Parameter;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class WrapperGenerator {

    public static String generateWrapperProcedure (
        Settings settings
    ){
        switch ( settings.getSelectType() ){
            case GENERATE:
                return generateWrapperForStoreProcedureWithoutColumnName( settings );
            case GENEROUT:
                return generateWrapperForStoreProcedureWithoutResultSet( settings );
        }
        throw new IllegalArgumentException();
    }

    private static String generateWrapperForStoreProcedureWithoutResultSet( Settings settings ) {
        final List<Parameter> inputParametrs    = settings.getInputParameterList();
        final List<Parameter> outputParametrs   = settings.getOutputParameterList();
        final String          name              = settings.getFunctionName();

        StringBuilder builder = new StringBuilder();

        builder.append( "" );




        return builder.toString();
    }

    private static String generateWrapperForStoreProcedureWithoutColumnName( Settings settings ) {
        final List<Parameter> inputParametrs    = settings.getInputParameterList();
        final List<Parameter> outputParametrs   = settings.getOutputParameterList();
        final String          name              = settings.getFunctionName();

        StringBuilder builder = new StringBuilder();

        builder.append( "create table #TempTableForNamedResultSet(\n" );

        boolean first = true;

        for( Parameter p : outputParametrs ){
            if( !first ){
                builder.append("    ,");
            } else {
                builder.append( "    " );
                first = false;
            }


            builder.append( p.getName() );
            builder.append( " " );
            builder.append( p.getSqlType() );
            builder.append( " " );
            builder.append( "NULL\n" );
        }

        builder.append( ");\n" );
        builder.append( "insert into #TempTableForNamedResultSet\n" );
        builder.append( "     exec " );
        builder.append( name );
        builder.append( "\n" );

        first = true;
        for( Parameter p : inputParametrs ){
            builder.append( "        " );

            if( !first ){
                builder.append(",");
            } else {
                first = false;
            }

            builder.append( "@" );
            builder.append( p.getName() );
            builder.append( " = ${" );
            builder.append( p.getName() );
            builder.append( "}\n" );
        }

        builder.append( ";\n" );
        builder.append( "SELECT * FROM #TempTableForNamedResultSet" );

        return builder.toString();
    }
}
