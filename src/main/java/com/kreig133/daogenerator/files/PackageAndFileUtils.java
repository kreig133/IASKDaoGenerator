package com.kreig133.daogenerator.files;

import java.io.File;

/**
 * @author eshangareev
 * @version 1.0
 */
public class PackageAndFileUtils {
    public static String getShortName( String fullJavaClassName ) {
        return fullJavaClassName.substring( fullJavaClassName.lastIndexOf( '.' ) + 1 );
    }

    public static String getPackage( String fullJavaClassName ) {
        return fullJavaClassName.substring( 0, fullJavaClassName.lastIndexOf( '.' ) );
    }

    public static void createDirsAndFile( File file ) {
        if(!file.exists()){
            file.mkdirs();
        }
    }

    public static String replacePointBySlash( String string ){
        if( string != null ){
            return string.replace( '.', '/' );
        }
        return null;
    }
}
