package com.kreig133.daogenerator;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.files.mybatis.model.ModelClassGenerator;
import com.kreig133.daogenerator.files.mybatis.test.TesterClassGenerator;
import com.kreig133.daogenerator.gui.Form;
import com.kreig133.daogenerator.settings.OperationSettings;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.files.Appender;
import com.kreig133.daogenerator.files.InOutClassGenerator;
import com.kreig133.daogenerator.files.JavaClassGenerator;
import com.kreig133.daogenerator.files.mybatis.implementation.ImplementationGenerator;
import com.kreig133.daogenerator.files.mybatis.intrface.InterfaceGenerator;
import com.kreig133.daogenerator.files.mybatis.mapping.MappingGenerator;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.InOutType;
import com.kreig133.daogenerator.settings.PropertiesFileController;
import com.kreig133.daogenerator.settings.Settings;
import jsyntaxpane.DefaultSyntaxKit;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static com.kreig133.daogenerator.settings.Settings.*;
import static com.kreig133.daogenerator.settings.Settings.SOURCE_DIR;

/**
 * @author eshangareev
 * @version 1.0
 */
public class DaoGenerator {

    private static final java.util.List<DaoMethod> daoMethods = new ArrayList<DaoMethod>();

    protected static final Appender appender = new Appender() {
        @Override
        public void appendStringToFile( File file, String string ) {
            FileOutputStream writer = null;
            try {
                writer = new FileOutputStream( file, false );
                writer.write( string.getBytes( "UTF-8") );
            } catch ( IOException e ) {
                //TODO
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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
            writeFiles();
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

    protected static void writeFiles() throws IOException {

        List<JavaClassGenerator> generators = new ArrayList<JavaClassGenerator>();

        generators.add( MappingGenerator.instance() );

        if ( Settings.settings().getType() == Type.IASK ) {
            generators.add( InterfaceGenerator.instance() );
            generators.add( ImplementationGenerator.instance() );
        } else {
            generators.add( new TesterClassGenerator() );
        }

        for ( DaoMethod daoMethod: daoMethods ) {
            if ( InOutClassGenerator.checkToNeedOwnInClass( daoMethod ) ) {
                generators.add( InOutClassGenerator.newInstance( daoMethod, InOutType.IN ) );
            }
            if( Settings.settings().getType() == Type.IASK ){
                if ( daoMethod.getOutputParametrs().getParameter().size() > 1 ) {
                    generators.add( InOutClassGenerator.newInstance( daoMethod, InOutType.OUT ) );
                }
            } else {
                generators.add( ModelClassGenerator.newInstance( daoMethod.getOutputParametrs() ) );
            }
        }

        for ( JavaClassGenerator generator : generators ) {
            generator.generateHead();

            for( DaoMethod daoMethod: daoMethods ){
                generator.generateBody( daoMethod );
            }

            appender.appendStringToFile( generator.getFile(), generator.getResult() );
            generator.reset();
        }

        daoMethods.clear();
    }

    public static void main( String[] args ) {
        final Properties defaultProperties = PropertiesFileController.getDefaultProperties();

        Settings.loadSettingsFromProperties( defaultProperties );

        final String property = defaultProperties.getProperty( SOURCE_DIR, "" );

        if( ! "".equals( property ) ){
            Settings.settings().setSourcePath( property );
            final Properties propertiesFromSourceDir = PropertiesFileController.getPropertiesFromSourceDir( property );
            if( propertiesFromSourceDir != null ){
                Settings.loadSettingsFromProperties( propertiesFromSourceDir );
            }
        }

        EventQueue.invokeLater( new Runnable() {
            @Override
            public void run() {
                try {
                    DefaultSyntaxKit.initKit();
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
                final JFrame frame = new JFrame( "DaoGenerator 2.5" );


                frame.setContentPane( Form.getInstance() );
                frame.setSize( settings().getFrameWidth(), settings().getFrameHeight() );

                frame.addComponentListener( new ComponentAdapter() {
                    @Override
                    public void componentResized( ComponentEvent e ) {
                        settings().setFrameWidth( frame.getWidth() );
                        settings().setFrameHeight( frame.getHeight() );
                    }
                } );

                frame.addWindowListener( new WindowAdapter() {
                    @Override
                    public void windowClosing( WindowEvent e ) {
                        try {
                            Settings.saveProperties();
                        } finally {
                            System.exit( 0 );
                        }
                    }
                } );

                frame.setVisible( true );
            }
        } );
    }
}