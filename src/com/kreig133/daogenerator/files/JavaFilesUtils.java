package com.kreig133.daogenerator.files;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.settings.OperationSettings;

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

    public static File mappingFile( OperationSettings operationSettings ) throws IOException {
        File file = null;

        switch ( operationSettings.getType() ){
            case IASK:
                file = new File(
                        operationSettings.getOutputPath() +
                        "/" +
                        replacePointBySlash( operationSettings.getMapperPackage() )+
                        "/" +
                        operationSettings.getOperationName() +
                        ".map.xml" );
                break;
            case DEPO:
                file = new File(
                        operationSettings.getOutputPath() +
                        "/" +
                        replacePointBySlash( operationSettings.getMapperPackage() ) +
                        "/" +
                        mapperFileName( operationSettings ) + JAVA_EXTENSION );
                break;
        }

        createDirsAndFile( file.getParentFile() );

        if( file == null ) throw new IllegalArgumentException();

        return file;
    }

    public static String mapperFileName( OperationSettings operationSettings ) {
        return operationSettings.getOperationName() + MAPPER_PREFIX;
    }

    public static File interfaceFile( OperationSettings operationSettings ) throws IOException {
        File file = new File(
                operationSettings.getOutputPath() +
                "/" +
                replacePointBySlash( operationSettings.getDaoPackage() ) +
                "/" +
                interfaceFileName( operationSettings ) + JAVA_EXTENSION );

        createDirsAndFile( file.getParentFile() );

        return file;
    }

    public static String interfaceFileName( OperationSettings operationSettings ) {
        return operationSettings.getOperationName() + "Dao";
    }

    public static File implementationFile( OperationSettings operationSettings ) throws IOException {
        File file = new File(
                operationSettings.getOutputPath() +
                "/" +
                replacePointBySlash( operationSettings.getDaoPackage() ) +
                "/" +
                implementationFileName( operationSettings ) + JAVA_EXTENSION );

        createDirsAndFile( file.getParentFile() );

        return file;
    }

    public static String implementationFileName( OperationSettings operationSettings ) {
        return operationSettings.getOperationName() + "DaoImpl";
    }

    public static File getInOrOutClassFile(
            OperationSettings operationSettings,
            InOutClass inOutClass
    ) throws IOException {
        File file = new File(
                operationSettings.getOutputPath   () +
                "/" +
                replacePointBySlash( operationSettings.getEntityPackage() ) +
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
