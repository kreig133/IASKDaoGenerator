package com.kreig133.daogenerator.enums;

import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.files.parsers.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public enum Mode {
    IS_INPUT_PARAMETRS ( new InputParameterParser() ),
    IS_OUTPUT_PARAMETRS( new OutputParametrParser() ),
    IS_SELECT_QUERY    ( new SimpleParser        () ),
    IS_TESTING_QUERY   ( new TestParser          () ),
    IS_COMMENTARY      ( new SimpleParser        () );


    Mode( IParser parser ) {
        this.parser = parser;
    }

    private final IParser parser;

    @SuppressWarnings( { "unchecked" } )
    public void parse( OperationSettings operationSettings, Object input, String lineForParse ){
        parser.parse( operationSettings, input, lineForParse );
    }


    public static Mode getByName( String name ){
        name = name.trim().toLowerCase();

        for( Mode mode: Mode.values() ){
            if( mode.toString().toLowerCase().equals( name ) ){
                return mode;
            }
        }

        return null;
    }
}
