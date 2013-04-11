package com.kreig133.daogenerator.common;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isAllUpperCase;
import static org.apache.commons.lang.StringUtils.isBlank;

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

    private enum Case{
        UPPER, LOWER
    }

    /**
     * Пытается переделать имя в Java-style
     * @param nameForCall
     * @return
     */
    public static String convertPBNameToName( @NotNull String nameForCall ) {
        if( isBlank(nameForCall) ) {
            return "";
        }
        return modifyOrDeleteFirstCharFromName(
                        toCamelStyle(
                                splitStringByUnderscores(
                                        toLowerCaseWhenAllCharsIsUpperCase(nameForCall)
                                )));
    }

    private static String toLowerCaseWhenAllCharsIsUpperCase(String nameForCall) {
        return isAllUpperCase(removeUnderscoresFromName(nameForCall)) ?
            nameForCall.toLowerCase() : nameForCall;
    }

    private static String toCamelStyle(String[] split) {
        StringBuilder builder = new StringBuilder(split[0]);
        for (int i = 1; i < split.length; i++) {
            builder.append(changeFirstCharCase(split[i], Case.UPPER));
        }
        return builder.toString();
    }


    private static String changeFirstCharCase(String s, Case caze) {
        final char[] chars = s.toCharArray();
        chars[0] = caze == Case.UPPER ? Character.toUpperCase(chars[0]) : Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    private static String modifyOrDeleteFirstCharFromName(String nameForCall) {
        char[] chars = nameForCall.toCharArray();

        if (Character.isUpperCase(chars[1]) && Character.isLowerCase(chars[0])) {
            chars = Arrays.copyOfRange(chars, 1, chars.length);
        }

        return changeFirstCharCaseToLowerCaseIfSecondCharIsLowerCase(toLowerCaseWhenAllCharsIsUpperCase(new String(chars)));
    }

    private static String changeFirstCharCaseToLowerCaseIfSecondCharIsLowerCase(String s) {
        char[] chars = s.toCharArray();
        if (Character.isUpperCase(chars[0]) && Character.isLowerCase(chars[1])) {
            chars[0] = Character.toLowerCase(chars[0]);
            return changeFirstCharCase(new String(chars), Case.LOWER);
        }
        return s;
    }

    private static String[] splitStringByUnderscores(String nameForCall) {
        return nameForCall.split( "_+" );
    }

    private static String removeUnderscoresFromName(String nameForCall) {
        return StringUtils.join(splitStringByUnderscores(nameForCall.trim()));
    }

    @NotNull
    public static File getFileFromDirectoryByName( String directoryPath, String fileName ) {
        return new File( new File( directoryPath ).getAbsolutePath() + "/" + fileName );
    }

    public static boolean collectionNotEmpty( List inputParams ) {
        return inputParams != null && ! inputParams.isEmpty();
    }
}
