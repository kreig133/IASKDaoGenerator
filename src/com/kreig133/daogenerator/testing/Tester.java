package com.kreig133.daogenerator.testing;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.enums.TestInfoType;

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

            if( functionSettings.getTestInfoType()== TestInfoType.NONE ) return;

            ResultSet resultSet = null;
            ResultSetMetaData metaData = null;
            boolean haveResultSet = false;

            switch ( functionSettings.getSelectType() ){
                case CALL:
                case GENERATE:
                    switch ( functionSettings.getTestInfoType() ){
                        case TQUERY:
                        case TGEN:
                            Statement statement = connection.createStatement();
                            haveResultSet = statement.execute(functionSettings.getQueryForTesting());
//                            statement.execute();
                            if( haveResultSet ){
                                metaData = statement.getResultSet().getMetaData();
                                break;
                            }
                            //Костыль
                            for( int i = 0; i < 100; i ++ ){
                                if( statement.getResultSet() != null  ){
                                    metaData = statement.getResultSet().getMetaData();
                                    break;
                                }
                                statement.getMoreResults();
                            }

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
                            statement.execute( functionSettings.getQueryForTesting() );
                            break;
                        default:
                    }
                    break;
                case SELECT:
                    switch ( functionSettings.getTestInfoType() ){
                        case TQUERY:
                            Statement statement = connection.createStatement();
                            resultSet = statement.executeQuery( functionSettings.getQueryForTesting() );
                            break;
                    }
                    break;
                default:
                    System.out.println(">>>>>>" + functionSettings.getSelectType());
                    throw new IllegalArgumentException("Для этой фигни тестирование еще не готово");
            }

            if( metaData == null ){
                alertError( functionSettings, null );
                return;
            }

            switch ( functionSettings.getSelectType() ){
                case CALL:
                case SELECT:
                case GENERATE:
                case GENEROUT:
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

        if( errors == null ) return;
        
        for( String s: errors ){
            System.out.println(s);
        }
    }

    private static void alertSuccess( FunctionSettings functionSettings ) {
        System.out.println( ">>>> Функция " + functionSettings.getName() + " успешно протестирована!");
    }
}
