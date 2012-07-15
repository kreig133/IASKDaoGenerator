package com.kreig133.daogenerator;

import com.kreig133.daogenerator.files.PackageAndFileUtils;
import com.kreig133.daogenerator.files.mybatis.test.TesterClassGenerator;
import com.kreig133.daogenerator.settings.Settings;
import org.jetbrains.annotations.NotNull;

import java.io.*;

/**
 * @author kreig133
 * @version 1.0
 */
public class MavenProjectGenerator {

    public static void generate() throws IOException {
        copyPropertiesFileToMavenProject();
        copyPomFileToMavenProject();
        copyAbstractTest();
        copyBaseClassesAndConfigs();
        copySqlServerDriver();
    }

    private static void copySqlServerDriver() throws IOException {
        String path = "/lib/sqljdbc-3.0.jar";
        copyFile(
                new FileInputStream( System.getProperty("user.dir") + path ),
                new File( Settings.settings().getOutputPathForJavaClasses() + path )
        );
    }

    private static void copyBaseClassesAndConfigs() throws IOException {
        copyFile(
                "AbstractDao.txt",
                getPathForSourceJavaClass( "ru.sbrf.iask.foundation.persistence.dao.AbstractDao" )
        );
        copyFile(
                "ICredentials.txt",
                getPathForSourceJavaClass( "ru.sbrf.iask.foundation.persistence.entity.ICredentials" )
        );
        copyFile(
                "AbstractDao.map.xml",
                Settings.settings().getPathForTestResources() + "/AbstractDao.map.xml"
        );
        copyFile(
                "testApplicationContext.xml",
                Settings.settings().getPathForTestResources() + "/testApplicationContext.xml"
        );
    }

    private static String getPathForSourceJavaClass( String fullClassName ) {
        return Settings.settings().getPathForGeneratedSource() +  "/"
                + PackageAndFileUtils.replacePointBySlash(
                fullClassName
        ) + ".java";
    }

    private static void copyFile( String resourceName, String destFilePath ) throws IOException {
        copyFile(
                MavenProjectGenerator.class.getClassLoader().getResourceAsStream( resourceName ),
                new File( destFilePath )
        );
    }

    protected static void copyAbstractTest() throws IOException {
        copyFile(
                "AbstractDaoExecuteTest.txt",
                Settings.settings().getPathForGeneratedTests() + "/"
                        + PackageAndFileUtils.replacePointBySlash( TesterClassGenerator.PARENT ) + ".java"
        );
    }

    protected static void copyPomFileToMavenProject() throws IOException {
        copyFile(
                "pom.xml",
                Settings.settings().getOutputPathForJavaClasses() + "/pom.xml"
        );
    }

    protected static void copyPropertiesFileToMavenProject() throws IOException {
        copyFile(
                "application.properties",
                Settings.settings().getPathForTestResources() + "/application.properties"
        );
    }

    public static void copyFile( InputStream inputStream, @NotNull File outputFile  ) throws IOException {
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
    
    public static int installProject() {
        final String[] cmdarray = { "cmd", "/c",
        		String.format("\"\"%s/apache-maven-3.0.4/bin/mvn\" -e -f \"%s/pom.xml\" clean install\"", 
        				System.getProperty("user.dir"),
        				Settings.settings().getOutputPathForJavaClasses())};
        for ( String s : cmdarray ) {
            System.out.println( s );
        }

        try {
            return Communicator.communicate( cmdarray ).waitFor();
        } catch ( IOException e ) {
            e.printStackTrace();
            return -1;
        } catch ( InterruptedException e ) {
            e.printStackTrace();
            return -1;
        }
    }
}
