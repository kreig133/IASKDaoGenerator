package com.kreig133.daogenerator;

import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.common.settings.OperationSettingsImpl;
import com.kreig133.daogenerator.gui.MainForm;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * @author eshangareev
 * @version 1.0
 */
public class DaoGenerator {

    private static OperationSettings operationSettings = new OperationSettingsImpl();

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

    public static OperationSettings getCurrentOperationSettings(){
        return operationSettings;
    }
}
