package com.kreig133.daogenerator.sql.test;

import com.kreig133.daogenerator.jaxb.JavaType;
import com.kreig133.daogenerator.jaxb.ParameterType;
import org.jetbrains.annotations.NotNull;

/**
 * @author kreig133
 * @version 1.0
 */
public class TestValueByStringGenerator {

    public static final String NULL = "null";

    @NotNull
    public static TestValueByStringGenerator newInstance( @NotNull ParameterType p ){
        if( p.getType() == JavaType.DATE || p.getType() == JavaType.STRING ){
            return new QuotedTestValueByStringGenerator();
        }
        
        return new TestValueByStringGenerator();
    }

    public String getTestValue( @NotNull ParameterType parameterType ){
        if ( parameterType.getTestValue() == null || NULL.equalsIgnoreCase( parameterType.getTestValue() ) ) {
            return NULL;
        }

        return getNotNullTestValue( parameterType );
    }

    protected String getNotNullTestValue( @NotNull ParameterType parameterType ) {
        String testValue = parameterType.getTestValue();

        if( parameterType.getType() != JavaType.STRING && "".equals( testValue ) ){
            return NULL;
        }
        return testValue;
    }
}
