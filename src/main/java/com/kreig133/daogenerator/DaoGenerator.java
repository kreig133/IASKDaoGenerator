package com.kreig133.daogenerator;

import com.kreig133.daogenerator.gui.Form;
import com.kreig133.daogenerator.presenter.FormPresenter;
import com.kreig133.daogenerator.settings.PropertiesFileController;
import com.kreig133.daogenerator.settings.Settings;
import jsyntaxpane.DefaultSyntaxKit;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;

import static com.kreig133.daogenerator.settings.Settings.SOURCE_DIR;
import static com.kreig133.daogenerator.settings.Settings.settings;

/**
 * @author eshangareev
 * @version 1.0
 */
public class DaoGenerator {

    public static String VERSION = "error";

    public static void main( String[] args ) {
        final Properties defaultProperties = PropertiesFileController.getDefaultProperties();

        assert defaultProperties != null;
        Settings.loadSettingsFromProperties( defaultProperties );

        VERSION = defaultProperties.getProperty( Settings.VERSION );

        final String property = defaultProperties.getProperty( SOURCE_DIR, "" );

        if( StringUtils.isNotBlank( property ) ){
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
                final JFrame frame = new JFrame( "DaoGenerator " + VERSION );

                FormPresenter presenter = new FormPresenter();
                presenter.setView(Form.getInstance(presenter));
                frame.setContentPane( Form.getMainPanel(presenter) );
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