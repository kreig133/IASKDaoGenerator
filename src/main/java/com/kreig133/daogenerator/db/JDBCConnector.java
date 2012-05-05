package com.kreig133.daogenerator.db;

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
public class JDBCConnector{

    private final Properties properties = new Properties();

    @Nullable
    private Connection connection = null;
    private static String PATH = "db/depo/application.properties";

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
            FileInputStream  props = new FileInputStream( PATH );
            properties.load( props );
            props.close();
        } catch ( IOException e ) {
            throw new RuntimeException( "Не удалось загрузить параметры соединения", e );
        }
    }

    private static JDBCConnector INSTANCE;

    private JDBCConnector() {
    }

    public synchronized static JDBCConnector instance(){
        if ( INSTANCE == null ) {
            INSTANCE = new JDBCConnector();
        }
        return INSTANCE;
    }
}
