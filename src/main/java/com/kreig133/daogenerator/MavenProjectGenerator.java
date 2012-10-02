package com.kreig133.daogenerator;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.files.ClassFiles;
import com.kreig133.daogenerator.files.PackageAndFileUtils;
import com.kreig133.daogenerator.files.mybatis.model.ModelClassGenerator;
import com.kreig133.daogenerator.jaxb.NamingUtils;
import com.kreig133.daogenerator.files.mybatis.mapping.MappingGenerator;
import com.kreig133.daogenerator.files.mybatis.test.TesterClassGenerator;
import com.kreig133.daogenerator.jaxb.ParentType;
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
        copyBaseModels();
        copyReferenceClasses();
        generateSpringConfig();
    }

    private static void copyBaseModels() throws IOException {
        for ( ParentType parentType : ParentType.values() ) {
            copyFile(
                    PackageAndFileUtils.getShortName( ModelClassGenerator.parentImport.get( parentType ) ) + ".txt",
                    Settings.settings().getPathForGeneratedSource() +  "/"
                            + PackageAndFileUtils.replacePointBySlash(
                                ModelClassGenerator.parentImport.get( parentType )
                            ) + ".java"
            );
        }
    }
    
    private static void copyReferenceClasses() throws IOException {
        for ( ClassFiles classFile : ClassFiles.values() ) {
            copyFile(
                    classFile.getFileName(),
                    Settings.settings().getPathForGeneratedSource() +  "/"
                            + PackageAndFileUtils.replacePointBySlash(classFile.getClassName()) + ".java"
            );
        }
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
                "AbstractDepoDaoExecuteTest.txt",
                Settings.settings().getPathForGeneratedTests()
                        + "/com/aplana/sbrf/deposit/AbstractDepoDaoExecuteTest.java"
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
                new FileInputStream( new File( System.getProperty( "user.dir") +  "/application.properties" ) ),
                new File( Settings.settings().getPathForTestResources() + "/application.properties" )
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

    /**
     * Запускает сборку тестового проекта
     * @return результат выполнения команды cmd
     */
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
