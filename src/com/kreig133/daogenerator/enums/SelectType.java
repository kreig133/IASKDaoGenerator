package com.kreig133.daogenerator.enums;

/**
 * @author eshangareev
 * @version 1.0
 */
public enum SelectType {
    CALL("Select"), SELECT("Select"), GENERATE("Select"), INSERT("Insert"), DELETE("Delete"), UPDATE("Update");

    private String annotation;

    public String getAnnotation() {
        return annotation;
    }

    SelectType( String annotation ) {
        this.annotation = annotation;
    }

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
        if( "insert".equals( name ) ){
            return INSERT;
        }
        if( "delete".equals( name ) ){
            return DELETE;
        }
        if( "update".equals( name ) ){
            return UPDATE;
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
