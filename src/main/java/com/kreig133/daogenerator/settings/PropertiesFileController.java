package com.kreig133.daogenerator.settings;

import com.kreig133.daogenerator.common.Utils;

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

    public static Properties getDefaultProperties() {
        try{
            Properties settings = new Properties();

            File file = getDefaultPropertiesFile();
            FileInputStream in = new FileInputStream( file );

            settings.load( in );

            final String sourceDir = settings.getProperty( SettingName.SOURCE_DIR );
            //Пытаемся считать настройки из папки SOURCE_DIR
            if( sourceDir != null && !"".equals( sourceDir ) ){
                final Properties propertiesFromSourceDir = getPropertiesFromSourceDir( sourceDir );
                if( propertiesFromSourceDir != null ){
                    settings = propertiesFromSourceDir;
                    settings.setProperty( SettingName.SOURCE_DIR, sourceDir );
                }
            }

            return settings;
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    private static File getDefaultPropertiesFile() {
        return new File( SettingName.PROPERTIES_FILE_NAME );
    }

    public static Properties getPropertiesFromSourceDir( String sourceDirPath ) {
        try{
            Properties settings = new Properties();

            File file = getSpecificPropertiesFile( sourceDirPath );

            if( !file.exists() ){
                return null;
            }

            FileInputStream in = new FileInputStream( file );

            settings.load( in );

            return settings;
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    private static File getSpecificPropertiesFile( String sourceDirPath ) {
        final File fileFromDirectoryByName =
                Utils.getFileFromDirectoryByName( sourceDirPath, SettingName.PROPERTIES_FILE_NAME );
        if ( ! fileFromDirectoryByName.exists() ) {
            //TODO по идее можно спрашивать юзера
            try {
                fileFromDirectoryByName.createNewFile();
            } catch ( IOException e ) {
                e.printStackTrace();
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
            properties.store( new FileOutputStream( getSpecificPropertiesFile( sourcePath ) ), COMMENTS );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }
}
