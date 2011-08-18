package com.kreig133.daogenerator.mybatis;

import com.kreig133.daogenerator.Utils;
import com.kreig133.daogenerator.enums.ReturnType;
import com.kreig133.daogenerator.enums.SelectType;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.parametr.Parameter;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class MethodGenerator {
    public static String methodGenerator(
            final List< Parameter > INPUT_PARAMETER_LIST,
            final List< Parameter > OUTPUT_PARAMETER_LIST,
            final String            name,
            final ReturnType        returnType
    ) {
        StringBuilder result = new StringBuilder( );
        result.append("    ");

        if (!OUTPUT_PARAMETER_LIST.isEmpty()) {
            if( returnType == ReturnType.MULTIPLE ){
                result.append( "List< " );
            }

            result.append( Utils.convertNameForClassNaming( name ));
            result.append("Out ");

            if( returnType == ReturnType.MULTIPLE ){
                result.append( "> " );
            }
        } else {
            result.append("void ");
        }

        result.append(name);
        result.append("(");
        if (!INPUT_PARAMETER_LIST.isEmpty()) {
            result.append(Utils.convertNameForClassNaming(name));
            result.append("In request");
        }
        result.append(");\n");

        return result.toString();
    }
}
