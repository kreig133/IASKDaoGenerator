package com.kreig133.daogenerator.mybatis;

import com.kreig133.daogenerator.InOutClass;
import com.kreig133.daogenerator.Settings;
import com.kreig133.daogenerator.Utils;
import com.kreig133.daogenerator.parametr.Parameter;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class XmlMappingGenerator {
    public static String generateXmlMapping(
        Settings settings
    ){
        List<Parameter> INPUT_PARAMETER_LIST    = settings.getInputParameterList();
        List<Parameter> OUTPUT_PARAMETER_LIST   = settings.getOutputParameterList();
        String name                             = settings.getFunctionName();

        StringBuilder result = new StringBuilder();
        result.append("    <select id=\"");
        result.append(name);
        result.append("\" statementType=\"CALLABLE\"");
        if (!INPUT_PARAMETER_LIST.isEmpty()) {
            result.append("\n        parameterType=\"");
            result.append( InOutClass.PACKAGE);
            result.append(".");
            result.append( Utils.convertNameForClassNaming( name ));
            result.append("In\"");
        }
        if (!OUTPUT_PARAMETER_LIST.isEmpty()) {
            result.append("\n        resultType=\"");
            result.append(InOutClass.PACKAGE);
            result.append(".");
            result.append(Utils.convertNameForClassNaming(name));
            result.append("Out\"");
        }
        result.append(">\n\n");
        result.append( generateProcedureCall( INPUT_PARAMETER_LIST, name ) );
        result.append("        )}\n\n");
        result.append("    </select>\n\n");

        return result.toString();
    }

    public static String generateProcedureCall(
            List<Parameter> INPUT_PARAMETER_LIST,
            String name
    ) {
        StringBuilder result = new StringBuilder();

        result.append("        {CALL ");
        result.append( name );
        result.append( "(\n" );
        boolean  first = true;
        for (Parameter p : INPUT_PARAMETER_LIST) {
            if( !first ){
                result.append( "           ,#{" );
            } else {
                first = false;
                result.append( "            #{" );
            }
            result.append( p.getName().trim() );
            result.append( "}\n");
        }
        result.append( "        )}" );

        return result.toString();
    }
}
