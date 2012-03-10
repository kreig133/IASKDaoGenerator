package com.kreig133.old_version_converter.parsers;

import com.kreig133.daogenerator.DaoGenerator;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.enums.Mode;
import com.kreig133.daogenerator.enums.TestInfoType;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.SelectType;
import com.kreig133.old_version_converter.parsers.strategy.Action;
import sun.reflect.generics.tree.ReturnType;

import java.io.*;

import static com.kreig133.old_version_converter.parsers.settings.SettingsReader.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class InputFileParser {

    private static Mode mode;

    public static void readFileWithDataForGenerateDao(
            File fileWithData,
            DaoMethod daoMethod
    ) throws IOException {

        final BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(fileWithData),
                        "CP1251"
                )
        );

        String line = reader.readLine();

        //Считываем настройки
        readType( line, daoMethod );

        line = reader.readLine();

        while (line != null) {
            try{

                if( ! isStopLine( line ) ){
                    if(
                            mode != null &&
                                    (
                                            mode == Mode.IS_SELECT_QUERY  ||
                                            line.length() > 7
                                    )
                    ) {
                        mode.parse( daoMethod, line );
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
            DaoMethod   daoMethod
    ){
        final String[] split = lineWithSettings.split( " " );

        setParameter( split, SELECT, daoMethod, new Action() {
            @Override
            public void doAction( String[] strings, Integer place, DaoMethod daoMethod ) {
                daoMethod.getCommon().getConfiguration().setType( SelectType.getByName( split[ place ] ) ); ;
            }
        } );
        setParameter( split, RETURN, daoMethod, new Action() {
            @Override
            public void doAction( String[] strings, Integer place, DaoMethod daoMethod ) {
                 daoMethod.getCommon().getConfiguration().setMultipleResult(
                         split[ place ].toLowerCase().equals( "multiple" )
                 );
            }
        } );
    }

    private static void setParameter(
            String[] strings,
            String key,
            DaoMethod daoMethod,
            Action action
    ){
        Integer placeOfParameter = DaoGenerator.getCurrentOperationSettings().getPlaceOfParameter( key );
        if( placeOfParameter != null  ){
            ParsersUtils.checkPlaceOfParameter( false, strings, placeOfParameter );
            action.doAction( strings, placeOfParameter , daoMethod );
        }
    }


    private static boolean isStopLine( String line ){
        for( String s : line.split( " " ) ){
            if( Mode.getByName( s ) != null ){
                mode = Mode.getByName( s );
                return  true;
            }
        }
        return  false;
    }
}
