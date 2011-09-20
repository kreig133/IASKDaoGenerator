package com.kreig133.daogenerator.enums;

/**
 * @author eshangareev
 * @version 1.0
 */
public enum ClassType {
    Class("class"), Interface("interface");

    private String inJava;

    ClassType( String inJava ){
        this.inJava = inJava;
    }

    @Override
    public String toString() {
        return inJava;
    }
}
