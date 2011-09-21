package com.kreig133.daogenerator.files.mybatis;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.common.strategy.FunctionalObjectWithoutFilter;
import com.kreig133.daogenerator.enums.MethodType;
import com.kreig133.daogenerator.enums.ReturnType;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.parameter.Parameter;

import java.util.List;

import static com.kreig133.daogenerator.common.Utils.*;
import static com.kreig133.daogenerator.common.StringBufferUtils.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class InterfaceMethodGenerator {

    public static String methodGenerator(
            final OperationSettings operationSettings,
            final FunctionSettings functionSettings
    ) {
        return  addTabsBeforeLine(
                    generateMethodSignature( operationSettings, functionSettings, MethodType.DAO ) + ";\n",
                    1 );
    }

    public static String generateMethodSignature(
            final OperationSettings operationSettings,
            final FunctionSettings functionSettings,
            final MethodType methodType
    ) {

        final List<Parameter>  inputParameterList = functionSettings.getInputParameterList();
        final List<Parameter> outputParameterList = functionSettings.getOutputParameterList();
        final String     name       = functionSettings.getName();
        final ReturnType returnType = functionSettings.getReturnType();

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

        builder.append( name ).append( "(" );
        if ( ! inputParameterList.isEmpty() ) {
            builder.append( "\n" );
            if ( checkToNeedOwnInClass( operationSettings, functionSettings ) ) {
                insertTabs( builder, 2 ).append( Utils.convertNameForClassNaming( name ) ).append( "In request\n" );
            } else {
                iterateForParameterList( builder, inputParameterList, 2, new FunctionalObjectWithoutFilter() {
                    @Override
                    public void writeString( StringBuilder builder, Parameter p ) {
                        if ( operationSettings.getType() == Type.DEPO && methodType == MethodType.MAPPER ) {
                            builder.append( "@Param(\"" ).append( p.getName() ).append( "\") " );
                        }
                        builder.append( p.getType() ).append( " " )
                                .append( operationSettings.getType() == Type.DEPO ? p.getName() : "request" );
                    }
                } );
            }
            insertTabs( builder, 1 );
        }
        builder.append( ")" );

        return builder.toString();
    }
}
