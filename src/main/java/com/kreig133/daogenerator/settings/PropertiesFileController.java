package com.kreig133.daogenerator.settings;

import com.kreig133.daogenerator.common.Utils;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author C.C.-fag
 * @version 1.0
 */
public class PropertiesFileController {

    private static final String COMMENTS = "Settings for DaoGenerator";
    private static final String CREATE_FILE_MESSAGE = "Создать файл" + Settings.PROPERTIES_FILE_NAME + "в папке \"%s\"?";

    public static Properties getDefaultProperties() {
        try{
            Properties settings = new Properties();

            File file = getDefaultPropertiesFile();
            FileInputStream in = new FileInputStream( file );

            settings.load( in );

            final String sourceDir = settings.getProperty( Settings.SOURCE_DIR );
            //Пытаемся считать настройки из папки SOURCE_DIR
            if( sourceDir != null && !"".equals( sourceDir ) ){
                final Properties propertiesFromSourceDir = getPropertiesFromSourceDir( sourceDir );
                if( propertiesFromSourceDir != null ){
                    settings = propertiesFromSourceDir;
                    settings.setProperty( Settings.SOURCE_DIR, sourceDir );
                }
            }

            return settings;
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    private static File getDefaultPropertiesFile() {
        return new File( "properties/" + Settings.PROPERTIES_FILE_NAME );
    }

    public static Properties getPropertiesFromSourceDir( String sourceDirPath ) {
        try{
            Properties settings = new Properties();

            File file = getSpecificPropertiesFile( sourceDirPath, false );

            if( file == null || !file.exists() ){
                return null;
            }

            FileInputStream in = new FileInputStream( file );

            settings.load( in );

            return settings;
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    private static File getSpecificPropertiesFile( String sourceDirPath, boolean createIfNotExist ) {

        final File fileFromDirectoryByName =
                Utils.getFileFromDirectoryByName( sourceDirPath, Settings.PROPERTIES_FILE_NAME );
        if ( ! fileFromDirectoryByName.exists() ) {
            if ( createIfNotExist ) {
                try {
                    if ( JOptionPane.showConfirmDialog( null, String.format( CREATE_FILE_MESSAGE, sourceDirPath ) )
                            ==
                            JOptionPane.OK_OPTION
                            ) {
                        fileFromDirectoryByName.createNewFile();
                    } else {
                        return null;
                    }
                } catch ( IOException e ) {
                    e.printStackTrace();
                }
            } else {
                return null;
            }
        }
        return fileFromDirectoryByName;
    }

    public static void saveCommonProperties( Properties properties ) {
        try {
            properties.store( new FileOutputStream( getDefaultPropertiesFile() ), COMMENTS );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public static void saveSpecificProperties( String sourcePath, Properties properties ) {
        try {
            properties.store( new FileOutputStream( getSpecificPropertiesFile( sourcePath, true ) ), COMMENTS );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }
}
