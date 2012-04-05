package com.kreig133.daogenerator.enums;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author eshangareev
 * @version 1.0
 */
public enum Type  {
    IASK, DEPO, TEST;

    @NotNull
    public String pathToProperty() {
        if ( this == TEST ) {
            return "target/db/depo/application.properties";
        }

        return "db/" + this.toString().toLowerCase() + "/application.properties";
    }
    
    @Nullable
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
