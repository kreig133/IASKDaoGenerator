package com.kreig133.daogenerator.files.mybatis;

import com.kreig133.daogenerator.common.Settings;
import com.kreig133.daogenerator.common.Utils;
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

    public static void generateFiles(
            Settings settings
    ) throws IOException {

        generateMapping         ( settings );
        generateInterface       ( settings );
        generateImplementation  ( settings );

        settings.clearSelectQuery();
        settings.getInputParameterList().clear();
        settings.getOutputParameterList().clear();
    }

    public static void prepareFiles(
            Settings settings
    ) throws IOException {

        InterfaceFilePreparatory        .prepareFile( settings );
        ImplementationFilePreparatory   .prepareFile( settings );
        MappingFilePreparatory          .prepareFile( settings );

    }

    public static void closeFiles( Settings settings ) throws IOException {
        String s = "\n}";
        Utils.appendByteToFile( interfaceFile       ( settings ), s.getBytes() );
        Utils.appendByteToFile( implementationFile  ( settings ), s.getBytes() );

        if( settings.getType() == Type.DEPO ){
            Utils.appendByteToFile( mappingFile ( settings ), s.getBytes() );
        } else {
            throw new RuntimeException( "Запили для ИАСКА. Быстро!" );
        }
    }

    private static void generateMapping(
        Settings settings
    ) throws IOException {
        String method;

        switch ( settings.getType() ){
            case IASK:
                Utils.appendByteToFile( mappingFile( settings ),
                        XmlMappingGenerator.generateXmlMapping( settings ).getBytes() );
                break;
            case DEPO:
                method =
                        AnnotationGenerator.generateAnnotation( settings )
                        +"    public "
                        + InterfaceMethodGenerator.generateMethodSignature( settings, MethodType.MAPPER  )
                        + "\n";
                Utils.appendByteToFile( mappingFile( settings ),
                        method.getBytes() );
                break;
        }
    }

    private static void generateInterface(
            Settings settings
    ) throws IOException {

        Utils.appendByteToFile(
                interfaceFile( settings ) ,
                InterfaceMethodGenerator.methodGenerator( settings ).getBytes()
        );
    }

    private static void generateImplementation(
            Settings settings
    ) throws IOException {

        Utils.appendByteToFile(
                implementationFile( settings ),
                ImplementationMethodGenerator.generateMethodImpl( settings ).getBytes()
        );
    }
}
