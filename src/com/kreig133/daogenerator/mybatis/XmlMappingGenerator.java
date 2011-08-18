package com.kreig133.daogenerator.mybatis;

import com.kreig133.daogenerator.InOutClass;
import com.kreig133.daogenerator.Utils;
import com.kreig133.daogenerator.parametr.Parameter;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class XmlMappingGenerator {
    public static String generateXmlMapping(
            List<Parameter> INPUT_PARAMETER_LIST,
            List<Parameter> OUTPUT_PARAMETER_LIST,
            String name
    ){
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
        generateProcedureCall( INPUT_PARAMETER_LIST, name, result );
        result.append("        )}\n\n");
        result.append("    </select>\n\n");

        return result.toString();
    }

    public static void generateProcedureCall( List<Parameter> INPUT_PARAMETER_LIST, String name,
                                             StringBuilder result ) {
        result.append("        {CALL ");
        result.append(name);
        result.append("(\n");
        boolean  first = true;
        for (Parameter p : INPUT_PARAMETER_LIST) {
            if( !first ){
                result.append(",\n");
            } else first = false;
            result.append("            #{");
            result.append(p.getName().trim());
            result.append("}\n");
        }
    }
}
