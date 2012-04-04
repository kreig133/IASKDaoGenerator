package com.kreig133.daogenerator.gui;

import com.kreig133.daogenerator.settings.Settings;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * @author eshangareev
 * @version 1.0
 */
public class GuiUtils {

    private static final JFileChooser jFileChooser = new DaoGenFileChooser();

    static {
        jFileChooser.setMultiSelectionEnabled( false );
        jFileChooser.setCurrentDirectory( new File( System.getProperty( "user.home" ) ) );
        jFileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
    }

    public static JFileChooser getFileChooser(){
        return jFileChooser;
    }

    private static class DaoGenFileChooser extends JFileChooser{
        @Override
        public int showOpenDialog( Component parent ) throws HeadlessException {
            setSelectedFile( new File( Settings.settings().getLastDirectory() ) );
            int i = super.showOpenDialog( parent );
            Settings.settings().setLastDirectory( getSelectedFile().getAbsolutePath() );
            return i;
        }
        @Override
        public int showSaveDialog( Component parent ) throws HeadlessException {
            setSelectedFile( new File( Settings.settings().getLastDirectory() ) );
            int i = super.showSaveDialog( parent );
            Settings.settings().setLastDirectory( getSelectedFile().getAbsolutePath() );
            return i;
        }
    }
}
