package com.kreig133.daogenerator.db;

import org.jetbrains.annotations.Nullable;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
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
    private static String PATH = "application.properties";

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
        
        try {
        	DriverManager.registerDriver ((Driver) Class.forName(properties.getProperty( DRIVER )).newInstance());
        } catch(Exception e) {
        	throw new RuntimeException( "Не удалось подключить драйвер СУБД", e );
        }
        System.setProperty( "jdbc.driver", properties.getProperty( DRIVER ) );

        try {
            connection = DriverManager.getConnection(
                    properties.getProperty( URL ),
                    properties.getProperty( USERNAME ),
                    properties.getProperty( PASSWORD )
            );
            connection.createStatement().execute( "SET NOCOUNT ON;" );
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
