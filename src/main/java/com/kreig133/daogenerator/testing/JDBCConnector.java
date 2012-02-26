package com.kreig133.daogenerator.testing;

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

    public static Connection connectToDB(
            OperationSettings operationSettings
    ) throws IOException, SQLException {

//        if( connection != null ) return connection;

        FileInputStream props = null;

        switch ( operationSettings.getType() ) {
            case IASK:
                props = new FileInputStream( "properties/db/iask.properties" );
                break;
            case DEPO:
                props = new FileInputStream( "properties/db/depo.properties" );
                break;
        }
        if ( props != null ) {
            properties.load( props );
            props.close();
        } else {
            throw new IOException( "Не удалось загрузить параметры соединения" );
        }

        System.setProperty( "jdbc.driver", properties.getProperty( DRIVER ) );

        Connection connection = DriverManager.getConnection(
                properties.getProperty( URL ),
                properties.getProperty( USERNAME ),
                properties.getProperty( PASSWORD )
        );

        return connection;
    }

    public static void main( String[] args ) throws IOException, SQLException {
        Connection connection = connectToDB( new EmptyOperationSettingsImpl() {
            @Override
            public Type getType() {
                return Type.IASK;
            }
        } );
        CallableStatement statement = connection.prepareCall( "{? = CALL mav_CodAddress ( null, 0 , 2 , 6, null )}" );

        statement.registerOutParameter( 1, Types.OTHER );
//        Statement statement = connection.createStatement();

        statement.execute();
        for ( int i = 0; i < 100; i++ ) {
            if ( statement.getMoreResults() ) {
                final ResultSetMetaData metaData = statement.getResultSet().getMetaData();
//                System.out.println( metaData.getColumnCount() );
                for ( int j = 1; j <= metaData.getColumnCount(); j++ ) {
                    System.out.println(
                            j +
                                    "  -  " +
                                    metaData.getColumnName( j ) +
                                    "  -  " +
                                    metaData.getColumnTypeName( j ) );
                }

            }
        }
//        final boolean execute = statement.execute();
//        ResultSet resultSet = statement.execute( "{CALL mav_CodAddress ( null, 0 , 2 , 6 )}" );
//        statement.execute( "{CALL mav_CodAddress ( null, 0 , 2 , 6, null )}" );
//        statement.execute( "{? = CALL mav_CodAddress ( null, 0 , 2 , 6, null )}" );
        statement.getMoreResults();
        if ( statement.getMoreResults() ) {
            final ResultSetMetaData metaData = statement.getResultSet().getMetaData();
//                System.out.println( metaData.getColumnCount() );
            for ( int i = 1; i <= metaData.getColumnCount(); i++ ) {
                System.out.println(
                        i +
                                "  -  " +
                                metaData.getColumnName( i ) +
                                "  -  " +
                                metaData.getColumnTypeName( i ) );
            }
        }
//                for( int j = 0; j < 10; j++ ){
//                    System.out.println(j);
//                    if( statement.getResultSet() != null  ){
//                        System.out.println( statement.getMoreResults() );
//
//                        break metka;
//                    }
//                }
//        final ResultSetMetaData metaData = resultSet.getMetaData();
//
//        for( int i = 1; i <= metaData.getColumnCount(); i++ ){
//            System.out.println(
//                    metaData.getColumnName( i )+
//                    "    " +
//                    metaData.getColumnTypeName( i ) +
//                    "\n" );
//        }
    }
}
