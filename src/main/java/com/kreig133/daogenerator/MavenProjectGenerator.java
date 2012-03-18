package com.kreig133.daogenerator;

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

    }

    private static void copyAppContextConfigToMavenProject() throws IOException {
        copyFile(
                MavenProjectGenerator.class.getClassLoader().getResourceAsStream( "testApplicationContext.xml" ),
                new File( DaoGenerator.settings().getPathForTestResources() + "/testApplicationContext.xml" )
        );
    }

    private static void copyPomFileToMavenProject() throws IOException {
        copyFile(
                MavenProjectGenerator.class.getClassLoader().getResourceAsStream( "pom.xml" ),
                new File( DaoGenerator.settings().getOutputPath() + "/pom.xml" )
        );
    }

    private static void copyPropertiesFileToMavenProject() throws IOException {
        final InputStream resourceAsStream = MavenProjectGenerator.class.getClassLoader().
                getResourceAsStream( "depo/application.properties" );// TODO

        final File propertiesOut = new File( DaoGenerator.settings().getPathForTestResources() + "/application.properties" );
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
}
