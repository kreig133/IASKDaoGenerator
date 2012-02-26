package com.kreig133.daogenerator.common.strategy;

import com.kreig133.daogenerator.parameter.Parameter;

/**
 * @author eshangareev
 * @version 1.0
 */
public interface FuctionalObject {

    void writeString( StringBuilder builder, Parameter p );

    boolean filter( Parameter p );
}
