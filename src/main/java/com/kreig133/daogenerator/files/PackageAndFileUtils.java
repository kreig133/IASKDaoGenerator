package com.kreig133.daogenerator.files;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * Вспомогательный класс для работы с файлами и папками. Содержит различные методы для преобразования названий
 * пакетов в пути файловой системы, удаления непустых папок и т.д.
 * 
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

    /**
     Remove a directory and all of its contents.

     The results of executing File.delete() on a File object
     that represents a directory seems to be platform
     dependent. This method removes the directory
     and all of its contents.

     @return true if the complete directory was removed, false if it could not be.
     If false is returned then some of the files in the directory may have been removed.

     */
    public static boolean removeDirectory(File directory) {
        if (directory == null)
            return false;
        if (!directory.exists())
            return true;
        if (!directory.isDirectory())
            return false;

        String[] list = directory.list();

        // Some JVMs return null for File.list() when the
        // directory is empty.
        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                File entry = new File(directory, list[i]);

                //        System.out.println("\tremoving entry " + entry);

                if (entry.isDirectory())
                {
                    if (!removeDirectory(entry))
                        return false;
                }
                else
                {
                    if (!entry.delete())
                        return false;
                }
            }
        }

        return directory.delete();
    }

}
