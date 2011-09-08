package com.kreig133.daogenerator.parsers;

import com.kreig133.daogenerator.DaoGenerator;
import com.kreig133.daogenerator.enums.Mode;
import com.kreig133.daogenerator.enums.ReturnType;
import com.kreig133.daogenerator.enums.SelectType;
import com.kreig133.daogenerator.enums.Type;

import java.io.*;

import static com.kreig133.daogenerator.common.Utils.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class InputFileParser {

    private static Mode mode;

    public static void readFileWithDataForGenerateDao(
            File fileWithData
    ) throws IOException {

        final BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(fileWithData),
                        "CP1251"
                )
        );

        String line = reader.readLine();

        //Считываем настройки
        readType( line );

        line = reader.readLine();

        while (line != null) {
            try{

                if( ! isStopLine( line ) ){
                    if( mode != null && ( mode == Mode.IS_SELECT_QUERY || line.length() > 7 ) ){
                        Parsers.readLine( DaoGenerator.instance(), mode, line );
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

    private static void readType( String lineWithSettings ){
        final String[] split = splitIt( lineWithSettings );

        assert split.length >= 3;

        DaoGenerator.setTYPE        ( Type      .getByName( split[ 0 ] ) );
        DaoGenerator.setSELECT_TYPE ( SelectType.getByName( split[ 1 ] ) ) ;
        DaoGenerator.setRETURN_TYPE ( ReturnType.getByName( split[ 2 ] ) );
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
