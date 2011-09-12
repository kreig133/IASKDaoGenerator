package com.kreig133.daogenerator.files;

import com.kreig133.daogenerator.common.Settings;

import java.io.File;
import java.io.IOException;

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

    public static File mappingFile( Settings settings ) throws IOException {
        File file = null;

        switch ( settings.getType() ){
            case IASK:
                file = new File( settings.getOutputPath() + "/" + settings.getOperationName() + ".map.xml" );
                break;
            case DEPO:
                file = new File(
                        settings.getOutputPath() +
                        "/" +
                        replacePointBySlash( settings.getMapperPackage() ) +
                        "/" +
                        mapperFileName( settings ) + JAVA_EXTENSION );
                break;
        }

        createDirsAndFile( file.getParentFile() );

        if( file == null ) throw new IllegalArgumentException();

        return file;
    }

    public static String mapperFileName( Settings settings ) {
        return settings.getOperationName() + MAPPER_PREFIX;
    }

    public static File interfaceFile( Settings settings ) throws IOException {
        File file = new File(
                settings.getOutputPath() +
                "/" +
                replacePointBySlash( settings.getDaoPackage() ) +
                "/" +
                interfaceFileName( settings ) + JAVA_EXTENSION );

        createDirsAndFile( file.getParentFile() );

        return file;
    }

    public static String interfaceFileName( Settings settings ) {
        return settings.getOperationName() + "Dao";
    }

    public static File implementationFile( Settings settings ) throws IOException {
        File file = new File(
                settings.getOutputPath() +
                "/" +
                replacePointBySlash( settings.getDaoPackage() ) +
                "/" +
                implementationFileName( settings ) + JAVA_EXTENSION );

        createDirsAndFile( file.getParentFile() );

        return file;
    }

    public static String implementationFileName( Settings settings ) {
        return settings.getOperationName() + "DaoImpl";
    }

    public static File getInOrOutClassFile( Settings settings, InOutClass inOutClass ) throws IOException {
        File file = new File(
                settings.getOutputPath   () +
                "/" +
                replacePointBySlash( settings.getEntityPackage() ) +
                "/" +
                inOutClass.getName() + JAVA_EXTENSION);

        createDirsAndFile( file.getParentFile() );

        return file;
    }

    private static String replacePointBySlash( String string ){
        return string.replace( '.', '/' );
    }

    private static void createDirsAndFile( File file ) throws IOException {
        if(!file.exists()){
            file.mkdirs();
        }
    }
}
