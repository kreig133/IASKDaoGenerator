package com.kreig133.daogenerator;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.settings.FunctionSettingsImpl;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.common.settings.OperationSettingsImpl;
import com.kreig133.daogenerator.files.mybatis.MyBatis;
import com.kreig133.daogenerator.gui.MainForm;
import com.sun.corba.se.spi.orb.Operation;

import javax.swing.*;
import java.awt.*;
import java.io.*;

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
                frame.pack();
                frame.setVisible( true );
            }
        } );
    }

    public static OperationSettings getCurrentOperationSettings(){
        return operationSettings;
    }

}
