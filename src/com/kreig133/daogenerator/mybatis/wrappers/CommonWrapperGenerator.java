package com.kreig133.daogenerator.mybatis.wrappers;

import com.kreig133.daogenerator.mybatis.wrappers.strategy.FuctionalObject;
import com.kreig133.daogenerator.parameter.InputParameter;
import com.kreig133.daogenerator.parameter.Parameter;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class CommonWrapperGenerator {

    protected static void declareParamInProcedure( StringBuilder builder, Parameter p ) {
        declareInTypeParamInProcedure( builder, p );
    }

    protected static void declareInTypeParamInProcedure( StringBuilder builder, Parameter p ) {
        declareParamNameInProcedure( builder, p );
        builder.append( "#{" );
        builder.append( p.getName() );
        builder.append( "}\n" );
    }

    protected static int declareParamInProcedure( StringBuilder builder, Parameter p, int index) {
        switch ( ( ( InputParameter ) p ).getInputType() ){
            case IN:
                declareInTypeParamInProcedure( builder, p );
                return index;
            case OUT:
                declareOutTypeParamInProcedure( builder, p, index );
                return ++index;
        }
        throw new AssertionError();
    }

    protected static int parameterName( StringBuilder builder, int index ){
        builder.append( "@P" );
        builder.append( index );
        builder.append( " " );

        return ++index;
    }

    private static void declareOutTypeParamInProcedure( StringBuilder builder, Parameter p , int index) {
        declareParamNameInProcedure( builder, p );
        parameterName( builder, index );
        builder.append( " output" );
    }

    private static void declareParamNameInProcedure( StringBuilder builder, Parameter p ) {
        builder.append( "@" );
        builder.append( p.getName() );
        builder.append( " = " );
    }
}
