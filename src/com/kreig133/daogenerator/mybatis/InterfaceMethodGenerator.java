package com.kreig133.daogenerator.mybatis;

import com.kreig133.daogenerator.common.Settings;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.enums.MethodType;
import com.kreig133.daogenerator.enums.ReturnType;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.mybatis.wrappers.strategy.FuctionalObject;
import com.kreig133.daogenerator.parameter.Parameter;

import java.util.List;

import static com.kreig133.daogenerator.common.Utils.*;
/**
 * @author eshangareev
 * @version 1.0
 */
public class InterfaceMethodGenerator {

    public static String methodGenerator(
        final Settings settings,
        final MethodType methodType
    ) {
        return "    " + generateMethodSignature( settings, methodType ) + ";\n";
    }

    public static String generateMethodSignature(
        final Settings settings,
        final MethodType methodType
    ) {

        final List< Parameter > INPUT_PARAMETER_LIST    = settings.getInputParameterList();
        final List< Parameter > OUTPUT_PARAMETER_LIST   = settings.getOutputParameterList();
        final String            name                    = settings.getFunctionName();
        final ReturnType        returnType              = settings.getReturnType();

        StringBuilder result = new StringBuilder( );

        if(OUTPUT_PARAMETER_LIST.isEmpty()){
            result.append("void ");
        } else {
            if( returnType == ReturnType.MULTIPLE ){
                result.append( "List< " );
            }
            if( OUTPUT_PARAMETER_LIST.size() == 1 ){
                result.append( OUTPUT_PARAMETER_LIST.get( 0 ).getType() );
            } else {
                result.append( Utils.convertNameForClassNaming( name ));
                result.append("Out");
            }
            result.append( " " );
            if( returnType == ReturnType.MULTIPLE ){
                result.append( "> " );
            }
        }

        result.append( name );
        result.append( "(\n" );
        if ( ! INPUT_PARAMETER_LIST.isEmpty() ) {
            if( checkToNeedOwnInClass( settings ) ){
                result.append( "        " );
                result.append( Utils.convertNameForClassNaming( name ) );
                result.append( "In request\n" );
            } else {
                iterateForParameterList( result, INPUT_PARAMETER_LIST, 2, new FuctionalObject() {
                    @Override
                    public void writeString( StringBuilder builder, Parameter p ) {
                        if ( settings.getType() == Type.DEPO && methodType == MethodType.MAPPER ){
                            builder.append( "@Param(\"" );
                            builder.append( p.getName() );
                            builder.append( "\") " );
                        }
                        builder.append( p.getType() );
                        builder.append( " " );
                        builder.append( p.getName() );
                    }
                    @Override
                    public boolean filter( Parameter p ) {
                        return true;
                    }
                } );
            }
        }
        result.append( "    )" );

        return result.toString();
    }
}
