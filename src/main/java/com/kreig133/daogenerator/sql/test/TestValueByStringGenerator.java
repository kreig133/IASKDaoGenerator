package com.kreig133.daogenerator.sql.test;

import com.kreig133.daogenerator.jaxb.JavaType;
import com.kreig133.daogenerator.jaxb.ParameterType;

/**
 * @author kreig133
 * @version 1.0
 */
public class TestValueByStringGenerator {
    
    public String getTestValue ( ParameterType parameterType ){
        if ( parameterType.getTestValue() == null || "null".equals( parameterType.getTestValue() ) ) {
            return "NULL";
        }

        return getNotNullTestValue( parameterType );
    }

    protected String getNotNullTestValue( ParameterType parameterType ) {
        return parameterType.getTestValue();
    }
}
