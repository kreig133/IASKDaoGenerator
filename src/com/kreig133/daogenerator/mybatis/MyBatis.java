package com.kreig133.daogenerator.mybatis;

import com.kreig133.daogenerator.Settings;
import com.kreig133.daogenerator.Utils;
import com.kreig133.daogenerator.enums.SelectType;
import com.kreig133.daogenerator.enums.Type;

import java.io.File;
import java.io.IOException;
import java.text.Annotation;

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
    }

    public static void generateMapping(
        Settings settings
    ) throws IOException {
        String method = "";

        switch ( settings.getType() ){
            case IASK:
                Utils.appandByteToFile( new File( settings.getOutputPath() + "mapper.java" ),
                        XmlMappingGenerator.generateXmlMapping( settings ).getBytes() );
                break;
            case DEPO:
                method = AnnotationGenerator.generateAnnotation( settings )
                        + InterfaceMethodGenerator.methodGenerator( settings ) + "\n";
                Utils.appandByteToFile( new File( settings.getOutputPath() + "mapper.java" ),
                        method.getBytes() );
                break;
        }
    }

    public static void generateInterface(
            Settings settings
    ) throws IOException {

        Utils.appandByteToFile(
                new File( settings.getOutputPath() + "interface.java" ),
                InterfaceMethodGenerator.methodGenerator( settings ).getBytes()
        );
    }

    public static void generateImplementation( Settings settings ){
        //TODO Доделать
    }
}
