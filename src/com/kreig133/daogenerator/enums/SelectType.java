package com.kreig133.daogenerator.enums;

/**
 * @author eshangareev
 * @version 1.0
 */
public enum SelectType {
    CALL, SELECT, GENERATE;

    public static SelectType getByName( String name ){
        name = name.trim().toLowerCase();

        if( "call".equals( name ) ){
            return CALL;
        }
        if( "select".equals( name ) ){
            return SELECT;
        }
        if( "generate".equals( name ) ){
            return GENERATE;
        }

        return null;
    }
}
