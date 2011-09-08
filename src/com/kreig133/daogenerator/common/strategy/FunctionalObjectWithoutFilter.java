package com.kreig133.daogenerator.common.strategy;

import com.kreig133.daogenerator.parameter.Parameter;

/**
 * @author eshangareev
 * @version 1.0
 */
abstract public class FunctionalObjectWithoutFilter implements FuctionalObject {
    @Override
    public boolean filter( Parameter p ) {
        return true;
    }
}
