package com.kreig133.daogenerator.files;

import com.kreig133.daogenerator.DaoGenerator;
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

    public static File mappingFile() throws IOException {
        File file = null;
        String path = DaoGenerator.getCurrentOperationSettings().getOutputPath() + "/" +
                replacePointBySlash( DaoGenerator.getCurrentOperationSettings().getMapperPackage() ) + "/";

        switch ( DaoGenerator.getCurrentOperationSettings().getType() ){
            case IASK:
                file = new File( path + DaoGenerator.getCurrentOperationSettings().getOperationName() + ".map.xml" );
                break;
            case DEPO:
                file = new File( path + mapperFileName() + JAVA_EXTENSION );
                break;
        }

        createDirsAndFile( file.getParentFile() );

        return file;
    }

    public static String mapperFileName() {
        return DaoGenerator.getCurrentOperationSettings().getOperationName() + MAPPER_PREFIX;
    }

    public static File interfaceFile() throws IOException {
        return daoFile( interfaceFileName() );
    }

    public static File implementationFile() throws IOException {
        return daoFile( implementationFileName( ) );
    }
    public static File daoFile( String fileName ) throws IOException {
        File file = new File(
                DaoGenerator.getCurrentOperationSettings().getOutputPath() + "/" +
                replacePointBySlash( DaoGenerator.getCurrentOperationSettings().getDaoPackage() ) + "/" +
                fileName + JAVA_EXTENSION );

        createDirsAndFile( file.getParentFile() );

        return file;
    }

    public static String interfaceFileName() {
        return DaoGenerator.getCurrentOperationSettings().getOperationName() + "Dao";
    }



    public static String implementationFileName() {
        return DaoGenerator.getCurrentOperationSettings().getOperationName() + "DaoImpl";
    }

    public static File getInOrOutClassFile(
            InOutClass inOutClass
    ) throws IOException {
        File file = new File(
                DaoGenerator.getCurrentOperationSettings().getOutputPath() +
                "/" +
                replacePointBySlash( DaoGenerator.getCurrentOperationSettings().getEntityPackage() ) +
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
