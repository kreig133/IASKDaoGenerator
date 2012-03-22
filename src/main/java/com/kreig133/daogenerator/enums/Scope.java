package com.kreig133.daogenerator.enums;

/**
 * @author eshangareev
 * @version 1.0
 */
public enum Scope {
    PUBLIC("public"), 
    PROTECTED("protected"), 
    DEFAULT(""), 
    PRIVATE("private");
    
    String value;

    private Scope( String value ) {
        this.value = value;
    }

    public String value(){
        return value;
    }
}
