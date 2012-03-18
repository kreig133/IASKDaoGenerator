package com.kreig133.daogenerator.db;

import com.kreig133.daogenerator.DaoGenerator;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import static com.kreig133.daogenerator.settings.SettingName.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class JDBCConnector {

    private static final Properties properties = new Properties();

    private static Connection connection = null;

    public static Connection connectToDB() {

        try {
            if ( connection != null && !connection.isClosed() ) {
                return connection;
            }
        } catch ( SQLException e ) {
            e.printStackTrace();
        }

        FileInputStream props;

        try {
            props = new FileInputStream( DaoGenerator.settings().getType().pathToProperty() );
            properties.load( props );
            props.close();
        } catch ( IOException e ) {
            throw new RuntimeException( "Не удалось загрузить параметры соединения", e );
        }

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
}
