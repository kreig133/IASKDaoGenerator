package com.kreig133.daogenerator.testing;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.settings.OperationSettings;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class Tester {

    public static void startFunctionTesting( OperationSettings operationSettings, FunctionSettings functionSettings ){
        try {
            Connection connection = JDBCConnector.connectToDB( operationSettings );

            ResultSet resultSet = null;

            switch ( functionSettings.getSelectType() ){
                case CALL:
                    switch ( functionSettings.getTestInfoType() ){
                        case TQUERY:
                            CallableStatement statement = connection.prepareCall(functionSettings.getQueryForTesting());
                            statement.registerOutParameter( 1, Types.OTHER );
                            resultSet = statement.executeQuery();
                            break;
                        default:
                    }
                    break;
                case DELETE:
                case INSERT:
                case UPDATE:
                    switch ( functionSettings.getTestInfoType() ){
                        case TQUERY:
                            Statement statement = connection.createStatement();
                            statement.execute(functionSettings.getQueryForTesting());
                            break;
                        default:
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Для этой фигни тестирование еще не готово");
            }

            switch ( functionSettings.getSelectType() ){
                case CALL:
                case SELECT:
                case GENERATE:
                case GENEROUT:
                    final ResultSetMetaData metaData = resultSet.getMetaData();
                    final List<String> errors = TypeAndNameComparator.compare( metaData, functionSettings );
                    if( errors.isEmpty() ){
                        alertSuccess( functionSettings );
                    } else {
                        alertError( functionSettings, errors );
                    }
                    break;
                case DELETE:
                case INSERT:
                case UPDATE:
                    alertSuccess( functionSettings );
            }
//            final Statement statement = connection.createStatement();
//            statement.setEscapeProcessing( true );
//             statement.executeQuery( functionSettings.getQueryForTesting() );
////            final boolean execute = statement.execute( functionSettings.getQueryForTesting() );
////            if ( execute ){
//                final SQLWarning warnings = statement.getWarnings();
//                final ResultSet resultSet = statement.getResultSet();
//                TypeAndNameComparator.compare( resultSet.getMetaData(), functionSettings );
////            } else {
////
////            }
        } catch ( IOException e ) {
            //TODO

            e.printStackTrace();
        } catch ( SQLException e ) {
            //TODO собирать сообщения об ошибках
            alertError( functionSettings, new ArrayList<String>() );
            e.printStackTrace();
        }
    }

    private static void alertError( FunctionSettings functionSettings, List<String> errors ) {
        System.out.println( ">>>> Проверка функции " + functionSettings.getName() + " провалена.");
        for( String s: errors ){
            System.out.println(s);
        }
    }

    private static void alertSuccess( FunctionSettings functionSettings ) {
        System.out.println( ">>>> Функция " + functionSettings.getName() + " успешно протестирована!");
    }
}
