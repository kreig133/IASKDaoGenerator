package com.kreig133.daogenerator;

import com.kreig133.daogenerator.common.SettingsImpl;
import com.kreig133.daogenerator.files.InOutClass;
import com.kreig133.daogenerator.common.Settings;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.enums.ReturnType;
import com.kreig133.daogenerator.enums.SelectType;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.files.mybatis.MyBatis;
import com.kreig133.daogenerator.gui.MainForm;
import com.kreig133.daogenerator.parameter.Parameter;
import com.kreig133.daogenerator.files.parsers.InputFileParser;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.kreig133.daogenerator.common.Utils.*;
/**
 * @author eshangareev
 * @version 1.0
 */
public class DaoGenerator {

    static Settings settings;


    public static void main(String[] args) throws IOException {

        settings = new SettingsImpl();
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

    public static void doAction() throws IOException {

        MyBatis.prepareFiles            ( settings );

        for(
                String s:
                ( new File( settings.getSourcePath() ) )
                        .list(
                                new FilenameFilter() {
                                    public boolean accept(File dir, String name) {
                                        return name.endsWith("txt");
                                    }
                                }
                        )
        ) {
            Controller.controller( new File( settings.getSourcePath() + "/"+s), settings );
        }

        MyBatis.closeFiles      ( settings );
    }

    public static Settings getCurrentSettings(){
        return settings;
    }

}
