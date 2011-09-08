package com.kreig133.daogenerator.mybatis;

import com.kreig133.daogenerator.DaoGenerator;
import com.kreig133.daogenerator.common.Settings;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.enums.MethodType;
import com.kreig133.daogenerator.enums.Type;

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

    private static File mappingFile( Settings settings ) {
        switch ( settings.getType() ){
            case IASK:
                return new File( settings.getOutputPath() + "+/" + settings.getOperationName() +
                        ".map.xml" );
            case DEPO:
                return new File( settings.getOutputPath() + "/" + mapperFileName( settings ) + JAVA_EXTENSION );
        }
        throw new IllegalArgumentException();
    }

    private static String mapperFileName( Settings settings ) {
        return settings.getOperationName() + MAPPER_PREFIX;
    }

    private static void generateInterface(
            Settings settings
    ) throws IOException {

        Utils.appendByteToFile(
                interfaceFile( settings ) ,
                InterfaceMethodGenerator.methodGenerator( settings, MethodType.DAO ).getBytes()
        );
    }

    private static File interfaceFile( Settings settings ) {
        return new File( settings.getOutputPath() + interfaceFileName( settings ) + JAVA_EXTENSION );
    }

    private static String interfaceFileName( Settings settings ) {
        return settings.getOperationName() + "Dao";
    }

    private static void generateImplementation(
            Settings settings
    ) throws IOException {
        
        Utils.appendByteToFile(
                implementationFile( settings ),
                ImplementationMethodGenerator.generateMethodImpl( settings ).getBytes()
        );
    }

    private static File implementationFile( Settings settings ) {
        return new File( settings.getOutputPath() + implementationFileName( settings ) + JAVA_EXTENSION );
    }

    private static String implementationFileName( Settings settings ) {
        return settings.getOperationName() + "DaoImpl";
    }


    public static void prepareFiles(
            Settings settings
    ) throws IOException {

        prepareInterfaceFile        ( settings );
        prepareImplementationFile   ( settings );
        prepareMappingFile          ( settings );

    }

    private static void prepareMappingFile( Settings settings ) throws IOException {
        StringBuilder builder = new StringBuilder();

        if( settings.getType() == Type.DEPO ){
            builder.append( "package " ).append( settings.getMapperPackage() ).append( ";\n\n" );

            commonImports( settings, builder );

            builder.append( "import org.apache.ibatis.annotations.*;\n\n" );
            //TODO блок комментариев
            builder.append( "public interface " ).append( mapperFileName( settings ) ).append( "{\n\n" );
        } else {
            throw new RuntimeException( "Запили для ИАСКА. Быстро! " );
        }
        Utils.appendByteToFile( mappingFile( settings ), builder.toString().getBytes() );
    }


    private static void prepareImplementationFile( Settings settings ) throws IOException {
        StringBuilder builder = new StringBuilder();

        if( settings.getType() == Type.DEPO ){
            startingLinesOfDaoFiles( settings, builder );
            builder.append( "import " ).append( mapperFileName( settings ) ).append( ".*;\n" );
            builder.append( "import org.mybatis.spring.support.SqlSessionDaoSupport;\n" );
            builder.append( "import org.springframework.stereotype.Repository;\n\n" );

            //TODO блок комментариев
            builder.append( "@Repository\n" );
            builder.append( "public class " ).append( implementationFileName( settings ) ).
                    append( "extends SqlSessionDaoSupport implements CloseDepoAccountDao {\n\n" );

            Utils.appendByteToFile( implementationFile( settings ), builder.toString().getBytes() );
        } else {
            throw new RuntimeException( "Запили для ИАСКА. Быстро! " );
        }
    }

    private static void prepareInterfaceFile( Settings settings ) throws IOException {
        StringBuilder builder = new StringBuilder();

        startingLinesOfDaoFiles( settings, builder );
        //TODO блок комментариев
        builder.append( "public interface ").append( interfaceFileName( settings ) ).append( "{\n\n" );

        Utils.appendByteToFile( interfaceFile( settings ), builder.toString().getBytes() );
    }

    private static void startingLinesOfDaoFiles( Settings settings, StringBuilder builder ) {
        builder.append( "package " ).append( settings.getDaoPackage() ).append( ";\n\n" );
        commonImports( settings, builder );
    }

    private static void commonImports( Settings settings, StringBuilder builder ) {
        builder.append( "import " ).append( settings.getEntityPackage() ).append( ".*;\n\n" );
        builder.append( "import java.util.List;\n\n" );
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
}
