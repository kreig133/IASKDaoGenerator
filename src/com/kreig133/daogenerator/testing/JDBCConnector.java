package com.kreig133.daogenerator.testing;

import com.kreig133.daogenerator.common.settings.EmptyOperationSettingsImpl;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.enums.Type;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Map;
import java.util.Properties;

import static com.kreig133.daogenerator.testing.settings.SettingName.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class JDBCConnector {
    private static final Properties properties = new Properties();
    private static String pathToProperties ="./src/com/kreig133/daogenerator/testing/settings/";
    private static Connection connection;


    public static Connection connectToDB(
            OperationSettings operationSettings
    ) throws IOException, SQLException {

        if( connection != null ) return connection;

        FileInputStream props = null;

        switch ( operationSettings.getType() ){
            case IASK:
                props = new FileInputStream( pathToProperties + "iask.properties" );
                break;
            case DEPO:
                props = new FileInputStream( pathToProperties + "depo.properties" );
                break;
        }
        if( props != null ){
            properties.load( props );
            props.close();
        } else {
            throw new IOException( "Не удалось загрузить параметры соединения" );
        }

        System.setProperty( "jdbc.driver", properties.getProperty( DRIVER ) );

        connection =  DriverManager.getConnection(
                properties.getProperty( URL ),
                properties.getProperty( USERNAME ),
                properties.getProperty( PASSWORD )
        );

        return connection;
    }

    public static void main( String[] args ) throws IOException, SQLException {
        Connection connection = connectToDB( new EmptyOperationSettingsImpl(){
            @Override
            public Type getType() {
                return Type.IASK;
            }
        });
        CallableStatement statement = connection.prepareCall( "{? = CALL ychGetNumDivision(4, null, 1)}" );

        statement.registerOutParameter( 1, Types.OTHER );

        ResultSet resultSet = statement.executeQuery();
        final ResultSetMetaData metaData = resultSet.getMetaData();

        for( int i = 1; i <= metaData.getColumnCount(); i++ ){
            System.out.println(
                    metaData.getColumnName( i )+
                    "    " +
                    metaData.getColumnTypeName( i ) +
                    "\n" );
        }
    }
}
