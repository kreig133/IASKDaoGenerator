package com.kreig133.daogenerator.files.mybatis;

import com.kreig133.daogenerator.DaoGenerator;
import com.kreig133.daogenerator.common.StringBuilderUtils;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.enums.MethodType;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.files.mybatis.preparatory.ImplementationFilePreparatory;
import com.kreig133.daogenerator.files.mybatis.preparatory.InterfaceFilePreparatory;
import com.kreig133.daogenerator.files.mybatis.preparatory.MappingFilePreparatory;
import com.kreig133.daogenerator.jaxb.DaoMethod;

import java.io.IOException;

import static com.kreig133.daogenerator.files.JavaFilesUtils.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class MyBatis {

    public static void prepareFiles(
            OperationSettings operationSettings
    ) throws IOException {

        InterfaceFilePreparatory        .prepareFile();
        ImplementationFilePreparatory   .prepareFile();
        MappingFilePreparatory          .prepareFile();

    }

    public static void generateFiles(
            DaoMethod daoMethod
    ) throws IOException {

        generateMapping         ( daoMethod );

        if( DaoGenerator.getCurrentOperationSettings().getType() == Type.IASK ){
            generateInterface       ( daoMethod );
            generateImplementation  ( daoMethod );
        }

    }

    public static void closeFiles() throws IOException {
        String s = "\n}";
        if( DaoGenerator.getCurrentOperationSettings().getType() == Type.IASK ){
            Utils.appendByteToFile( interfaceFile       (), s.getBytes() );
            Utils.appendByteToFile( implementationFile  (), s.getBytes() );
            Utils.appendByteToFile( mappingFile         (), "</mapper>".getBytes() );
        } else {
            Utils.appendByteToFile( mappingFile         (), s.getBytes() );
        }
    }

    private static void generateMapping(
        DaoMethod daoMethod
    ) throws IOException {
        String method;

        switch ( DaoGenerator.getCurrentOperationSettings().getType() ){
            case IASK:
                Utils.appendByteToFile( mappingFile(),
                        XmlMappingGenerator.generateXmlMapping( daoMethod ).getBytes() );
                break;
            case DEPO:
                method =
                        AnnotationGenerator.generateAnnotation( daoMethod )
                        +"    public "
                        + InterfaceMethodGenerator.generateMethodSignature(
                                daoMethod,
                                MethodType.MAPPER  )
                        + ";\n";
                Utils.appendByteToFile( mappingFile(),
                        method.getBytes() );
                break;
        }
    }

    private static void generateInterface(
            DaoMethod daoMethod
    ) throws IOException {

        StringBuilder builder = new StringBuilder();

        Utils.appendByteToFile(
                interfaceFile(),
                StringBuilderUtils.getJavaDocString(
                        builder,
                        daoMethod.getCommon().getComment().split( "\n" )
                ).append(
                        InterfaceMethodGenerator.methodGenerator( daoMethod )
                ).toString().getBytes()
        );
    }

    private static void generateImplementation(
            DaoMethod daoMethod
    ) throws IOException {

        Utils.appendByteToFile(
                implementationFile(),
                ImplementationMethodGenerator.generateMethodImpl( daoMethod ).getBytes()
        );
    }
}
