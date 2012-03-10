package com.kreig133.daogenerator.common.strategy;

import com.kreig133.daogenerator.jaxb.ParameterType;

/**
 * @author eshangareev
 * @version 1.0
 */
public interface FuctionalObject {

    void writeString( StringBuilder builder, ParameterType p );

    boolean filter( ParameterType p );
}
