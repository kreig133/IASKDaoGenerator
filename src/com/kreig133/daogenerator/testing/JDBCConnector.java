package com.kreig133.daogenerator.testing;

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
    private static String pathToProperties ="./src/com/kreig133/daogenerator/testing/settings/";

    private static Connection connection;

    public static void connectToDB(
            OperationSettings operationSettings
    ) throws IOException, SQLException {

        if( connection != null ) throw  new AssertionError( "Соединение с базой уже было установлено!" );

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
    }

    public static void main( String[] args ) throws IOException, SQLException {
        connectToDB( new OperationSettings() {
            @Override
            public Type getType() {
                return Type.DEPO;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void setType( Type type ) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getOperationName() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void setOperationName( String operationName ) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getOutputPath() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void setOutputPath( String outputPath ) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getEntityPackage() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void setEntityPackage( String entityPackage ) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getMapperPackage() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void setMapperPackage( String mapperPackage ) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getDaoPackage() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void setDaoPackage( String daoPackage ) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getSourcePath() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void setSourcePath( String path ) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean skipTesting() {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void setSkipTesting( boolean skipTesting ) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        } );
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery( "SELECT * FROM DepoType" );

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
