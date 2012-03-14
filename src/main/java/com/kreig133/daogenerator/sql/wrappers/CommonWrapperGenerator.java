package com.kreig133.daogenerator.sql.wrappers;

import com.kreig133.daogenerator.jaxb.ParameterType;

import static com.kreig133.daogenerator.common.StringBuilderUtils.insertEscapedParamName;

/**
 * @author eshangareev
 * @version 1.0
 */
public class CommonWrapperGenerator {

    protected static void declareParamInProcedure( StringBuilder builder, ParameterType p ) {
        declareInTypeParamInProcedure( builder, p );
    }

    protected static void declareInTypeParamInProcedure( StringBuilder builder, ParameterType p ) {
        declareParamNameInProcedure( builder, p );
        insertEscapedParamName( builder, p );
    }

    protected static int declareParamInProcedure( StringBuilder builder, ParameterType p, int index) {
        switch ( p.getInOut() ){
            case IN:
                declareInTypeParamInProcedure( builder, p );
                return index;
            case OUT:
                declareOutTypeParamInProcedure( builder, p, index );
                return ++index;
        }
        throw new AssertionError();
    }

    protected static int declareParamInProcedureForTesting( StringBuilder builder, ParameterType p, int index ){
        switch (  p.getInOut() ) {
            case IN:
                declareParamNameInProcedure( builder, p );
                builder.append( " ?" );
                return index;
            case OUT:
                declareOutTypeParamInProcedure( builder, p, index );
                return ++index;
        }
        throw new AssertionError();
    }

    protected static int parameterName( StringBuilder builder, int index ){
        builder.append( "@P" ).append( index ).append( " " );

        return ++index;
    }

    private static void declareOutTypeParamInProcedure( StringBuilder builder, ParameterType p, int index ) {
        declareParamNameInProcedure( builder, p );
        parameterName( builder, index );
        builder.append( " output" );
    }

    private static void declareParamNameInProcedure( StringBuilder builder, ParameterType p ) {
        builder.append( "@" ).append( p.getName() ).append( " = " );
    }
}
