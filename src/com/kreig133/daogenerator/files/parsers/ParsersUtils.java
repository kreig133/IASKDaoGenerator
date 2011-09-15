package com.kreig133.daogenerator.files.parsers;

/**
 * @author eshangareev
 * @version 1.0
 */
public class ParsersUtils {
    public static void checkPlaceOfParameter( boolean required, String[] params, Integer placeOfParameter ){
        if( ! ( placeOfParameter != null && params.length > placeOfParameter ) ){
            System.out.println( " >>>>Текущие значения:" );
            for( int i = 0; i < params.length; i++ ){
                System.out.println( ">>>>>>    " + i + " - " + params[ i ] );
            }
            System.out.println( ">>>>>>>   placeOfParameter - " + placeOfParameter );
            throw new AssertionError( "Ошибка! Не правильно задан place of parameter" );
        }
        if( required && placeOfParameter == null ){
            throw new AssertionError( "Ошибка! Обязательный place of parameter не задан!" );
        }
    }
}
