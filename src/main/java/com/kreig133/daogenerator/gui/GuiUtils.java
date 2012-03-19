package com.kreig133.daogenerator.gui;

import javax.swing.*;
import java.io.File;

/**
 * @author eshangareev
 * @version 1.0
 */
public class GuiUtils {

    private static final JFileChooser jFileChooser = new JFileChooser();

    static {
        jFileChooser.setMultiSelectionEnabled( false );
        jFileChooser.setCurrentDirectory( new File( System.getProperty( "user.home" ) ) );
        jFileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
    }

    public static JFileChooser getFileChooser(){
        return jFileChooser;
    }
}
