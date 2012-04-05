package com.kreig133.daogenerator.files.builder;

import com.kreig133.daogenerator.JaxbHandler;
import com.kreig133.daogenerator.MavenProjectGenerator;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.files.Appender;
import com.kreig133.daogenerator.files.JavaClassGenerator;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.settings.OperationSettings;
import com.kreig133.daogenerator.settings.Settings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author eshangareev
 * @version 1.0
 */
public abstract class FileBuilder {
    protected List<JavaClassGenerator> generators = new ArrayList<JavaClassGenerator>();

    final public Map<File, String> build( List<DaoMethod> daoMethod ) {
        prepareBuilder( daoMethod );
        generateHead();
        generateBody( daoMethod );

        HashMap<File, String> result = new HashMap<File, String>();

        for ( JavaClassGenerator generator : generators ) {
            result.put( generator.getFile(), generator.getResult() );
            generator.reset();
        }

        return result;
    }

    protected abstract void prepareBuilder( List<DaoMethod> daoMethod );

    final protected void generateHead(){
        for ( JavaClassGenerator generator : generators ) {
            generator.generateHead();
        }
    }

    public abstract void generateBody( List<DaoMethod> daoMethods );

    public static void generateJavaCode() {
        System.out.println("Generating new maven project started...");

        final OperationSettings opSettings = Settings.settings();

        Settings.saveProperties();

        for ( String s : getXmlFileNamesInDirectory( Settings.settings().getSourcePath() ) ) {
            daoMethods.add( JaxbHandler.unmarshallFile(
                    Utils.getFileFromDirectoryByName( opSettings.getSourcePath(), s )
            ) );
        }

        try {
            generateAndWriteFiles();
            MavenProjectGenerator.generate();
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
        System.out.println("..generating new maven project finished.");
    }

    public static String[] getXmlFileNamesInDirectory( String path ) {
        return ( new File( path ) )
                .list(
                        new FilenameFilter() {
                            public boolean accept( File dir, String name ) {
                                return name.endsWith( "xml" );
                            }
                        }
                );
    }

    private static final java.util.List<DaoMethod> daoMethods = new ArrayList<DaoMethod>();

    protected static final Appender appender = new Appender() {
        @Override
        public void appendStringToFile( File file, String string ) {
            FileOutputStream writer = null;
            try {
                writer = new FileOutputStream( file, false );
                writer.write( string.getBytes( "UTF-8") );
            } catch ( IOException e ) {
                e.printStackTrace();
            } finally {
                if ( writer != null )
                    try {
                        writer.close();
                    } catch ( IOException e ) {
                        e.printStackTrace();
                    }
            }
        }
    };





    protected static void generateAndWriteFiles() throws IOException {

        List<FileBuilder> builders = new ArrayList<FileBuilder>( 2 );

        // порядок важен! из-за того, что при генерации моделей с Paging'ом
        // обновляются имена у параметров на родительские
        builders.add( ParameterClassBuilder.newInstance() );
        builders.add( MapperFileBuilder.newInstance() );

        Map<File, String> builded = null;
        for ( FileBuilder builder : builders ) {
            if( builded == null ){
                builded = builder.build( daoMethods );
            } else {
                builded.putAll( builder.build( daoMethods ) );
            }
        }

        assert builded != null;

        for ( File file : builded.keySet() ) {
            appender.appendStringToFile( file, builded.get( file ) );
        }

        daoMethods.clear();
    }
}
