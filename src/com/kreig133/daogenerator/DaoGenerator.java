package com.kreig133.daogenerator;

import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.common.settings.OperationSettingsImpl;
import com.kreig133.daogenerator.gui.MainForm;

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


    public static void main(String[] args) throws IOException {

        operationSettings = new OperationSettingsImpl();
        EventQueue.invokeLater( new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame( "MainForm" );
                frame.setContentPane          ( new MainForm().panel1 );
                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                //frame.setSize(500, 300);
                //frame.set //Установить точку где будет показано окно
                //frame.pack();
                //frame.setVisible( true );

                loadSettings(frame);
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
        final int w=Integer.parseInt(settings.getProperty("width","500"));
        final int h=Integer.parseInt(settings.getProperty("height","200"));
        final String sourceDir = settings.getProperty("sourceDir", "D:\\");
        final String destDir = settings.getProperty("destDir", "D:\\");
        final String entityPackage = settings.getProperty("entityPackage", "ru.sbrf.iask.so.aplana.persistence.entity.op148");
        final String interfacePackage = settings.getProperty("interfacePackage", "ru.sbrf.iask.so.aplana.persistence.dao");
        final String mappingPackage = settings.getProperty("mappingPackage", "ru.sbrf.iask.so.aplana.persistence.data");
        final boolean iask = Boolean.parseBoolean(settings.getProperty("iask", "1"));
        final boolean depo = Boolean.parseBoolean(settings.getProperty("depo", "0"));
        final boolean skipTests = Boolean.parseBoolean(settings.getProperty("skipTests","0"));

        final Component[] components = frame.getContentPane().getComponents();
        ((JTextField)components[0]).setText(sourceDir);
        ((JTextField)components[3]).setText(destDir);
        ((JTextField)components[6]).setText(entityPackage);
        ((JTextField)components[8]).setText(interfacePackage);
        ((JTextField)components[9]).setText(mappingPackage);
        ((JCheckBox)components[12]).setSelected(skipTests);
        ((JRadioButton)components[13]).setSelected(iask);
        ((JRadioButton)components[14]).setSelected(depo);

        frame.setSize(w, h);
        frame.setVisible(true);
    }

    public static OperationSettings getCurrentOperationSettings(){
        return operationSettings;
    }

}
