package com.kreig133.daogenerator.common;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class Utils {
    public static SimpleDateFormat getDaoGeneratorDateFormat() {
        return new SimpleDateFormat( getDaoGeneratorDateFormatString() );
    }

    private static String getDaoGeneratorDateFormatString() {
        return "M-d-yyyy H:m:s.SSS";
    }

    public static boolean stringContainsMoreThanOneWord( @NotNull String text ) {
        return ( text.split( "\\s+" ).length > 1 );
    }

    @Nullable
    public static String streamToString( @NotNull InputStream stream ) {
        try {
            try {
                return IOUtils.toString( stream );
            } finally {
                stream.close();
            }
        } catch ( IOException e ) {
            e.printStackTrace();
            return null;
        }
    }

    @NotNull
    public static StringBuilder insertTabs( @NotNull StringBuilder builder, int tabsQuantity ){
        for( int i = 0 ; i < tabsQuantity; i ++ ){
            builder.append( "\t" );
        }
        return builder;
    }


    /**
     * Пытается переделать имя в Java-style
     * @param nameForCall
     * @return
     */
    public static String convertPBNameToName( @NotNull String nameForCall ) {
        if( StringUtils.isBlank( nameForCall ) ) {
            return "";
        }
        if ( StringUtils.isAllUpperCase( StringUtils.join( nameForCall.trim().split( "_+" ) ) ) ) {
            nameForCall =  nameForCall.toLowerCase();
        }
        {
            final char[] chars = nameForCall.toCharArray();
            chars[ 0 ] = Character.toLowerCase( chars[ 0 ] );
            nameForCall = new String( chars );
        }
        StringBuilder builder = new StringBuilder();

        final String[] split = nameForCall.split( "_+" );
        builder.append( split[ 0 ] );
        for( int i = 1; i < split.length ; i++ ){
            final char[] chars = split[ i ].toCharArray();
            chars[ 0 ] = Character.toUpperCase( chars[ 0 ] );
            builder.append( new String( chars ) );
        }

        return builder.toString();
    }

    @NotNull
    public static File getFileFromDirectoryByName( String directoryPath, String fileName ) {
        return new File( new File( directoryPath ).getAbsolutePath() + "/" + fileName );
    }

    public static boolean collectionNotEmpty( List inputParams ) {
        return inputParams != null && ! inputParams.isEmpty();
    }
}
