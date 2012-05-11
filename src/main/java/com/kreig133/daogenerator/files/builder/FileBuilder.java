package com.kreig133.daogenerator.files.builder;

import com.kreig133.daogenerator.JaxbHandler;
import com.kreig133.daogenerator.MavenProjectGenerator;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.files.Appender;
import com.kreig133.daogenerator.files.JavaClassGenerator;
import com.kreig133.daogenerator.files.mybatis.DaoJavaClassGenerator;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.jaxb.validators.DaoMethodValidator;
import com.kreig133.daogenerator.settings.OperationSettings;
import com.kreig133.daogenerator.settings.Settings;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    @NotNull
    protected List<JavaClassGenerator> generators = new ArrayList<JavaClassGenerator>();

    @NotNull
    final public Map<File, String> build() {
        prepareBuilder( daoMethods );
        generateHead();
        generateBody( daoMethods );

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

    public static boolean generateJavaCode() {
        System.out.println("Generating new maven project started...");

        final OperationSettings opSettings = Settings.settings();

        Settings.saveProperties();

        for ( String s : getXmlFileNamesInDirectory( Settings.settings().getSourcePath() ) ) {
            daoMethods.add( JaxbHandler.unmarshallFile(
                    Utils.getFileFromDirectoryByName( opSettings.getSourcePath(), s )
            ) );
        }
        boolean success = false;
        if( DaoMethodValidator.checkDaoMethods( daoMethods ) ){
            try {
                generateAndWriteFiles();
                MavenProjectGenerator.generate();
                success = true;
            } catch ( IOException e ) {
                throw new RuntimeException( e );
            }
        }

        daoMethods.clear();

        System.out.println("..generating new maven project finished.");
        return success;
    }



    public static String[] getXmlFileNamesInDirectory( String path ) {
        return ( new File( path ) )
                .list(
                        new FilenameFilter() {
                            public boolean accept( File dir, @NotNull String name ) {
                                return name.endsWith( "xml" );
                            }
                        }
                );
    }

    private static final List<DaoMethod> daoMethods = new ArrayList<DaoMethod>();

    @Nullable
    protected static final Appender appender = new Appender() {
        @Override
        public void appendStringToFile( File file, @NotNull String string ) {
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

    protected static void generateAndWriteFiles() {

        List<FileBuilder> builders = new ArrayList<FileBuilder>( 2 );

        // порядок важен! из-за того, что при генерации моделей с Paging'ом
        // обновляются имена у параметров на родительские
        builders.add( ParameterClassBuilder.newInstance() );
        builders.add( MapperFileBuilder.newInstance() );

        Map<File, String> builded = null;
        for ( FileBuilder builder : builders ) {
            if( builded == null ){
                builded = builder.build();
            } else {
                builded.putAll( builder.build() );
            }
        }

        assert builded != null;

        for ( File file : builded.keySet() ) {
            assert appender != null;
            appender.appendStringToFile( file, builded.get( file ) );
        }
    }
}
