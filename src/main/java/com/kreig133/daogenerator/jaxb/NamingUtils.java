package com.kreig133.daogenerator.jaxb;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * @author eshangareev
 * @version 1.0
 */
public class NamingUtils {
    @NotNull
    public static String convertNameForClassNaming( @NotNull String name ) {
        final char[] chars = name.toCharArray();
        chars[ 0 ] = Character.toUpperCase( chars[ 0 ] );
        return new String( chars );
    }

    @NotNull
    public static String convertNameForNonClassNaming( @NotNull String name ) {
        final char[] chars = name.toCharArray();
        chars[ 0 ] = Character.toLowerCase( chars[ 0 ] );
        return new String( chars );
    }

    /**
     * Конвертирует имя для использования в геттерах и сеттерах
     * @param name
     * @return
     */
    @NotNull
    public static String convertNameForGettersAndSetters( @NotNull String name ) {

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

    public static String convertNameForEnum( @NotNull String name ) {
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
