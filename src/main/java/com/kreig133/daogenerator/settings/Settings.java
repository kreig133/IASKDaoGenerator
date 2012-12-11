package com.kreig133.daogenerator.settings;

/**
 * Created by IntelliJ IDEA.
 * @author phpusr
 * Date: 23.02.12
 * Time: 12:30
 */

import com.kreig133.daogenerator.DaoGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Properties;

/**
 * Хранит имена параметров, чтобы по ним обращаться к файлу с настройками
 */
public class Settings {
    @NotNull
    private static OperationSettings operationSettings = new OperationSettingsImpl();

    @NotNull
    public static OperationSettings settings(){
        return operationSettings;
    }


    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String SOURCE_DIR = "sourceDir";
    public static final String DEST_DIR = "destDir";
    public static final String ENTITY_PACKAGE = "entityPackage";
    public static final String INTERFACE_PACKAGE = "interfacePackage";
    public static final String PROPERTIES_FILE_NAME = "DaoGenerator.properties";
    public static final String MAPPING_PACKAGE = "mappingPackage";
    public static final String LAST_DIRECTORY = "lastDirectory";
    public static final String PROJECT_FOLDER = "home";
    public static final String VERSION = "version";


    public static final String URL = "jdbc.url";
    public static final String USERNAME = "jdbc.username";
    public static final String PASSWORD = "jdbc.password";
    public static final String DRIVER = "jdbc.driverClass";


    public static void saveProperties() {
        Properties properties = new Properties();

        properties.setProperty( DEST_DIR            , operationSettings.getOutputPathForJavaClasses () );
        properties.setProperty( MAPPING_PACKAGE     , operationSettings.getMapperPackage            () );
        properties.setProperty( ENTITY_PACKAGE      , operationSettings.getEntityPackage            () );
        properties.setProperty( INTERFACE_PACKAGE   , operationSettings.getDaoPackage               () );

        if ( new File( settings().getSourcePath()  ).exists() ) {
            PropertiesFileController.saveSpecificProperties( operationSettings.getSourcePath(), properties );
        }

        properties.setProperty( WIDTH               , String.valueOf( settings().getFrameWidth () ) );
        properties.setProperty( HEIGHT              , String.valueOf( settings().getFrameHeight() ) );
        properties.setProperty( SOURCE_DIR          , operationSettings.getSourcePath() );
        properties.setProperty( PROJECT_FOLDER      , operationSettings.getProjectFolder() );
        properties.setProperty( LAST_DIRECTORY      , operationSettings.getLastDirectory() );
        properties.setProperty( VERSION             , DaoGenerator.VERSION );
        PropertiesFileController.saveCommonProperties( properties );

    }

    public static void loadSettingsFromProperties( @NotNull Properties properties ){

        settings().setDaoPackage   ( properties.getProperty( INTERFACE_PACKAGE, settings().getDaoPackage   () ) );
        settings().setEntityPackage( properties.getProperty( ENTITY_PACKAGE, settings().getEntityPackage() ) );
        settings().setMapperPackage( properties.getProperty( MAPPING_PACKAGE, settings().getMapperPackage() ) );
        settings().setProjectFolder( properties.getProperty( PROJECT_FOLDER, settings().getProjectFolder() ) );
        settings().setLastDirectory( properties.getProperty( LAST_DIRECTORY, settings().getLastDirectory() ) );
        settings().setOutputPathForJavaClasses(
                properties.getProperty( DEST_DIR, settings().getOutputPathForJavaClasses() )
        );
        settings().setFrameHeight(
                Integer.valueOf( properties.getProperty( HEIGHT, String.valueOf( settings().getFrameHeight() ) ) )
        );
        settings().setFrameWidth(
                Integer.valueOf( properties.getProperty( WIDTH, String.valueOf( settings().getFrameWidth() ) ) )
        );
    }
}
