package com.kreig133.daogenerator.files;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.enums.ClassType;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class JavaFilesUtils {

    public static final String MAPPER_PREFIX = "Mapper";
    public static final String JAVA_EXTENSION = ".java";



    public static void insertPackageLine( StringBuilder builder, String packageName ) {
        builder.append( "package " ).append( packageName ).append( ";\n\n" );
    }

    public static File mappingFile( OperationSettings operationSettings ) throws IOException {
        File file = null;
        String path = operationSettings.getOutputPath() + "/" +
                replacePointBySlash( operationSettings.getMapperPackage() ) + "/";

        switch ( operationSettings.getType() ){
            case IASK:
                file = new File( path + operationSettings.getOperationName() + ".map.xml" );
                break;
            case DEPO:
                file = new File( path + mapperFileName( operationSettings ) + JAVA_EXTENSION );
                break;
        }

        if( file == null ) throw new IllegalArgumentException();

        createDirsAndFile( file.getParentFile() );

        return file;
    }

    public static String mapperFileName( OperationSettings operationSettings ) {
        return operationSettings.getOperationName() + MAPPER_PREFIX;
    }

    public static File interfaceFile( OperationSettings operationSettings ) throws IOException {
        return daoFile( operationSettings, interfaceFileName( operationSettings ) );
    }

    public static File implementationFile( OperationSettings operationSettings ) throws IOException {
        return daoFile( operationSettings, implementationFileName( operationSettings ) );
    }
    public static File daoFile( OperationSettings operationSettings, String fileName ) throws IOException {
        File file = new File(
                operationSettings.getOutputPath() + "/" +
                replacePointBySlash( operationSettings.getDaoPackage() ) + "/" +
                fileName + JAVA_EXTENSION );

        createDirsAndFile( file.getParentFile() );

        return file;
    }

    public static String interfaceFileName( OperationSettings operationSettings ) {
        return operationSettings.getOperationName() + "Dao";
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

    public static void insertImport( StringBuilder builder, String path ){
        builder.append( "import " ).append( path ).append( ";\n" );
    }

    public static void insertClassDeclaration(
            ClassType classType,
            StringBuilder builder,
            String name,
            String parentClassName,
            List< String > interfaces
    ){
        builder.append( "public " ).append( classType ).append( " " ).append( name );
        if( ! ( parentClassName == null || "".equals( parentClassName.trim() ) ) ){
            builder.append( " extends " ).append( parentClassName );
        }

        if( interfaces!= null && !interfaces.isEmpty() ){
            builder.append( " implements " );
            for( int i = 0; i < interfaces.size(); i++ ){
                if( i > 0 ){
                    builder.append( "," );
                }
                builder.append( interfaces.get( i ) );
                builder.append( " " );
            }
        }
        builder.append( "{\n\n" );
    }

    private static void createDirsAndFile( File file ) {
        if(!file.exists()){
            file.mkdirs();
        }
    }
}
