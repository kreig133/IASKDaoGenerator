package com.kreig133.daogenerator.settings;

/**
 * Created by IntelliJ IDEA.
 * @author phpusr
 * Date: 23.02.12
 * Time: 12:30
 */

import com.kreig133.daogenerator.enums.Type;

import java.io.File;
import java.util.Properties;

/**
 * Хранит имена параметров, чтобы по ним обращаться к файлу с настройками
 */
public class Settings {
    private static OperationSettings operationSettings = new OperationSettingsImpl();

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
    public static final String MODEL_PACKAGE  = "modelPackge";
    public static final String IASK = "iask";
    public static final String DEPO = "depo";


    public static final String URL = "jdbc.url";
    public static final String USERNAME = "jdbc.username";
    public static final String PASSWORD = "jdbc.password";
    public static final String DRIVER = "jdbc.driverClass";


    public static void saveProperties() {
        Properties properties = new Properties();

        properties.setProperty( IASK                , String.valueOf( operationSettings.getType() == Type.IASK ) );
        properties.setProperty( DEPO                , String.valueOf( operationSettings.getType() == Type.DEPO ) );

        properties.setProperty( WIDTH               , String.valueOf( settings().getFrameWidth () ) );
        properties.setProperty( HEIGHT              , String.valueOf( settings().getFrameHeight() ) );

        properties.setProperty( DEST_DIR            , operationSettings.getOutputPathForJavaClasses () );
        properties.setProperty( ENTITY_PACKAGE      , operationSettings.getEntityPackage            () );
        properties.setProperty( INTERFACE_PACKAGE   , operationSettings.getDaoPackage               () );
        properties.setProperty( MAPPING_PACKAGE     , operationSettings.getMapperPackage            () );
        properties.setProperty( MODEL_PACKAGE       , operationSettings.getModelPackage             () );

        if ( new File(  settings().getSourcePath()  ).exists() ) {
            PropertiesFileController.saveSpecificProperties( operationSettings.getSourcePath(), properties );
        }

        properties.setProperty( SOURCE_DIR          , operationSettings.getSourcePath() );

        PropertiesFileController.saveCommonProperties( properties );

    }

    public static void loadSettingsFromProperties( Properties properties ){

        settings().setType(
                Boolean.parseBoolean( properties.getProperty( IASK, "1" ) ) ? Type.IASK : Type.DEPO
        );

        settings().setDaoPackage   ( properties.getProperty( INTERFACE_PACKAGE, settings().getDaoPackage   () ) );
        settings().setEntityPackage( properties.getProperty( ENTITY_PACKAGE,    settings().getEntityPackage() ) );
        settings().setMapperPackage( properties.getProperty( MAPPING_PACKAGE,   settings().getMapperPackage() ) );
        settings().setModelPackage ( properties.getProperty( MODEL_PACKAGE,     settings().getModelPackage () ) );
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
