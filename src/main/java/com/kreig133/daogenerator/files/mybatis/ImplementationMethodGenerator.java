package com.kreig133.daogenerator.files.mybatis;

import com.kreig133.daogenerator.DaoGenerator;
import com.kreig133.daogenerator.enums.MethodType;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.files.JavaFilesUtils;
import com.kreig133.daogenerator.jaxb.DaoMethod;

import static com.kreig133.daogenerator.common.StringBuilderUtils.insertTabs;

/**
 * @author eshangareev
 * @version 1.0
 */
public class ImplementationMethodGenerator {

    public static String generateMethodImpl(
            DaoMethod daoMethod
    ){

        StringBuilder builder = new StringBuilder();

        insertTabs( builder, 1 ).append( "@Override\n    public " );
        builder.append( InterfaceMethodGenerator.generateMethodSignature(
                daoMethod,
                MethodType.DAO ) );
        builder.append( "{\n" );
        insertTabs( builder, 2 );

        if( ! daoMethod.getOutputParametrs().getParameter().isEmpty() ){
            builder.append( "return " );
        }

        if( DaoGenerator.getCurrentOperationSettings().getType() == Type.DEPO ){
            generateDepoStyleMethodCall( daoMethod, builder );
        } else {
            generateIaskStyleMethodCall( daoMethod, builder );
        }


        builder.append( ");\n" );
        insertTabs(builder, 1 ).append( "}\n\n" );

        return builder.toString();
    }

    private static void generateIaskStyleMethodCall(
            DaoMethod  daoMethod,
            StringBuilder     builder
    ) {
        builder.append( "select" );
        if ( daoMethod.getCommon().getConfiguration().isMultipleResult() ) {
            builder.append( "List" );
        } else {
            builder.append( "One" );
        }

        builder.append( "(\"" ).append( DaoGenerator.getCurrentOperationSettings().getDaoPackage() ).append( "." )
                .append( JavaFilesUtils.interfaceFileName() ).append( "." )
                .append( daoMethod.getCommon().getMethodName() ).append( "\" ").append( "," );
        if( ! daoMethod.getInputParametrs().getParameter().isEmpty() ){
            builder.append( "request" );
        } else {
            builder.append( "null" );
        }
    }

    private static void generateDepoStyleMethodCall( DaoMethod daoMethod, StringBuilder builder ) {
        builder.append( "getSqlSession().getMapper( " ).append(
                DaoGenerator.getCurrentOperationSettings().getOperationName()
        ).append( JavaFilesUtils.MAPPER_PREFIX ).append( ".class )." ).append( daoMethod.getCommon().getMethodName() )
                .append( "(" );

        if ( ! daoMethod.getInputParametrs().getParameter().isEmpty() ){
            builder.append( "request" );
        }
    }
}
