package com.kreig133.daogenerator;

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

    }

    private static void copyAbstractTester() throws IOException {
        copyFile(
                MavenProjectGenerator.class.getClassLoader().getResourceAsStream( "AbstractDepoDaoExecuteTester.java" ),
                new File( Settings.settings().getPathForGeneratedTests()
                        + "/com/aplana/sbrf/AbstractDepoDaoExecuteTester.java" )
        );
    }

    private static void copyAppContextConfigToMavenProject() throws IOException {
        copyFile(
                MavenProjectGenerator.class.getClassLoader().getResourceAsStream( "testApplicationContext.xml" ),
                new File( Settings.settings().getPathForTestResources() + "/testApplicationContext.xml" )
        );
    }

    private static void copyPomFileToMavenProject() throws IOException {
        copyFile(
                MavenProjectGenerator.class.getClassLoader().getResourceAsStream( "pom.xml" ),
                new File( Settings.settings().getOutputPathForJavaClasses() + "/pom.xml" )
        );
    }

    private static void copyPropertiesFileToMavenProject() throws IOException {
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
        return MappingGenerator.instance().getFileName() + "Config";
    }
}
