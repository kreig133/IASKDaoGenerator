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

    public static JFileChooser getNewFileChooser(){
        JFileChooser jFileChooser = new DaoGenFileChooser();
        jFileChooser.setMultiSelectionEnabled( false );
        jFileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
        jFileChooser.setSelectedFile( new File( Settings.settings().getLastDirectory() ) );
        return jFileChooser;
    }

    private static class DaoGenFileChooser extends JFileChooser{
        @Override
        public int showOpenDialog( Component parent ) throws HeadlessException {
            int i = super.showOpenDialog( parent );
            Settings.settings().setLastDirectory( getSelectedFile().getAbsolutePath() );
            return i;
        }
        @Override
        public int showSaveDialog( Component parent ) throws HeadlessException {

            int i = super.showSaveDialog( parent );
            Settings.settings().setLastDirectory( getSelectedFile().getAbsolutePath() );
            return i;
        }
    }
}
