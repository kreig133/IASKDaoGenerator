package com.kreig133.daogenerator;

import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.common.settings.OperationSettingsImpl;
import com.kreig133.daogenerator.gui.MainForm;
import org.dyndns.phpusr.daogenerator.settings.SettingName;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author eshangareev
 * @version 1.0
 */
public class DaoGenerator {

    private static OperationSettings operationSettings;
    public static final String DAO_GENERATOR_PROPERTIES = "DaoGenerator.properties";
    public static final String MAPPING_PACKAGE = "mappingPackage";
    public static final String IASK = "iask";
    public static final String DEPO = "depo";
    public static final String SKIP_TESTS = "skipTests";


    public static void main(String[] args) throws IOException {

        operationSettings = new OperationSettingsImpl();
        EventQueue.invokeLater( new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame( "MainForm" );
                frame.setContentPane          ( new MainForm().panel1 );
                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                //frame.set //Установить точку где будет показано окно

                loadSettings(frame);
                frame.setVisible(true);
                //System.exit(0);
            }
        } );
    }

    private static void loadSettings(JFrame frame) {
        Properties settings=new Properties();
        try{
            File file = new File(DAO_GENERATOR_PROPERTIES);
            FileInputStream in=new FileInputStream(file);
            settings.load(in);
        } catch(IOException e){
            e.printStackTrace();
        }
        final int width=Integer.parseInt(settings.getProperty(SettingName.WIDTH,"500"));
        final int height=Integer.parseInt(settings.getProperty(SettingName.HEIGHT,"200"));
        final String sourceDir = settings.getProperty(SettingName.SOURCE_DIR, "D:\\");
        final String destDir = settings.getProperty(SettingName.DEST_DIR, "D:\\");
        final String entityPackage = settings.getProperty(SettingName.ENTITY_PACKAGE, "ru.sbrf.iask.so.aplana.persistence.entity.op148");
        final String interfacePackage = settings.getProperty(SettingName.INTERFACE_PACKAGE, "ru.sbrf.iask.so.aplana.persistence.dao");
        final String mappingPackage = settings.getProperty(MAPPING_PACKAGE, "ru.sbrf.iask.so.aplana.persistence.data");
        final boolean iask = Boolean.parseBoolean(settings.getProperty(IASK, "1"));
        final boolean depo = Boolean.parseBoolean(settings.getProperty(DEPO, "0"));
        final boolean skipTests = Boolean.parseBoolean(settings.getProperty(SKIP_TESTS,"0"));

        //TODO сделать загрузку с помощью properties.load( props );

        final Component[] components = frame.getContentPane().getComponents();
        ((JTextField)components[0]).setText(sourceDir);
        ((JTextField)components[3]).setText(destDir);
        ((JTextField)components[6]).setText(entityPackage);
        ((JTextField)components[8]).setText(interfacePackage);
        ((JTextField)components[9]).setText(mappingPackage);
        ((JCheckBox)components[12]).setSelected(skipTests);
        ((JRadioButton)components[13]).setSelected(iask);
        ((JRadioButton)components[14]).setSelected(depo);

        frame.setSize(width, height);
    }

    public static OperationSettings getCurrentOperationSettings(){
        return operationSettings;
    }

}
