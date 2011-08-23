package com.kreig133.daogenerator.mybatis;

import com.kreig133.daogenerator.Settings;
import com.kreig133.daogenerator.Utils;

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

    public static void generateMapping(
        Settings settings
    ) throws IOException {
        String method = "";

        switch ( settings.getType() ){
            case IASK:
                Utils.appandByteToFile( new File( settings.getOutputPath() + "+/"+settings.getOperationName()+
                        JAVA_EXTENSION ),
                        XmlMappingGenerator.generateXmlMapping( settings ).getBytes() );
                break;
            case DEPO:
                method = AnnotationGenerator.generateAnnotation( settings )
                        + InterfaceMethodGenerator.methodGenerator( settings ) + "\n";
                Utils.appandByteToFile( new File( settings.getOutputPath() + "/"+settings.getOperationName()+
                        MAPPER_PREFIX + JAVA_EXTENSION ),
                        method.getBytes() );
                break;
        }
    }

    public static void generateInterface(
            Settings settings
    ) throws IOException {

        Utils.appandByteToFile(
                new File( settings.getOutputPath() + settings.getOperationName() +"Dao" + JAVA_EXTENSION ),
                InterfaceMethodGenerator.methodGenerator( settings ).getBytes()
        );
    }

    public static void generateImplementation(
            Settings settings
    ) throws IOException {
        
        Utils.appandByteToFile(
                new File( settings.getOutputPath() + settings.getOperationName() + "DaoImpl" + JAVA_EXTENSION ),
                ImplementationMethodGenerator.generateMethodImpl( settings ).getBytes()
        );
    }
}
