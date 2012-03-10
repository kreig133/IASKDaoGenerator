package com.kreig133.daogenerator.enums;

import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.old_version_converter.parsers.*;
import com.kreig133.daogenerator.jaxb.DaoMethod;

/**
 * @author eshangareev
 * @version 1.0
 */
public enum Mode {
    IS_INPUT_PARAMETRS ( new InputParameterParser() ),
    IS_OUTPUT_PARAMETRS( new OutputParametrParser() ),
    IS_SELECT_QUERY    ( new SelectQueryParser   () ),
    IS_COMMENTARY      ( new CommentParser       () );


    Mode( IParser parser ) {
        this.parser = parser;
    }

    private final IParser parser;

    @SuppressWarnings( { "unchecked" } )
    public void parse(
            DaoMethod daoMethod,
            String lineForParse
    ){
        parser.parse( daoMethod, lineForParse );
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
