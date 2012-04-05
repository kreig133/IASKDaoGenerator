package com.kreig133.daogenerator.sql.test;

import com.kreig133.daogenerator.jaxb.ParameterType;
import org.jetbrains.annotations.NotNull;

/**
 * @author kreig133
 * @version 1.0
 */
public class QuotedTestValueByStringGenerator extends TestValueByStringGenerator{
    @NotNull
    @Override
    protected String getNotNullTestValue( @NotNull ParameterType parameterType ) {
        return "'" +parameterType.getTestValue() + "'";
    }
}
