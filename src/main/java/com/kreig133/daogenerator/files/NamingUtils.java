package com.kreig133.daogenerator.files;

import org.apache.commons.lang.StringUtils;

/**
 * @author eshangareev
 * @version 1.0
 */
public class NamingUtils {
    public static String convertNameForClassNaming( String name ) {
        final char[] chars = name.toCharArray();
        chars[ 0 ] = Character.toUpperCase( chars[ 0 ] );
        return new String( chars );
    }

    public static String convertNameForNonClassNaming( String name ) {
        final char[] chars = name.toCharArray();
        chars[ 0 ] = Character.toLowerCase( chars[ 0 ] );
        return new String( chars );
    }

    /**
     * Конвертирует имя для использования в геттерах и сеттерах
     * @param name
     * @return
     */
    public static String convertNameForGettersAndSetters( String name ) {

        if ( StringUtils.isEmpty( name ) ) throw new IllegalArgumentException();

        final char[] chars = name.toCharArray();

        if ( Character.isLowerCase( chars[ 0 ] ) ) {
            if ( chars.length == 1 || Character.isLowerCase( chars[ 1 ] ) ) {
                chars[ 0 ] = Character.toUpperCase( chars[ 0 ] );
                name = new String( chars );
            }
        }

        return name;
    }

    public static String convertNameForEnum( String name ) {
        StringBuilder builder = new StringBuilder();
        char[] chars = name.toCharArray();
        for(  int i = 0 ; i< chars.length; i ++ ) {
            if ( Character.isUpperCase( chars[ i ] ) ) {
                if ( i > 0 && !Character.isUpperCase( chars[ i - 1 ] ) ) {
                    builder.append( "_" );
                }
            }
            builder.append( chars[ i ] );
        }
        return builder.toString().toUpperCase();
    }
}
