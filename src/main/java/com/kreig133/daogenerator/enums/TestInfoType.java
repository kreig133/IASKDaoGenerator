package com.kreig133.daogenerator.enums;

/**
 * @author eshangareev
 * @version 1.0
 */
public enum TestInfoType {
    TQUERY, TPARAM, NONE, TGEN;

    public static TestInfoType getByName( String name ){
        for( TestInfoType infoType: TestInfoType.values() ){
            if(infoType.toString().toLowerCase().equals( name.trim().toLowerCase() )){
                return infoType;
            }
        }
        throw new IllegalArgumentException();
    }
}
