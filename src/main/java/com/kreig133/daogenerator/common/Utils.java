package com.kreig133.daogenerator.common;

import com.kreig133.daogenerator.jaxb.ParameterType;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

/**
 * @author eshangareev
 * @version 1.0
 */
public class Utils {

    public static boolean stringContainsMoreThanOneWord( String text ) {
        return ( text.split( "\\s+" ).length > 1 );
    }

    public static String streamToString( InputStream stream ) {
        try{
            return  new Scanner( stream ).useDelimiter( "\\A" ).next();
        } finally {
            try {
                stream.close();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }

    public static StringBuilder insertTabs( StringBuilder builder, int tabsQuantity ){
        for( int i = 0 ; i < tabsQuantity; i ++ ){
            builder.append( "    " );
        }
        return builder;
    }


    /**
     * Пытается переделать имя в Java-style
     * @param nameForCall
     * @return
     */
    public static String convertPBNameToName( String nameForCall ) {
        if( StringUtils.isEmpty( nameForCall ) ) {
            return "";
        }
        {
            final char[] chars = nameForCall.toCharArray();
            chars[ 0 ] = Character.toLowerCase( chars[ 0 ] );
            nameForCall = new String( chars );
        }
        StringBuilder builder = new StringBuilder();

        final String[] split = nameForCall.split( "_" );
        builder.append( split[ 0 ] );
        for( int i = 1; i < split.length ; i++ ){
            final char[] chars = split[ i ].toCharArray();
            chars[ 0 ] = Character.toUpperCase( chars[ 0 ] );
            builder.append( new String( chars ) );
        }

        return builder.toString();
    }

    public static File getFileFromDirectoryByName( String directoryPath, String fileName ) {
        return new File( new File( directoryPath ).getAbsolutePath() + "/" + fileName );
    }

    public static void iterateForParameterList(
            StringBuilder builder,
            List<ParameterType> parameterList,
            FuctionalObject functionalObject
    ) {
        iterateForParameterList( builder, parameterList, 1, functionalObject );
    }

    public static void iterateForParameterList(
            StringBuilder builder,
            List<ParameterType> parameterList,
            int tabs,
            FuctionalObject functionalObject

    ) {
        boolean first = true;
        for ( ParameterType p : parameterList ) {
            if ( functionalObject.filter( p ) ) {
                if ( ! first ) {
                    builder.append( "," );
                    builder.append( "\n" );
                } else {
                    first = false;
                }
                insertTabs( builder, tabs );
                functionalObject.writeString( builder, p );
            }
        }
        builder.append( "\n" );
    }

}
