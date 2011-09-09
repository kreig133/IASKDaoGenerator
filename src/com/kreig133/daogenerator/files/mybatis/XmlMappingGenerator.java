package com.kreig133.daogenerator.files.mybatis;

import com.kreig133.daogenerator.common.Settings;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.common.strategy.FunctionalObjectWithoutFilter;
import com.kreig133.daogenerator.parameter.Parameter;

import java.util.List;

import static com.kreig133.daogenerator.common.Utils.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class XmlMappingGenerator {
    public static String generateXmlMapping(
        Settings settings
    ){
        final List<Parameter> inputParameterList  = settings.getInputParameterList();
        final List<Parameter> outputParameterList = settings.getOutputParameterList();
        final String name                         = settings.getFunctionName();
        final String package_                     = settings.getEntityPackage();

        StringBuilder builder = new StringBuilder();
        builder.append( "    <select id=\"" ).append( name ).append( "\" statementType=\"CALLABLE\"" );

        writeParameterType(  inputParameterList, name, "parameterType", "In" , package_, builder );
        writeParameterType( outputParameterList, name, "resultType"   , "Out", package_, builder );

        builder.append( ">\n\n" );

        builder.append( generateProcedureCall( inputParameterList, name ) );

        builder.append( "        )}\n\n" );
        builder.append( "    </select>\n\n" );

        return builder.toString();
    }

    private static void writeParameterType(
            List<Parameter> outputParameterList,
            String name,
            String type,
            String suffix,
            String package_,
            StringBuilder result
    ) {
        if ( ! outputParameterList.isEmpty() ) {
            result.append( "\n        " ).append( type ).append( "=\"" );
            if ( outputParameterList.size() > 1 ) {
                result.append( package_ ).append( "." );
                result.append( Utils.convertNameForClassNaming( name ) ).append( suffix );
            } else {
                result.append( "java.lang." ).append( outputParameterList.get( 0 ).getType() );
            }
            result.append( "\"" );
        }
    }

    public static String generateProcedureCall(
            List<Parameter> inputParameterList,
            String name
    ) {
        StringBuilder result = new StringBuilder();

        result.append("        {CALL ").append( name ).append( "(\n" );

        iterateForParameterList( result, inputParameterList, 3, new FunctionalObjectWithoutFilter() {
            @Override
            public void writeString( StringBuilder builder, Parameter p ) {
                builder.append( "#{" ).append( p.getName() ).append( "}" );
            }
        } );

        result.append( "        )}" );

        return result.toString();
    }
}
