package com.kreig133.daogenerator;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.common.settings.OperationSettingsImpl;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.files.Appender;
import com.kreig133.daogenerator.files.InOutClassGenerator;
import com.kreig133.daogenerator.files.JavaClassGenerator;
import com.kreig133.daogenerator.files.mybatis.implementation.ImplementationGenerator;
import com.kreig133.daogenerator.files.mybatis.intrface.InterfaceGenerator;
import com.kreig133.daogenerator.files.mybatis.mapping.MappingGenerator;
import com.kreig133.daogenerator.gui.MainForm;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.InOutType;
import com.kreig133.daogenerator.settings.PropertiesFileController;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

import static com.kreig133.daogenerator.settings.SettingName.*;
import static com.kreig133.daogenerator.settings.SettingName.MAPPING_PACKAGE;
import static com.kreig133.daogenerator.settings.SettingName.SOURCE_DIR;

/**
 * @author eshangareev
 * @version 1.0
 */
public class DaoGenerator {

    private static OperationSettings operationSettings = new OperationSettingsImpl();

    public static OperationSettings settings(){
        return operationSettings;
    }

    private static final java.util.List<DaoMethod> daoMethods = new ArrayList<DaoMethod>();

    protected static final Appender appender = new Appender() {
        @Override
        public void appendStringToFile( File file, String string ) {
            FileOutputStream writer = null;
            try {

                writer = new FileOutputStream( file, false );
                writer.write( string.getBytes() );
            } catch ( IOException e ) {
                //TODO
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } finally {
                if ( writer != null )
                    try {
                        writer.close();
                    } catch ( IOException e ) {
                        e.printStackTrace();  //TODO
                    }
            }
        }
    };

    public static void doAction() {

        final OperationSettings opSettings = DaoGenerator.settings();

        saveProperties();

        for( String s : getXmlFileNamesInDirectory() ) {
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

    }

    public static String[] getXmlFileNamesInDirectory( ) {
        return ( new File( DaoGenerator.settings() .getSourcePath() ) )
                .list(
                        new FilenameFilter() {
                            public boolean accept( File dir, String name ) {
                                return name.endsWith( "xml" );
                            }
                        }
                );
    }

    protected static void writeFiles() throws IOException {

        java.util.List<JavaClassGenerator> generators = new ArrayList<JavaClassGenerator>();

        generators.add( MappingGenerator.instance() );

        if ( DaoGenerator.settings().getType() == Type.IASK ) {
            generators.add( InterfaceGenerator.instance() );
            generators.add( ImplementationGenerator.instance() );
        }

        for ( DaoMethod daoMethod: daoMethods ) {
            if ( InOutClassGenerator.checkToNeedOwnInClass( daoMethod ) ) {
                generators.add( InOutClassGenerator.newInstance( daoMethod, InOutType.IN ) );
            }

            if ( daoMethod.getOutputParametrs().getParameter().size() > 1 ) {
                generators.add( InOutClassGenerator.newInstance( daoMethod, InOutType.OUT ) );
            }
        }

        for ( JavaClassGenerator generator : generators ) {
            generator.generateHead();

            for( DaoMethod daoMethod: daoMethods ){
                generator.generateBody( daoMethod );
            }

            generator.generateFoot();

            appender.appendStringToFile( generator.getFile(), generator.getResult() );
        }
    }

    protected static void saveProperties() {
        Properties properties = new Properties();

        final OperationSettings operationSettings = DaoGenerator.settings();

        properties.setProperty( IASK                , String.valueOf( operationSettings.getType() == Type.IASK ) );
        properties.setProperty( DEPO                , String.valueOf( operationSettings.getType() == Type.DEPO ) );

        properties.setProperty( WIDTH               , String.valueOf( ( int ) MainForm.getInstance().getSize().getWidth () ) );
        properties.setProperty( HEIGHT              , String.valueOf( ( int ) MainForm.getInstance().getSize().getHeight() ) );

        properties.setProperty( DEST_DIR            , operationSettings.getOutputPath() );
        properties.setProperty( ENTITY_PACKAGE      , operationSettings.getEntityPackage() );
        properties.setProperty( INTERFACE_PACKAGE   , operationSettings.getDaoPackage() );
        properties.setProperty( MAPPING_PACKAGE     , operationSettings.getMapperPackage() );

        PropertiesFileController.saveSpecificProperties( operationSettings.getSourcePath(), properties );

        properties.setProperty( SOURCE_DIR          , operationSettings.getSourcePath() );

        PropertiesFileController.saveCommonProperties( properties );

    }

    public static void main(String[] args) throws IOException {

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        EventQueue.invokeLater( new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame( "MainForm" );
                frame.setContentPane          ( MainForm.getInstance() );
                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                frame.setSize( MainForm.getInstance().getSize() );
                frame.setVisible(true);
            }
        } );
    }
}
