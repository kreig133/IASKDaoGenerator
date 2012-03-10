package com.kreig133.daogenerator.common.strategy;

import com.kreig133.daogenerator.jaxb.ParameterType;

/**
 * @author eshangareev
 * @version 1.0
 */
abstract public class FunctionalObjectWithoutFilter implements FuctionalObject {
    @Override
    public boolean filter( ParameterType p ) {
        return true;
    }
}
