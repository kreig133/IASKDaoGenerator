package com.kreig133.daogenerator.sql.creators;

import com.kreig133.daogenerator.common.strategy.FuctionalObject;
import com.kreig133.daogenerator.jaxb.ParameterType;

import java.util.List;

/**
 * @author kreig133
 * @version 1.0
 */
abstract public class CommonCallCreator extends QueryCreator{

    protected static StringBuilder insertParameterName( StringBuilder builder, ParameterType p ) {
        return builder.append( "@" ).append( p.getName() ).append( " = " );
    }
}
