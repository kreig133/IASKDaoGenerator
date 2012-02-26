package com.kreig133.daogenerator.enums;

/**
 * @author eshangareev
 * @version 1.0
 */
public enum InputOrOutputType {
    IN, OUT;

    public static InputOrOutputType getByName( String name ) {
        for( InputOrOutputType inputOrOutputType : InputOrOutputType.values() ){
            if( inputOrOutputType.toString().equalsIgnoreCase( name.trim() )){
                return inputOrOutputType;
            }
        }
        throw  new IllegalArgumentException();
    }
}
