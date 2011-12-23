package com.kreig133.daogenerator.files.mybatis;

import com.kreig133.daogenerator.common.StringBuilderUtils;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.enums.MethodType;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.files.mybatis.preparatory.ImplementationFilePreparatory;
import com.kreig133.daogenerator.files.mybatis.preparatory.InterfaceFilePreparatory;
import com.kreig133.daogenerator.files.mybatis.preparatory.MappingFilePreparatory;

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

        InterfaceFilePreparatory        .prepareFile( operationSettings );
        ImplementationFilePreparatory   .prepareFile( operationSettings );
        MappingFilePreparatory          .prepareFile( operationSettings );

    }

    public static void generateFiles(
            OperationSettings operationSettingsSettings,
            FunctionSettings functionSettings
    ) throws IOException {

        generateMapping         ( operationSettingsSettings, functionSettings );
        generateInterface       ( operationSettingsSettings, functionSettings );
        generateImplementation  ( operationSettingsSettings, functionSettings );

    }

    public static void closeFiles(
            OperationSettings operationSettings
    ) throws IOException {
        String s = "\n}";
        if( operationSettings.getType() == Type.IASK ){
            Utils.appendByteToFile( interfaceFile       ( operationSettings ), s.getBytes() );
            Utils.appendByteToFile( implementationFile  ( operationSettings ), s.getBytes() );
            Utils.appendByteToFile( mappingFile         ( operationSettings ), "</mapper>".getBytes() );
        } else {
            Utils.appendByteToFile( mappingFile         ( operationSettings ), s.getBytes() );
        }
    }

    private static void generateMapping(
        OperationSettings operationSettings,
        FunctionSettings functionSettings
    ) throws IOException {
        String method;

        switch (  operationSettings.getType() ){
            case IASK:
                Utils.appendByteToFile( mappingFile( operationSettings ),
                        XmlMappingGenerator.generateXmlMapping( operationSettings, functionSettings ).getBytes() );
                break;
            case DEPO:
                method =
                        AnnotationGenerator.generateAnnotation( functionSettings )
                        +"    public "
                        + InterfaceMethodGenerator.generateMethodSignature(
                                operationSettings,
                                functionSettings,
                                MethodType.MAPPER  )
                        + "\n";
                Utils.appendByteToFile( mappingFile( operationSettings ),
                        method.getBytes() );
                break;
        }
    }

    private static void generateInterface(
            OperationSettings operationSettings,
            FunctionSettings functionSettings
    ) throws IOException {
        if( operationSettings.getType() == Type.IASK ){
            StringBuilder builder = new StringBuilder();
    
            Utils.appendByteToFile(
                    interfaceFile( operationSettings ),
                    StringBuilderUtils.getJavaDocString(
                            builder,
                            functionSettings.getCommentBuilder().toString().split( "\n" )
                    ).append(
                            InterfaceMethodGenerator.methodGenerator( operationSettings, functionSettings )
                    ).toString().getBytes()
            );
        }
    }

    private static void generateImplementation(
            OperationSettings operationSettings,
            FunctionSettings functionSettings
    ) throws IOException {

        if( operationSettings.getType() == Type.IASK ){
            Utils.appendByteToFile(
                    implementationFile( operationSettings ),
                    ImplementationMethodGenerator.generateMethodImpl( operationSettings, functionSettings ).getBytes()
            );
        }
    }
}
