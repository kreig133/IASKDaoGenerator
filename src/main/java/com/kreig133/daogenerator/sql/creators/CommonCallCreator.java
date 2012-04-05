package com.kreig133.daogenerator.sql.creators;

import com.kreig133.daogenerator.jaxb.ParameterType;
import org.jetbrains.annotations.NotNull;

/**
 * @author kreig133
 * @version 1.0
 */
abstract public class CommonCallCreator extends QueryCreator{
    protected static String transformParameterName( @NotNull ParameterType p, String rightValue ) {
        return String.format( "@%s = %s", p.getName(), rightValue );
    }
}
