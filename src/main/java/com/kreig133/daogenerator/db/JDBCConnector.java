package com.kreig133.daogenerator.db;

import com.kreig133.daogenerator.DaoGenerator;
import com.kreig133.daogenerator.common.settings.EmptyOperationSettingsImpl;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.enums.Type;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import static com.kreig133.daogenerator.testing.settings.SettingName.*;

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

        FileInputStream props = null;

        try {
            switch ( DaoGenerator.getCurrentOperationSettings().getType() ) {
                case IASK:
                    props = new FileInputStream( "properties/db/iask.properties" );
                    break;
                case DEPO:
                    props = new FileInputStream( "properties/db/depo.properties" );
                    break;
            }
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
