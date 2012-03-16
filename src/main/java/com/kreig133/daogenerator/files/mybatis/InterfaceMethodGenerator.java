package com.kreig133.daogenerator.files.mybatis;

import com.kreig133.daogenerator.DaoGenerator;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.common.strategy.FunctionalObjectWithoutFilter;
import com.kreig133.daogenerator.enums.MethodType;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;

import java.util.List;

import static com.kreig133.daogenerator.common.Utils.*;
import static com.kreig133.daogenerator.common.StringBuilderUtils.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class InterfaceMethodGenerator {

    public static String methodGenerator(
            final DaoMethod daoMethod
    ) {
        return  addTabsBeforeLine( generateMethodSignature( daoMethod, MethodType.DAO ) + ";\n", 1 );
    }

    public static String generateMethodSignature(
            final DaoMethod daoMethod,
            final MethodType methodType
    ) {

        final List<ParameterType>  inputParameterList = daoMethod.getInputParametrs().getParameter();
        final List<ParameterType> outputParameterList = daoMethod.getOutputParametrs().getParameter();
        final String     name       = daoMethod.getCommon().getMethodName();

        StringBuilder builder = new StringBuilder();

        if ( outputParameterList.isEmpty() ) {
            builder.append( "void " );
        } else {
            if ( daoMethod.getCommon().getConfiguration().isMultipleResult() ) {
                builder.append( "List<" );
            }
            if ( outputParameterList.size() == 1 ) {
                builder.append( outputParameterList.get( 0 ).getType().value() );
            } else {
                builder.append( Utils.convertNameForClassNaming( name ) ).append( "Out" );
            }
            if ( daoMethod.getCommon().getConfiguration().isMultipleResult() ) {
                builder.append( ">" );
            }
            builder.append( " " );
        }

        builder.append( name ).append( "(" );
        if ( ! inputParameterList.isEmpty() ) {
            builder.append( "\n" );
            if ( checkToNeedOwnInClass( daoMethod ) ) {
                insertTabs( builder, 2 ).append( Utils.convertNameForClassNaming( name ) ).append( "In request\n" );
            } else {
                iterateForParameterList( builder, inputParameterList, 2, new FunctionalObjectWithoutFilter() {
                    @Override
                    public void writeString( StringBuilder builder, ParameterType p ) {
                        if (
                                DaoGenerator.getCurrentOperationSettings().getType()
                                ==
                                Type.DEPO && methodType == MethodType.MAPPER
                        ) {
                            builder.append( "@Param(\"" ).append( p.getName() ).append( "\") " );
                        }
                        builder.append( p.getType().value() ).append( " " )
                            .append(
                                    DaoGenerator.getCurrentOperationSettings().getType() == Type.DEPO ?
                                            p.getName()
                                            : "request"
                            );
                    }
                } );
            }
            insertTabs( builder, 1 );
        }
        builder.append( ")" );

        return builder.toString();
    }
}
