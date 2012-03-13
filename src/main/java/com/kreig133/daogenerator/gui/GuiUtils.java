package com.kreig133.daogenerator.gui;

import javax.swing.*;
import java.io.File;

/**
 * @author eshangareev
 * @version 1.0
 */
public class GuiUtils {
    public static JFileChooser getFileChooser(){
        final JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setMultiSelectionEnabled( false );
        jFileChooser.setCurrentDirectory( new File( System.getProperty( "user.dir" ) ) );
        jFileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
        return jFileChooser;
    }
}
