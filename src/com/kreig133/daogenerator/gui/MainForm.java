package com.kreig133.daogenerator.gui;

import com.kreig133.daogenerator.DaoGenerator;
import com.kreig133.daogenerator.common.Settings;
import com.kreig133.daogenerator.enums.Type;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @author eshangareev
 * @version 1.0
 */
public class MainForm {

    public  JPanel panel1;
    private JTextField sourceDirTextField;
    private JButton setSourceDirButton;
    private JTextField destDirTextField;
    private JButton setDestDirButton;
    private JTextField entityPackageTextField;
    private JTextField interfacePackageTextField;
    private JTextField mappingPackageTextField;
    private JCheckBox сУказаниемInOutCheckBox;
    private JButton startButton;
    private JRadioButton IASKRadioButton;
    private JRadioButton DEPORadioButton;

    public MainForm() {
        setSourceDirButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                setSourcePath( DaoGenerator.getCurrentSettings() );
            }
        } );
        setDestDirButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                setOutputPath( DaoGenerator.getCurrentSettings() );
            }
        } );
        startButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                if( validate() ){
                    try {
                        fillSettingsWithData();
                        DaoGenerator.doAction();
                    } catch ( IOException e1 ) {
                        e1.printStackTrace();
                        //TODO вывести окно с ошибкой
                    }
                }
            }
        } );
    }

    private void fillSettingsWithData() {
        Settings settings = DaoGenerator.getCurrentSettings();

        settings.setSourcePath      ( sourceDirTextField        .getText() );
        settings.setOutputPath      ( destDirTextField          .getText() + "/" + "src" );
        settings.setDaoPackage      ( interfacePackageTextField .getText() );
        settings.setEntityPackage   ( entityPackageTextField    .getText() );
        settings.setMapperPackage   ( mappingPackageTextField   .getText() );
        settings.setOperationName   ( tempOperationName                    );

        settings.setType( IASKRadioButton.isSelected()? Type.IASK: Type.DEPO );
    }

    private boolean validate() {
        if ( (   IASKRadioButton.isSelected() &&   DEPORadioButton.isSelected() )  ||
             ( ! IASKRadioButton.isSelected() && ! DEPORadioButton.isSelected() ) ){
            JOptionPane.showMessageDialog( panel1, "Выберите один (!) тип проекта." );
            return false;
        }
        if(
                ( ! isPackageName( interfacePackageTextField.getText() ) ) ||
                ( ! isPackageName( entityPackageTextField   .getText() ) ) ||
                ( ! isPackageName( mappingPackageTextField  .getText() ) )
        ){
            JOptionPane.showMessageDialog( panel1, "Одно или несколкьо имен пакетов не прошло валидацию." );
            return false;
        }

        //TODO надо бы проверить пути

        return true;
    }

    private boolean isPackageName( String packageName ){
        return Pattern.compile( "[\\w\\d]+(\\.[\\w\\d]+)+" ).matcher( packageName ).matches();
    }

    JFileChooser fc = new JFileChooser( );
    {
        fc.setMultiSelectionEnabled ( false );
        fc.setCurrentDirectory      ( new File( System.getProperty("user.dir") ) );
        fc.setFileSelectionMode     ( JFileChooser.DIRECTORIES_ONLY );
    }

    private void setOutputPath( Settings settings ) {
        int returnVal = fc.showSaveDialog( null );

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            destDirTextField.setText( file.getAbsolutePath() );
        }
    }

    private String tempOperationName;

    private void setSourcePath( Settings settings ) {
        int returnVal = fc.showOpenDialog( panel1 );

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file           = fc.getSelectedFile();
            tempOperationName   = file.getName();
            sourceDirTextField.setText( file.getAbsolutePath() );
        }
    }
}

