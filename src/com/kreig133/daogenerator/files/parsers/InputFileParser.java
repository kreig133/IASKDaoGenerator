package com.kreig133.daogenerator.files.parsers;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.enums.Mode;
import com.kreig133.daogenerator.enums.ReturnType;
import com.kreig133.daogenerator.enums.SelectType;
import com.kreig133.daogenerator.enums.TestInfoType;
import com.kreig133.daogenerator.files.parsers.strategy.Action;

import java.io.*;

import static com.kreig133.daogenerator.common.Utils.*;
import static com.kreig133.daogenerator.files.parsers.settings.SettingsReader.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class InputFileParser {

    private static Mode mode;

    public static void readFileWithDataForGenerateDao(
            File fileWithData,
            OperationSettings operationSettings,
            FunctionSettings functionSettings
    ) throws IOException {

        final BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(fileWithData),
                        "CP1251"
                )
        );

        String line = reader.readLine();

        //Считываем настройки
        readType( line, operationSettings, functionSettings );

        line = reader.readLine();

        while (line != null) {
            try{

                if( ! isStopLine( line ) ){
                    if(
                            mode != null &&
                                    (
                                            mode == Mode.IS_SELECT_QUERY  ||
                                            mode == Mode.IS_TESTING_QUERY ||
                                            line.length() > 7
                                    )
                    ) {
                        Parsers.readLine( operationSettings, functionSettings, mode, line );
                    }
                }

                line = reader.readLine();

            } catch (ArrayIndexOutOfBoundsException ex){
                System.out.println(line);
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }
    }

    private static void readType(
            String      lineWithSettings,
            OperationSettings operationSettings,
            FunctionSettings functionSettings
    ){
        final String[] split = splitIt( lineWithSettings );

        setParameter( split, SELECT, operationSettings, functionSettings, new Action() {
            @Override
            public void doAction( String[] strings, Integer place, OperationSettings operationSettings,
                                  FunctionSettings functionSettings ) {
                functionSettings.setSelectType ( SelectType.getByName( split[ place ] ) ) ;
            }
        } );
        setParameter( split, RETURN, operationSettings, functionSettings, new Action() {
            @Override
            public void doAction( String[] strings, Integer place, OperationSettings operationSettings, FunctionSettings functionSettings ) {
                 functionSettings.setReturnType ( ReturnType.getByName( split[ place ] ) );
            }
        } );

        setParameter( split, TEST, operationSettings, functionSettings, new Action() {
            @Override
            public void doAction( String[] strings, Integer place, OperationSettings operationSettings, FunctionSettings functionSettings ) {
                functionSettings.setTestInfoType( TestInfoType.getByName( split[ place ] ) );
            }
        } );
    }

    private static void setParameter(
            String[] strings,
            String key,
            OperationSettings operationSettings,
            FunctionSettings functionSettings,
            Action action
    ){
        Integer placeOfParameter = operationSettings.getPlaceOfParameter( key );
        if( placeOfParameter != null  ){
            ParsersUtils.checkPlaceOfParameter( false, strings, placeOfParameter );
            action.doAction( strings, placeOfParameter ,operationSettings, functionSettings );
        }
    }



    private static boolean isStopLine( String line ){
        for( String s : splitIt( line ) ){
            if( Mode.getByName( s ) != null ){
                mode = Mode.getByName( s );
                return  true;
            }
        }
        return  false;
    }
}
