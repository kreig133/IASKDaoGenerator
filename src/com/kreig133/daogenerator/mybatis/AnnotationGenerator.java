package com.kreig133.daogenerator.mybatis;

import com.kreig133.daogenerator.enums.SelectType;

/**
 * @author eshangareev
 * @version 1.0
 */
public class AnnotationGenerator {
    public static String generateAnnotation( SelectType selectType ){
        StringBuilder builder = new StringBuilder(  );

        switch ( selectType ){
            case CALL:
                break;
            case GENERATE:
                break;
            case SELECT:
                break;
        }
    }
}
