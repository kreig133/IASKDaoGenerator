package com.kreig133.daogenerator.gui;

import com.kreig133.daogenerator.Controller;
import com.kreig133.daogenerator.DaoGenerator;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.enums.Type;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.Properties;
import java.util.regex.Pattern;

import static com.kreig133.daogenerator.settings.PropertiesFileController.getDefaultProperties;
import static com.kreig133.daogenerator.settings.PropertiesFileController.getPropertiesFromSourceDir;
import static com.kreig133.daogenerator.settings.SettingName.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class MainForm {

    private JPanel       mainPanel;
    private JTextField   sourceDirTextField;
    private JButton      setSourceDirButton;
    private JTextField   destDirTextField;
    private JButton      setDestDirButton;
    private JTextField   entityPackageTextField;
    private JTextField   interfacePackageTextField;
    private JTextField   mappingPackageTextField;
    private JCheckBox    skipTestsCheckBox;
    private JButton      startButton;
    private JRadioButton IASKRadioButton;
    private JRadioButton DEPORadioButton;
    private static MainForm INSTANCE;
    private boolean start = true;

    protected MainForm() {
        setSourceDirButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                setSourcePath();
            }
        } );
        setDestDirButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                setOutputPath();
            }
        } );
        startButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                if( validate() ){
                    fillSettingsWithData();
                    mainPanel.setVisible( false );
                    Controller.doAction();
                    System.exit( 0 );
                }
            }
        } );
        
        sourceDirTextField.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                reloadProperties();
            }
        } );

        sourceDirTextField.addFocusListener( new FocusListener() {
            @Override
            public void focusGained( FocusEvent e ) { /* do nothing */ }

            @Override
            public void focusLost( FocusEvent e ) {
                reloadProperties();
            }
        } );

        loadSettingsFromProperties( getDefaultProperties() );
    }

    private void reloadProperties() {
        loadSettingsFromProperties( getPropertiesFromSourceDir( sourceDirTextField.getText() ) );
    }

    private void loadSettingsFromProperties( Properties settings ) {
        if ( settings == null ) {
            return;
        }

        skipTestsCheckBox           .setSelected( Boolean.parseBoolean( settings.getProperty( SKIP_TESTS,   "0" ) ) );
        IASKRadioButton             .setSelected( Boolean.parseBoolean( settings.getProperty( IASK,         "1" ) ) );
        DEPORadioButton             .setSelected( Boolean.parseBoolean( settings.getProperty( DEPO,         "0" ) ) );
        
        destDirTextField            .setText( settings.getProperty( DEST_DIR, "D:\\" ) );
        entityPackageTextField      .setText( settings.getProperty( ENTITY_PACKAGE, "ru.sbrf.aplana.entity" ) );
        interfacePackageTextField   .setText( settings.getProperty( INTERFACE_PACKAGE, "ru.sbrf.aplana.dao" ) );
        mappingPackageTextField     .setText( settings.getProperty( MAPPING_PACKAGE, "ru.sbrf.aplana.data" ) );
        
        if( start ){
            mainPanel.setSize(
                    Integer.parseInt( settings.getProperty( WIDTH, "500" ) ) ,
                    Integer.parseInt( settings.getProperty( HEIGHT, "200" ) )
            );
            sourceDirTextField          .setText( settings.getProperty( SOURCE_DIR          , "D:\\") );
            start = false;
        }
    }

    private void fillSettingsWithData() {
        OperationSettings operationSettings = DaoGenerator.getCurrentOperationSettings();

        operationSettings.setOutputPath     ( destDirTextField              .getText() + "/" + "src" );
        operationSettings.setSkipTesting    ( skipTestsCheckBox             .isSelected () );
        operationSettings.setSourcePath     ( sourceDirTextField            .getText    () );
        operationSettings.setDaoPackage     ( interfacePackageTextField     .getText    () );
        operationSettings.setEntityPackage  ( entityPackageTextField        .getText    () );
        operationSettings.setMapperPackage  ( mappingPackageTextField       .getText    () );
        operationSettings.setOperationName  ( tempOperationName == null ?
                new File( sourceDirTextField.getText()).getName() : tempOperationName );

        operationSettings.setType( IASKRadioButton.isSelected() ? Type.IASK : Type.DEPO );
    }

    private boolean validate() {
        if ( (   IASKRadioButton.isSelected() &&   DEPORadioButton.isSelected() )  ||
             ( ! IASKRadioButton.isSelected() && ! DEPORadioButton.isSelected() ) ){
            JOptionPane.showMessageDialog( mainPanel, "Выберите один (!) тип проекта." );
            return false;
        }
        if(
                ( ! isPackageName( interfacePackageTextField.getText() ) ) ||
                ( ! isPackageName( entityPackageTextField   .getText() ) ) ||
                ( ! isPackageName( mappingPackageTextField  .getText() ) )
        ){
            JOptionPane.showMessageDialog( mainPanel, "Одно или несколкьо имен пакетов не прошло валидацию." );
            return false;
        }

        //TODO надо бы проверить пути

        return true;
    }

    private boolean isPackageName( String packageName ){
        return Pattern.compile( "[\\w\\d]+(\\.[\\w\\d]+)+" ).matcher( packageName ).matches();
    }

    private final JFileChooser fc = new JFileChooser( );
    {
        fc.setMultiSelectionEnabled ( false );
        fc.setCurrentDirectory      ( new File( System.getProperty("user.dir") ) );
        fc.setFileSelectionMode     ( JFileChooser.DIRECTORIES_ONLY );
    }

    private void setOutputPath() {
        int returnVal = fc.showSaveDialog( null );

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            destDirTextField.setText( file.getAbsolutePath() );
        }
    }

    private String tempOperationName;

    private void setSourcePath() {
        int returnVal = fc.showOpenDialog( mainPanel );

        if ( returnVal == JFileChooser.APPROVE_OPTION ) {
            File file           = fc.getSelectedFile();
            tempOperationName   = file.getName();
            sourceDirTextField.setText( file.getAbsolutePath() );
            reloadProperties();
        }
    }

    public static JPanel getInstance(){
        if ( INSTANCE == null ) {
            INSTANCE = new MainForm();
        }

        return INSTANCE.mainPanel;
    }
}

