package com.kreig133.daogenerator.enums;

/**
 * @author eshangareev
 * @version 1.0
 */
public enum ReturnType {
    SINGLE, MULTIPLE;

    public static ReturnType getByName( String name ){
        name = name.trim().toLowerCase();

        for( ReturnType returnType: ReturnType.values() ){
            if( returnType.toString().toLowerCase().equals( name ) ){
                return returnType;
            }
        }

        return null;
    }
}
