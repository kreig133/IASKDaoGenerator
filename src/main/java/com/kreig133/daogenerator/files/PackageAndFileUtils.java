package com.kreig133.daogenerator.files;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * @author eshangareev
 * @version 1.0
 */
public class PackageAndFileUtils {
    public static String getShortName( @NotNull String fullJavaClassName ) {
        return fullJavaClassName.substring( fullJavaClassName.lastIndexOf( '.' ) + 1 );
    }

    public static String getPackage( @NotNull String fullJavaClassName ) {
        return fullJavaClassName.substring( 0, fullJavaClassName.lastIndexOf( '.' ) );
    }

    public static void createDirsAndFile( @NotNull File file ) {
        if(!file.exists()){
            file.mkdirs();
        }
    }

    @Nullable
    public static String replacePointBySlash( @Nullable String string ){
        if( string != null ){
            return string.replace( '.', '/' );
        }
        return null;
    }
}
