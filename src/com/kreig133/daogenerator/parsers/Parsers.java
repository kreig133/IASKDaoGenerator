package com.kreig133.daogenerator.parsers;

import com.kreig133.daogenerator.DaoGenerator;
import com.kreig133.daogenerator.parametr.Parameter;
import com.kreig133.daogenerator.enums.Mode;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class Parsers {

    static List<Parameter> inputParameterList  = DaoGenerator. INPUT_PARAMETER_LIST;
    static List<Parameter> outputParameterList = DaoGenerator.OUTPUT_PARAMETER_LIST;
    static StringBuilder   query               = DaoGenerator.QUERY;



    public static void readLine( Mode mode, String line ){
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
