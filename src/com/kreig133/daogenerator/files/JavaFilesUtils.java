package com.kreig133.daogenerator.files;

import com.kreig133.daogenerator.common.Settings;

import java.io.File;

/**
 * @author eshangareev
 * @version 1.0
 */
public class JavaFilesUtils {

    public static final String MAPPER_PREFIX = "Mapper";
    public static final String JAVA_EXTENSION = ".java";

    public static void insertPackageLine( String packageName, StringBuilder builder ) {
        builder.append( "package " ).append( packageName ).append( ";\n\n" );
    }

    public static File mappingFile( Settings settings ) {
        switch ( settings.getType() ){
            case IASK:
                return new File( settings.getOutputPath() + "/" + settings.getOperationName() +
                        ".map.xml" );
            case DEPO:
                return new File( settings.getOutputPath() + "/" + mapperFileName( settings ) + JAVA_EXTENSION );
        }
        throw new IllegalArgumentException();
    }

    public static String mapperFileName( Settings settings ) {
        return settings.getOperationName() + MAPPER_PREFIX;
    }

    public static File interfaceFile( Settings settings ) {
        return new File( settings.getOutputPath() + "/" + interfaceFileName( settings ) + JAVA_EXTENSION );
    }

    public static String interfaceFileName( Settings settings ) {
        return settings.getOperationName() + "Dao";
    }

    public static File implementationFile( Settings settings ) {
        return new File( settings.getOutputPath() + "/" + implementationFileName( settings ) + JAVA_EXTENSION );
    }

    public static String implementationFileName( Settings settings ) {
        return settings.getOperationName() + "DaoImpl";
    }
}
