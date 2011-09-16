package com.kreig133.daogenerator.common;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.common.strategy.FuctionalObject;
import com.kreig133.daogenerator.parameter.Parameter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class Utils {

    public static boolean checkToNeedOwnInClass(
            OperationSettings operationSettings,
            FunctionSettings functionSettings
    ) {
        final List<Parameter> inputParameterList = functionSettings.getInputParameterList();

        final Type type = operationSettings.getType();

        return  ( inputParameterList.size() > 3 && type == Type.DEPO ) ||
                ( inputParameterList.size() > 1 && type == Type.IASK );
    }

    public static void getJavaDocString( StringBuilder builder, String[] commentsLine ) {

        boolean commentsNotEmpty = false;
        for ( String string : commentsLine ) {
            if ( string != null && ! ( "".equals( string ) ) ) {
                commentsNotEmpty = true;
                break;
            }
        }

        if ( ! commentsNotEmpty ) return;

        builder.append( "\t/**\n" );
        for ( String comment : commentsLine ) {
            builder.append( "\t * " );
            builder.append( comment );
            builder.append( "\n" );
        }
        builder.append( "\t */\n" );
    }

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

    public static String[] splitIt( String string ) {
        return string.split( " " );
    }

    public static String wrapWithQuotes( String string ) {

        String[] strings = string.split( "\n" );

        strings = deleteEmptyStrings( strings );

        StringBuilder builder = new StringBuilder();

        for ( int i = 0; i < strings.length; i++ ) {
            if ( i != 0 ) {
                builder.append( "        +" );
            } else {
                builder.append( "        " );
            }
            builder.append( "\"" ).append( strings[ i ] ).append( "\\n\"\n" );
        }
        return builder.toString();
    }

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

    public static void iterateForParameterList(
            StringBuilder builder,
            List<Parameter> parameterList,
            FuctionalObject functionalObject
    ) {
        iterateForParameterList( builder, parameterList, 1, functionalObject );
    }

    public static void iterateForParameterList(
            StringBuilder builder,
            List<Parameter> parameterList,
            int tabs,
            FuctionalObject functionalObject

    ) {
        boolean first = true;

        for ( Parameter p : parameterList ) {
            if ( functionalObject.filter( p ) ) {
                for ( int i = 0; i < tabs; i++ ) {
                    builder.append( "    " );
                }
                if ( ! first ) {
                    builder.append( "," );
                } else {
                    first = false;
                }
                functionalObject.writeString( builder, p );
                builder.append( "\n" );
            }
        }
    }

    public static String replaceQuestionMarkWithStrings( List<String> testParameterList, String s ) {
        String[] afterSplit = s.split( "\\?" );

        StringBuilder builder = new StringBuilder();
        builder.append( afterSplit[ 0 ] );

        if ( testParameterList.size() != afterSplit.length - 1 )
            throw new AssertionError(
                    "Количество параметров не совпадает с количеством вопросительных знаков в вопросе!" );

        for( int i = 1; i < afterSplit.length; i++ ){
            builder.append( testParameterList.get( i - 1 ) );
            builder.append( afterSplit[ i ] );
        }

        return builder.toString();
    }

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
}
