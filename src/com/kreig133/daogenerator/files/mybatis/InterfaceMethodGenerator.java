package com.kreig133.daogenerator.files.mybatis;

import com.kreig133.daogenerator.common.Settings;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.common.strategy.FunctionalObjectWithoutFilter;
import com.kreig133.daogenerator.enums.MethodType;
import com.kreig133.daogenerator.enums.ReturnType;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.parameter.Parameter;

import java.util.List;

import static com.kreig133.daogenerator.common.Utils.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class InterfaceMethodGenerator {

    public static String methodGenerator(
            final Settings settings
    ) {
        return "    " + generateMethodSignature( settings, MethodType.DAO ) + ";\n";
    }

    public static String generateMethodSignature(
            final Settings settings,
            final MethodType methodType
    ) {

        final List<Parameter>  inputParameterList = settings.getInputParameterList();
        final List<Parameter> outputParameterList = settings.getOutputParameterList();
        final String name = settings.getFunctionName();
        final ReturnType returnType = settings.getReturnType();

        StringBuilder builder = new StringBuilder();

        if ( outputParameterList.isEmpty() ) {
            builder.append( "void " );
        } else {
            if ( returnType == ReturnType.MULTIPLE ) {
                builder.append( "List< " );
            }
            if ( outputParameterList.size() == 1 ) {
                builder.append( outputParameterList.get( 0 ).getType() );
            } else {
                builder.append( Utils.convertNameForClassNaming( name ) ).append( "Out" );
            }
            builder.append( " " );
            if ( returnType == ReturnType.MULTIPLE ) {
                builder.append( "> " );
            }
        }

        builder.append( name ).append( "(\n" );

        if ( ! inputParameterList.isEmpty() ) {
            if ( checkToNeedOwnInClass( settings ) ) {
                builder.append( "        " ).append( Utils.convertNameForClassNaming( name ) ).append( "In request\n" );
            } else {
                iterateForParameterList( builder, inputParameterList, 2, new FunctionalObjectWithoutFilter() {
                    @Override
                    public void writeString( StringBuilder builder, Parameter p ) {
                        if ( settings.getType() == Type.DEPO && methodType == MethodType.MAPPER ) {
                            builder.append( "@Param(\"" ).append( p.getName() ).append( "\") " );
                        }
                        builder.append( p.getType() ).append( " " ).append( p.getName() );
                    }
                } );
            }
        }
        builder.append( "    )" );

        return builder.toString();
    }
}
