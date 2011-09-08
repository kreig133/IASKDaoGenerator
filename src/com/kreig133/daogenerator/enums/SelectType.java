package com.kreig133.daogenerator.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author eshangareev
 * @version 1.0
 */
public enum SelectType {
    CALL    ("Select"),
    SELECT  ("Select"),
    GENERATE("Select"),
    GENEROUT("Select"),
    INSERT  ("Insert"),
    DELETE  ("Delete"),
    UPDATE  ("Update");

    private final String annotation;

    public String getAnnotation() {
        return annotation;
    }

    SelectType( String annotation ) {
        this.annotation = annotation;
    }

    public static SelectType getByName( String name ){
        name = name.trim().toLowerCase();
        for( SelectType selectType: SelectType.values() ){
            if( selectType.toString().toLowerCase().equals( name ) ){
                return selectType;
            }
        }

        return null;
    }

    public static boolean isQuery( SelectType selectType ){
        return
                selectType == SELECT ||
                selectType == INSERT ||
                selectType == DELETE ||
                selectType == UPDATE;
    }
}
