package com.kreig133.daogenerator.common;

import com.kreig133.daogenerator.DaoGenerator;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.kreig133.daogenerator.common.StringBuilderUtils.insertTabs;

/**
 * @author eshangareev
 * @version 1.0
 */
public class Utils {

    /**
     * Проверяет нужно ли создавать in-класс
     * @param daoMethod
     * @return
     */
    public static boolean checkToNeedOwnInClass(
            DaoMethod daoMethod
    ) {
        final List<ParameterType> parameters = daoMethod.getInputParametrs().getParameter();

        final Type type = DaoGenerator.getCurrentOperationSettings().getType();

        //TODO магические цифры, да и вообще вынести отсюда например, в тот же Parametrs
        return  ( parameters.size() > 3 && type == Type.DEPO ) ||
                ( parameters.size() > 1 && type == Type.IASK );
    }

    /**
     * Конвертирует имя для использования в геттерах и сеттерах
     * @param name
     * @return
     */
    public static String convertNameForGettersAndSetters( String name ) {

        if ( name == null || "".equals( name ) ) throw new IllegalArgumentException();

        final char[] chars = name.toCharArray();

        if ( Character.isLowerCase( chars[ 0 ] ) ) {
            if ( chars.length == 1 || Character.isLowerCase( chars[ 1 ] ) ) {
                chars[ 0 ] = Character.toUpperCase( chars[ 0 ] );
                name = new String( chars );
            }
        }

        return name;
    }

    public static String convertNameForClassNaming( String name ) {
        final char[] chars = name.toCharArray();
        chars[ 0 ] = Character.toUpperCase( chars[ 0 ] );
        return new String( chars );
    }

    /**
     * Заменяет SQL-овские одинарные кавычки, на Java-всие, нужно в-основном для обработки значений по умолчанию
     * @param string
     * @return
     */
    public static String handleDefaultValue( String string ) {
        if ( string == null ) return string;
        final char[] chars = string.toCharArray();
        for ( int i = 0; i < chars.length; i++ ) {
            if ( ( chars[ i ] == '\'' ) || ( chars[ i ] == '‘' ) || ( chars[ i ] == '’' ) ) {
                chars[ i ] = '\"';
            }
        }

        return new String( chars );
    }

//    public static String[] splitIt( String string ) {
//        return string.split( " " );
//    }

    /**
     * Обрамляет каждую новую строку в кавычки и конкатенацию строк
     * @param string
     * @return
     */
    public static String wrapWithQuotes( String string ) {

        String[] strings = string.split( "\n" );

        strings = deleteEmptyStrings( strings );

        StringBuilder builder = new StringBuilder();

        for ( int i = 0; i < strings.length; i++ ) {
            if ( i != 0 ) {
                insertTabs(builder, 2).append( "+" );
            } else {
                insertTabs(builder, 2);
            }
            builder.append( "\"" ).append( strings[ i ] ).append( "\\n\"\n" );
        }
        return builder.toString();
    }

    /**
     * Удаляет пустые строки из массива
     * @param in
     * @return
     */
    private static String[] deleteEmptyStrings( String[] in ) {
        String[] temp = new String[ in.length ];

        int length = 0;

        for ( String s : in ) {
            if ( stringNotEmpty( s ) ) {
                temp[ length ] = s;
                length++;
            }
        }

        String[] result = new String[ length ];

        System.arraycopy( temp, 0, result, 0, length );

        return result;
    }

    public static void appendByteToFile( File file, byte[] data ) throws IOException {
        FileOutputStream writer = null;
        try {
            writer = new FileOutputStream( file, true );
            writer.write( data );
        } finally {
            if ( writer != null ) writer.close();
        }
    }

    public static boolean stringNotEmpty( String string ) {
        return string != null && ! ( "".equals( string ) );
    }



//    public static String replaceQuestionMarkWithStrings( DaoMethod daoMethod, String s ) {
//        Map<Integer, String> testParameterList = functionSettings.getTestParams();
//        String[] afterSplit = s.split( "\\?" );
//
//        StringBuilder builder = new StringBuilder();
//        builder.append( afterSplit[ 0 ] );
//
////        if ( testParameterList.size() != afterSplit.length - 1 )
////            throw new AssertionError(
////                    "Количество параметров не совпадает с количеством вопросительных знаков в вопросе!" );
//
//        for( int i = 1; i <= functionSettings.getInputParameterList().size(); i++ ){
//            //TODO вставка параметров по дефолту
//            builder.append( testParameterList.get( i ) );
//            builder.append( afterSplit[ i ] );
//        }
//
//        System.out.println( builder );
//        return builder.toString();
//    }

    /**
     * Пытается переделать имя в Java-style
     * @param nameForCall
     * @return
     */
    public static String convertPBNameToName( String nameForCall ) {
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

    /**
     * Добавляет заданное количество табов перед каждой строкой
     * @param text
     * @param tabsQuantity
     * @return
     */
    public static String addTabsBeforeLine( String text, int tabsQuantity ){
        String[] split = text.split( "\n" );
        StringBuilder builder = new StringBuilder();
        for( String s: split ){
            for ( int i = 0; i < tabsQuantity; i ++ ){
                builder.append( "\t" );
            }
            builder.append( s ).append( "\n" );
        }
        return builder.toString();
    }


    public static File getFileFromDirectoryByName( String directoryPath, String fileName ) {
        return new File( new File( directoryPath ).getAbsolutePath() + "/" + fileName );
    }
}
