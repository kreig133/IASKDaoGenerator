package com.kreig133.daogenerator.mybatis;

import com.kreig133.daogenerator.common.Settings;

/**
 * @author eshangareev
 * @version 1.0
 */
public class ImplementationMethodGenerator {

    public static String generateMethodImpl( Settings settings ){

        StringBuilder result = new StringBuilder();

        result.append( "    @Override\n    public " );
        result.append( InterfaceMethodGenerator.generateMethodSignature( settings ) );
        result.append( "{\n" );
        result.append( "        " );
        if( ! settings.getOutputParameterList().isEmpty() ){
            result.append( "return " );
        }
        result.append( "getSqlSession().getMapper( " );
        result.append( settings.getOperationName() );
        result.append( MyBatis.MAPPER_PREFIX );
        result.append( ".class )." );
        result.append( settings.getFunctionName() );
        result.append( "(" );
        if( ! settings.getInputParameterList().isEmpty() ){
            result.append( "request" );
        }
        result.append( ");\n" );
        result.append( "    }\n\n" );

        return result.toString();
    }
}
