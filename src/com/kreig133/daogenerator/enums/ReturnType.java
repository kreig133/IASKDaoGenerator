package com.kreig133.daogenerator.enums;

/**
 * @author eshangareev
 * @version 1.0
 */
public enum ReturnType {
    SINGLE, MULTIPLE;

    public static ReturnType getByName( String name ){
        name = name.trim().toLowerCase();

        if( "single".equals( name )){
            return SINGLE;
        }
        if( "multiple".equals( name )){
            return MULTIPLE;
        }
        return null;
    }
}
