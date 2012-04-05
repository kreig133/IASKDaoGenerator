package com.kreig133.daogenerator.db;

import com.kreig133.daogenerator.common.TypeChangeListener;
import com.kreig133.daogenerator.settings.Settings;
import org.jetbrains.annotations.Nullable;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static com.kreig133.daogenerator.settings.Settings.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class JDBCConnector implements TypeChangeListener{

    private final Properties properties = new Properties();

    @Nullable
    private Connection connection = null;

    @Nullable
    public Connection connectToDB() {

        try {
            if ( connection != null && ! connection.isClosed() ) {
                return connection;
            }
        } catch ( SQLException e ) {
            e.printStackTrace();
        }

        loadProperties();

        System.setProperty( "jdbc.driver", properties.getProperty( DRIVER ) );

        try {
            connection = DriverManager.getConnection(
                    properties.getProperty( URL ),
                    properties.getProperty( USERNAME ),
                    properties.getProperty( PASSWORD )
            );
        } catch ( SQLException e ) {
            throw new RuntimeException( "Не удалось подключиться к базе", e );
        }

        return connection;
    }

    private void loadProperties() {
        try {
            FileInputStream  props = new FileInputStream( Settings.settings().getType().pathToProperty() );
            properties.load( props );
            props.close();
        } catch ( IOException e ) {
            throw new RuntimeException( "Не удалось загрузить параметры соединения", e );
        }
    }

    private static JDBCConnector INSTANCE;

    public static JDBCConnector instance(){
        if ( INSTANCE == null ) {
            INSTANCE = new JDBCConnector();
            Settings.settings().addTypeChangeListener( INSTANCE );
        }
        return INSTANCE;
    }

    @Override
    public void typeChanged() {
        connection = null;
    }
}
