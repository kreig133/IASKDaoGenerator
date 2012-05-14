package com.kreig133.daogenerator;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.files.PackageAndFileUtils;
import com.kreig133.daogenerator.files.mybatis.mapping.MappingGenerator;
import com.kreig133.daogenerator.files.mybatis.test.TesterClassGenerator;
import com.kreig133.daogenerator.jaxb.NamingUtils;
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
        copyAppContextConfigToMavenProject();
        copyAbstractTest();
        copyBaseClass();
        generateSpringConfig();
    }

    private static void copyBaseClass() throws IOException {
        copyFile(
                "AbstractDao.txt",
                Settings.settings().getPathForGeneratedSource() +  "/"
                        + PackageAndFileUtils.replacePointBySlash(
                        "com.luxoft.sbrf.iask.persistence.common.dao.AbstractDao"
                ) + ".java"
        );
        copyFile(
                "AbstractDaoCommand.txt",
                Settings.settings().getPathForGeneratedSource() +  "/"
                        + PackageAndFileUtils.replacePointBySlash(
                        "com.luxoft.sbrf.iask.persistence.common.dao.AbstractDaoCommand"
                ) + ".java"
        );
    }

    private static void copyFile( String resourceName, String destFilePath ) throws IOException {
        copyFile(
                MavenProjectGenerator.class.getClassLoader().getResourceAsStream( resourceName ),
                new File( destFilePath )
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
                        getConfigName()
                )
        );
    }

    protected static String fillContextTemplateByData( @NotNull String string ) {
        return string
                .replaceFirst( "\\$\\{beanClass\\}",
                        Settings.settings().getMapperPackage() + "." +
                                MappingGenerator.instance().getFileName() )
                .replaceFirst( "\\$\\{beanName\\}",
                        NamingUtils.convertNameForNonClassNaming(
                                MappingGenerator.instance().getFileName() ) );
    }

    protected static void copyAbstractTest() throws IOException {
        copyFile(
                "AbstractDaoExecuteTest.txt",
                Settings.settings().getPathForGeneratedTests() + "/"
                        + PackageAndFileUtils.replacePointBySlash( TesterClassGenerator.PARENT ) + ".java"
        );
    }

    protected static void copyAppContextConfigToMavenProject() throws IOException {
        copyFile(
                "testApplicationContext.xml",
                Settings.settings().getPathForTestResources() + "/testApplicationContext.xml"
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
    
    @NotNull
    public static String getConfigName(){
        return TesterClassGenerator.TEST_CONFIG;
    }

    public static int installProject() {
        final String[] cmdarray = { "cmd", "/C",
                System.getProperty( "user.dir" )+"\\"+
                "apache-maven-3.0.4\\bin\\mvn -e -f "+ Settings.settings().getOutputPathForJavaClasses() +
                "\\pom.xml clean install"};

        for ( String s1 : cmdarray ) {
            System.out.println( s1 );
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
