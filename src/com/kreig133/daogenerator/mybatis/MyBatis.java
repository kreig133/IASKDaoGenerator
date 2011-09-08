package com.kreig133.daogenerator.mybatis;

import com.kreig133.daogenerator.common.Settings;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.enums.MethodType;

import java.io.File;
import java.io.IOException;

/**
 * @author eshangareev
 * @version 1.0
 */
public class MyBatis {

    public static final String MAPPER_PREFIX = "Mapper";
    public static final String JAVA_EXTENSION = ".java";

    public static void generateFiles(
            Settings settings
    ) throws IOException {

        generateMapping         ( settings );
        generateInterface       ( settings );
        generateImplementation  ( settings );
    }

    private static void generateMapping(
        Settings settings
    ) throws IOException {
        String method;

        switch ( settings.getType() ){
            case IASK:
                Utils.appendByteToFile( new File( settings.getOutputPath() + "+/" + settings.getOperationName() +
                        JAVA_EXTENSION ),
                        XmlMappingGenerator.generateXmlMapping( settings ).getBytes() );
                break;
            case DEPO:
                method = AnnotationGenerator.generateAnnotation( settings )
                        + InterfaceMethodGenerator.methodGenerator( settings, MethodType.MAPPER  ) + "\n";
                Utils.appendByteToFile( new File( settings.getOutputPath() + "/" + settings.getOperationName() +
                        MAPPER_PREFIX + JAVA_EXTENSION ),
                        method.getBytes() );
                break;
        }
    }

    private static void generateInterface(
            Settings settings
    ) throws IOException {

        Utils.appendByteToFile(
                new File( settings.getOutputPath() + settings.getOperationName() + "Dao" + JAVA_EXTENSION ),
                InterfaceMethodGenerator.methodGenerator( settings, MethodType.DAO ).getBytes()
        );
    }

    private static void generateImplementation(
            Settings settings
    ) throws IOException {
        
        Utils.appendByteToFile(
                new File( settings.getOutputPath() + settings.getOperationName() + "DaoImpl" + JAVA_EXTENSION ),
                ImplementationMethodGenerator.generateMethodImpl( settings ).getBytes()
        );
    }
}
