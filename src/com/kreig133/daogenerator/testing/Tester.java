package com.kreig133.daogenerator.testing;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.settings.OperationSettings;

import java.io.IOException;
import java.sql.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class Tester {

    public static void startFunctionTesting( OperationSettings operationSettings, FunctionSettings functionSettings ){
        try {
            Connection connection = JDBCConnector.connectToDB( operationSettings );

            final Statement statement = connection.createStatement();
            statement.setEscapeProcessing( true );
             statement.executeQuery( functionSettings.getQueryForTesting() );
//            final boolean execute = statement.execute( functionSettings.getQueryForTesting() );
//            if ( execute ){
                final SQLWarning warnings = statement.getWarnings();
                final ResultSet resultSet = statement.getResultSet();
                TypeAndNameComparator.compare( resultSet.getMetaData(), functionSettings );
//            } else {
//
//            }
        } catch ( IOException e ) {
            //TODO
            e.printStackTrace();
        } catch ( SQLException e ) {
            //TODO собирать сообщения об ошибках
            e.printStackTrace();
        }

    }
}
