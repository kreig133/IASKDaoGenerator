package com.kreig133.daogenerator.mybatis;

import com.kreig133.daogenerator.parametr.Parameter;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class WrapperGenerator {

    public static String generateWrapperProcedure (
            List<Parameter> inputParametrs,
            List<Parameter> outputParametrs,
            String          name
    ){
        StringBuilder builder = new StringBuilder();

        builder.append( "declare @res int;\n" );
        builder.append( "create table #TempTableForNamedResultSet(\n" );

        boolean first = false;

        for( Parameter p : outputParametrs ){
            if( !first ){
                builder.append(",\n");
            } else first = false;

            builder.append( "    " );
            builder.append( p.getName() );
            builder.append( " " );
            builder.append( p.getSqlType() );
            builder.append( " " );
            builder.append( "NULL\n" );
        }

        builder.append( ");" );
        builder.append( "select @res = 0;\n" );
        builder.append( "insert into #TempTableForNamedResultSet\n" );
        builder.append( "     exec " );
        builder.append( name );
        builder.append( " " );

        first = false;
        for( Parameter p : inputParametrs ){
            builder.append( "        " );

            if( !first ){
                builder.append(",\n");
            } else first = false;

            builder.append( "@" );
            builder.append( p.getName() );
            builder.append( "\n" );
        }

        builder.append( "select @res = @@error;\n" );
        builder.append( "if @res<>0 return -1;\n" );
        builder.append( "SELECT * FROM #TempTableForNamedResultSet" );

        return builder.toString();
    }
}
