package com.kreig133.daogenerator.enums;

/**
 * @author eshangareev
 * @version 1.0
 */
public enum Type  {
    IASK, DEPO;

    public static Type getByName( String name ){
        name = name.trim().toLowerCase();

        for( Type type: Type.values() ){
            if( type.toString().toLowerCase().equals( name ) ){
                return type;
            }
        }

        return null;
    }
}
