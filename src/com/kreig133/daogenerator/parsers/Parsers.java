package com.kreig133.daogenerator.parsers;

import com.kreig133.daogenerator.DaoGenerator;
import com.kreig133.daogenerator.Settings;
import com.kreig133.daogenerator.parametr.Parameter;
import com.kreig133.daogenerator.enums.Mode;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class Parsers {

    public static void readLine(
            Settings settings,
            Mode mode,
            String line
    ){
        final List<Parameter> inputParameterList  = settings. getInputParameterList();
        final List<Parameter> outputParameterList = settings.getOutputParameterList();
        final StringBuilder   query               = settings.getSelectQuery        ();

        switch ( mode ){
            case IS_INPUT_PARAMETRS:
                mode.parse( inputParameterList, line );
                break;
            case IS_OUTPUT_PARAMETRS:
                mode.parse( outputParameterList, line );
                break;
            case IS_SELECT_QUERY:
                mode.parse( query, line );
                break;
        }
    }
}
