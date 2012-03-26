package com.kreig133.daogenerator;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.files.JavaClassGenerator;
import com.kreig133.daogenerator.files.mybatis.mapping.MappingGenerator;
import com.kreig133.daogenerator.settings.Settings;

import java.io.*;

/**
 * @author kreig133
 * @version 1.0
 */
public class MavenProjectGenerator {

    public static void generate() throws IOException {
        copyPropertiesFileToMavenProject();
        copyPomFileToMavenProject();
        copyAppContextConfigToMavenProject();
        copyAbstractTester();
        copyBaseModel();
        generateSpringConfig();
    }

    private static void copyBaseModel() throws IOException {
        copyFile(
                MavenProjectGenerator.class.getClassLoader().getResourceAsStream( "DepoModelData.txt" ),
                new File( Settings.settings().getPathForGeneratedSource()
                        + "/com/aplana/sbrf/deposit/web/common/client/operation/data/DepoModelData.java" )
        );
    }

    protected static void generateSpringConfig() throws IOException {
        InputStream stream = new ByteArrayInputStream( fillContextTemplateByData( Utils.streamToString(
                MavenProjectGenerator.class.getClassLoader().getResourceAsStream( "mapperBeanContextTemplate.xml" )
        ) ).getBytes() );

        copyFile(
                stream,
                new File(
                        Settings.settings().getPathForTestResources() + "/" +
                        JavaClassGenerator.replacePointBySlash( Settings.settings().getMapperPackage() ) + "/" +
                        getConfigName() + ".xml"
                )
        );
    }

    protected static String fillContextTemplateByData( String string ) {
        return string
                .replaceFirst( "\\$\\{beanClass\\}",
                        Settings.settings().getMapperPackage() + "." +
                                MappingGenerator.instance().getFileName() )
                .replaceFirst( "\\$\\{beanName\\}",
                        JavaClassGenerator.convertNameForNonClassNaming(
                                MappingGenerator.instance().getFileName() ) );
    }

    protected static void copyAbstractTester() throws IOException {
        copyFile(
                MavenProjectGenerator.class.getClassLoader().getResourceAsStream( "AbstractDepoDaoExecuteTester.txt" ),
                new File( Settings.settings().getPathForGeneratedTests()
                        + "/com/aplana/sbrf/deposit/AbstractDepoDaoExecuteTester.java" )
        );
    }

    protected static void copyAppContextConfigToMavenProject() throws IOException {
        copyFile(
                MavenProjectGenerator.class.getClassLoader().getResourceAsStream( "testApplicationContext.xml" ),
                new File( Settings.settings().getPathForTestResources() + "/testApplicationContext.xml" )
        );
    }

    protected static void copyPomFileToMavenProject() throws IOException {
        copyFile(
                MavenProjectGenerator.class.getClassLoader().getResourceAsStream( "pom.xml" ),
                new File( Settings.settings().getOutputPathForJavaClasses() + "/pom.xml" )
        );
    }

    protected static void copyPropertiesFileToMavenProject() throws IOException {
        final InputStream resourceAsStream = MavenProjectGenerator.class.getClassLoader().
                getResourceAsStream( "depo/application.properties" );// TODO

        final File propertiesOut = new File( Settings.settings().getPathForTestResources() + "/application.properties" );
        copyFile( resourceAsStream, propertiesOut );
    }

    public static void copyFile( InputStream inputStream, File outputFile  ) throws IOException {
        outputFile.getParentFile().mkdirs();

        final BufferedInputStream bufferedInputStream = new BufferedInputStream( inputStream );
        byte[] buffer = new byte[1024*10];

        final FileOutputStream fileOutputStream = new FileOutputStream( outputFile );

        try {

            while ( true ) {
                final int read = bufferedInputStream.read( buffer );

                if( read == -1 ) break;

                fileOutputStream.write( buffer, 0, read );

                if( read < buffer.length ) break;
            }
        } finally {
            fileOutputStream.close();
            bufferedInputStream.close();
        }
    }
    
    public static String getConfigName(){
        return MappingGenerator.instance().getFileName() + "TestConfig";
    }

    public static int installProject() {
        final String[] cmdarray = { "cmd", "/C", "mvn -f "+ Settings.settings().getOutputPathForJavaClasses() +
                "\\pom.xml clean install"};

        for ( String s1 : cmdarray ) {
            System.out.println( s1 );
        }

        try {
            Process exec = Runtime.getRuntime().exec( cmdarray );
            Communicator.communicate(
                    exec,
                    new OutputStreamWriter( System.out, "Cp866" ),
                    new OutputStreamWriter( System.err, "Cp866" )
            );
            return exec.waitFor();
        } catch ( IOException e ) {
            e.printStackTrace();
            return -1;
        } catch ( InterruptedException e ) {
            e.printStackTrace();
            return -1;
        }
    }
}
