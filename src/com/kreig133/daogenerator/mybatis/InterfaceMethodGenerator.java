package com.kreig133.daogenerator.mybatis;

import com.kreig133.daogenerator.Settings;
import com.kreig133.daogenerator.Utils;
import com.kreig133.daogenerator.enums.ReturnType;
import com.kreig133.daogenerator.parameter.Parameter;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class InterfaceMethodGenerator {

    public static String methodGenerator(
        Settings settings
    ) {
        return "    " + generateMethodSignature( settings ) + ";\n";
    }

    public static String generateMethodSignature(
        Settings settings
    ) {

        final List< Parameter > INPUT_PARAMETER_LIST    = settings.getInputParameterList();
        final List< Parameter > OUTPUT_PARAMETER_LIST   = settings.getOutputParameterList();
        final String            name                    = settings.getFunctionName();
        final ReturnType        returnType              = settings.getReturnType();

        StringBuilder result = new StringBuilder( );

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
        result.append(")");

        return result.toString();
    }
}
