package com.kreig133.daogenerator.sql.test;

import com.kreig133.daogenerator.jaxb.ParameterType;

/**
 * @author kreig133
 * @version 1.0
 */
public class QuotedTestValueByStringGenerator extends TestValueByStringGenerator{
    @Override
    protected String getNotNullTestValue( ParameterType parameterType ) {
        return "'" +parameterType.getTestValue() + "'";
    }
}
