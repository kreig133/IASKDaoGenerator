package com.kreig133.daogenerator.mybatis;

import com.kreig133.daogenerator.common.Settings;
import com.kreig133.daogenerator.enums.MethodType;

/**
 * @author eshangareev
 * @version 1.0
 */
public class ImplementationMethodGenerator {

    public static String generateMethodImpl( Settings settings ){

        StringBuilder builder = new StringBuilder();

        builder.append( "    @Override\n    public " );
        builder.append( InterfaceMethodGenerator.generateMethodSignature( settings, MethodType.DAO ) );
        builder.append( "{\n" );
        builder.append( "        " );

        if( ! settings.getOutputParameterList().isEmpty() ){
            builder.append( "return " );
        }

        builder.append( "getSqlSession().getMapper( " ).append( settings.getOperationName() );
        builder.append( MyBatis.MAPPER_PREFIX ).append( ".class )." );
        builder.append( settings.getFunctionName() ).append( "(" );

        if( ! settings.getInputParameterList().isEmpty() ){
            builder.append( "request" );
        }

        builder.append( ");\n" );
        builder.append( "    }\n\n" );

        return builder.toString();
    }
}
