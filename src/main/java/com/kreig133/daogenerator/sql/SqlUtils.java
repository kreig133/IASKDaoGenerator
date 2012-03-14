package com.kreig133.daogenerator.sql;

import com.kreig133.daogenerator.jaxb.JavaType;
import com.kreig133.daogenerator.jaxb.ParameterType;

/**
 * @author eshangareev
 * @version 1.0
 */
public class SqlUtils {
    public static String getTestValue( ParameterType p ) {
        if ( p.getTestValue() == null || "null".equals( p.getTestValue() ) ) {
            return "NULL";
        }

        if ( p.getType() == JavaType.STRING || p.getType() == JavaType.DATE ) {
            return "'" +p.getTestValue() + "'";
        }

        return p.getTestValue();
    }
}
