package com.kreig133.daogenerator.mybatis;

import com.kreig133.daogenerator.common.Settings;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.mybatis.wrappers.strategy.FuctionalObject;
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
        List<Parameter> inputParameterList    = settings.getInputParameterList();
        List<Parameter> outputParameterList   = settings.getOutputParameterList();
        String name                             = settings.getFunctionName();
        String package_                         = settings.getPackage();

        StringBuilder result = new StringBuilder();
        result.append("    <select id=\"").append( name ).append( "\" statementType=\"CALLABLE\"" );

        writeParameterType(  inputParameterList, name, "parameterType", "In" , package_, result );
        writeParameterType( outputParameterList, name, "resultType"   , "Out", package_, result );

        result.append(">\n\n");

        result.append( generateProcedureCall( inputParameterList, name ) );

        result.append("        )}\n\n");
        result.append("    </select>\n\n");

        return result.toString();
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

        iterateForParameterList( result, inputParameterList, 3, new FuctionalObject() {
            @Override
            public void writeString( StringBuilder builder, Parameter p ) {
                builder.append( "#{" );
                builder.append( p.getName() );
                builder.append( "}");
            }
            @Override
            public boolean filter( Parameter p ) {
                return true;
            }
        } );

        result.append( "        )}" );

        return result.toString();
    }
}
