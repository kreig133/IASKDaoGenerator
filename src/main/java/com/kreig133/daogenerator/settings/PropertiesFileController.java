package com.kreig133.daogenerator.settings;

import com.kreig133.daogenerator.common.Utils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @Nullable
    public static Properties getDefaultProperties() {
        try{
            Properties settings = new Properties();

            settings.load( new FileInputStream( getDefaultPropertiesFileName() ) );

            final String sourceDir = settings.getProperty( Settings.SOURCE_DIR );
            //Пытаемся считать настройки из папки SOURCE_DIR
            if( StringUtils.isNotEmpty( sourceDir ) ){
                final Properties propertiesFromSourceDir = getPropertiesFromSourceDir( sourceDir );
                if( propertiesFromSourceDir != null ) {
                    settings.setProperty( Settings.DEST_DIR, propertiesFromSourceDir.getProperty( Settings.DEST_DIR ) );
                    settings.setProperty( Settings.MAPPING_PACKAGE,
                            propertiesFromSourceDir.getProperty( Settings.MAPPING_PACKAGE ) );
                }
            }

            return settings;
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    private static String getDefaultPropertiesFileName() {
        return Settings.PROPERTIES_FILE_NAME ;
    }

    @Nullable
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

    @Nullable
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

    public static void saveCommonProperties( @NotNull Properties properties ) {
        try {
            properties.store( new FileOutputStream( getDefaultPropertiesFileName() ), COMMENTS );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public static void saveSpecificProperties( String sourcePath, @NotNull Properties properties ) {
        try {
            File specificPropertiesFile = getSpecificPropertiesFile( sourcePath, true );

            if( specificPropertiesFile != null ){
                properties.store( new FileOutputStream( specificPropertiesFile ), COMMENTS );
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }
}
