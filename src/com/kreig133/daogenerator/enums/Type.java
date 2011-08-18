package com.kreig133.daogenerator.enums;

/**
 * @author eshangareev
 * @version 1.0
 */
public enum Type  {
    IASK, DEPO;

    public static Type getByName( String name ){
        name = name.trim().toLowerCase();

        if( "iask".equals( name ) ){
            return IASK;
        }

        if( "depo".equals( name ) ){
            return DEPO;
        }
        
        return null;
    }
}
