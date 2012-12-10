package com.kreig133.daogenerator.files.equalshashbuilder;

import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.jaxb.ParametersType;

/**
 * @author zildarius
 * @version 1.0
 */
public class HashBuilder {
    private static final String CLASS_BEGIN = "@Override\n" +
                                              "    public int hashCode() {\n";

    private static final String CLASS_END = "        return result;\n" +
                                            "    }";

    private static final String PRE_PARAMETER = "        int result = %s != null ? %s.hashCode() : 0;\n";
    private static final String PARAMETER = "        result = 31 * result + (%s != null ? %s.hashCode() : 0);\n";

    public static String hashBuilder(ParametersType params){
        StringBuilder result = new StringBuilder(CLASS_BEGIN);

        boolean isFirst = true;
        for (ParameterType param : params.getParameter()){
            if (isFirst){
                result.append( String.format(PRE_PARAMETER, param.getRenameTo(), param.getRenameTo()));
                isFirst = false;
            } else {
                result.append( String.format(PARAMETER, param.getRenameTo(), param.getRenameTo()));
            }
        }

        result.append(CLASS_END);
        return result.toString();
    }
}
