package com.kreig133.daogenerator;

import com.sun.java.browser.plugin2.liveconnect.v1.Result;

/**
 * @author eshangareev
 * @version 1.0
 */
public class Utils {
    public static String getJavaDocString(String[] commentsLine){
        StringBuilder result = new StringBuilder();
        result.append("\t/**\n" );
        for( String comment : commentsLine ){
            result.append( "\t * " );
            result.append( comment );
            result.append( "\n"    );
        }
        result.append("\t */\n");

        return  result.toString();
    }

    public static String convertNameForGettersAndSetters ( String name ){
        if ( name == null || "".equals( name ) ) throw  new IllegalArgumentException();

        String newName = name;
        final char[] chars = name.toCharArray();
        if ( Character.isLowerCase( chars[ 0 ] ) ){
            if( chars.length == 1 || Character.isLowerCase( chars[ 1 ] ) ){
                chars[ 0 ] = Character.toUpperCase( chars [0] );
                name = new String( chars );
            }
        }

        return name;

    }

    public static String convertNameForClassNaming(String name){
        final char[] chars = name.toCharArray();
        chars[0] = Character.toUpperCase( chars[ 0 ] );
        return new String( chars );
    }

    public static String handleDefaultValue(String string){
        if( string == null ) return string;
        final char[] chars = string.toCharArray();
        for(int i = 0; i < chars.length; i++){
            if( (chars[i] == '\'' )|| (chars[i] == '‘')  || (chars[i] == '’')){
                chars[i] = '\"';
            }
        }

        return new String(chars);
    }
}
