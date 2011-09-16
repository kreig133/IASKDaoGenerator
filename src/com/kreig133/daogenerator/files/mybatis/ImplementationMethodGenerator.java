package com.kreig133.daogenerator.files.mybatis;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.enums.MethodType;
import com.kreig133.daogenerator.enums.ReturnType;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.files.JavaFilesUtils;

/**
 * @author eshangareev
 * @version 1.0
 */
public class ImplementationMethodGenerator {

    public static String generateMethodImpl(
            OperationSettings operationSettings,
            FunctionSettings functionSettings
    ){

        StringBuilder builder = new StringBuilder();

        builder.append( "    @Override\n    public " );
        builder.append( InterfaceMethodGenerator.generateMethodSignature(
                operationSettings,
                functionSettings,
                MethodType.DAO ) );
        builder.append( "{\n" );
        builder.append( "        " );

        if( ! functionSettings.getOutputParameterList().isEmpty() ){
            builder.append( "return " );
        }

        if( operationSettings.getType() == Type.DEPO ){
            generateDepoStyleMethodCall( operationSettings, functionSettings, builder );
        } else {
            generateIaskStyleMethodCall( operationSettings, functionSettings, builder );
        }


        builder.append( ");\n" );
        builder.append( "    }\n\n" );

        return builder.toString();
    }

    private static void generateIaskStyleMethodCall( OperationSettings operationSettings, FunctionSettings functionSettings, StringBuilder builder ) {
        builder.append( "select" );
        if( functionSettings.getReturnType() == ReturnType.SINGLE ){
            builder.append( "One" );
        } else {
            builder.append( "List" );
        }
        builder.append( "(\"" ).append( operationSettings.getDaoPackage() ).append( "." )
                .append( functionSettings.getName() ).append( "\" ").append( "," );
        if( ! functionSettings.getInputParameterList().isEmpty() ){
            builder.append( "request" );
        } else {
            builder.append( "null" );
        }
    }

    private static void generateDepoStyleMethodCall( OperationSettings operationSettings, FunctionSettings functionSettings, StringBuilder builder ) {
        builder.append( "getSqlSession().getMapper( " ).append( operationSettings.getOperationName() );
        builder.append( JavaFilesUtils.MAPPER_PREFIX ).append( ".class )." );
        builder.append( functionSettings.getName() ).append( "(" );
        if( ! functionSettings.getInputParameterList().isEmpty() ){
            builder.append( "request" );
        }
    }
}
