package com.kreig133.daogenerator.sql.wrappers;

import com.kreig133.daogenerator.parameter.InputParameter;
import com.kreig133.daogenerator.parameter.Parameter;

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
        builder.append( "#{" ).append( p.getName() ).append( "}" );
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

    protected static int declareParamInProcedureForTesting( StringBuilder builder, Parameter p, int index ){
        switch ( ( ( InputParameter ) p ).getInputType() ) {
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

    private static void declareOutTypeParamInProcedure( StringBuilder builder, Parameter p , int index) {
        declareParamNameInProcedure ( builder, p );
        parameterName               ( builder, index );
        builder.append( " output" );
    }

    private static void declareParamNameInProcedure( StringBuilder builder, Parameter p ) {
        builder.append( "@" ).append( p.getName() ).append( " = " );
    }
}
