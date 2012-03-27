package com.kreig133.daogenerator.files;

import static com.kreig133.daogenerator.common.Utils.stringNotEmpty;

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

        if ( ! stringNotEmpty( name ) ) throw new IllegalArgumentException();

        final char[] chars = name.toCharArray();

        if ( Character.isLowerCase( chars[ 0 ] ) ) {
            if ( chars.length == 1 || Character.isLowerCase( chars[ 1 ] ) ) {
                chars[ 0 ] = Character.toUpperCase( chars[ 0 ] );
                name = new String( chars );
            }
        }

        return name;
    }
}
