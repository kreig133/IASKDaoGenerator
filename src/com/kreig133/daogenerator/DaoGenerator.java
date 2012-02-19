package com.kreig133.daogenerator;

import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.common.settings.OperationSettingsImpl;
import com.kreig133.daogenerator.gui.MainForm;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * @author eshangareev
 * @version 1.0
 */
public class DaoGenerator {

    private static OperationSettings operationSettings;


    public static void main(String[] args) throws IOException {

        operationSettings = new OperationSettingsImpl();
        EventQueue.invokeLater( new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame( "MainForm" );
                frame.setContentPane          ( new MainForm().panel1 );
                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                frame.setSize(500, 300);
                //frame.set //Установить точку где будет показано окно
                //frame.pack();
                frame.setVisible( true );

                loadSettings();
            }
        } );
    }

    private static void loadSettings() {
        Properties settings=new Properties();
        try{
            File file = new File(DaoGenerator.class.getResource("DaoGenerator.ini").toURI());
            FileInputStream in=new FileInputStream(file);
            settings.load(in);
        } catch(IOException e){
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        int h=Integer.parseInt(settings.getProperty("height","200"));
        int w=Integer.parseInt(settings.getProperty("width","300"));
        //setSize(w,h);
    }

    public static OperationSettings getCurrentOperationSettings(){
        return operationSettings;
    }

}
