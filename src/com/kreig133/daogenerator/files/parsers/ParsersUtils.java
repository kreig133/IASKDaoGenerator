package com.kreig133.daogenerator.files.parsers;

/**
 * @author eshangareev
 * @version 1.0
 */
public class ParsersUtils {
    public static void checkPlaceOfParameter( boolean required, int paramListLength, Integer placeOfParameter ){
        if( ! ( placeOfParameter != null && paramListLength > placeOfParameter ) ){
            throw new AssertionError( "Ошибка! Не правильно задан place of parameter" );
        }
        if( required && placeOfParameter == null ){
            throw new AssertionError( "Ошибка! Обязательный place of parameter не задан!" );
        }
    }
}
