package com.kreig133.daogenerator.files;

import com.kreig133.daogenerator.common.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author eshangareev
 * @version 1.0
 */
public class JavaDocGenerator extends Generator{

    public StringBuilder insertJavaDoc( String ... commentsLine ) {
        return insertJavaDoc( false, commentsLine );
    }

    public StringBuilder insertJavaDoc( boolean  withReturn, String ... commentsLine ) {
        return insertJavaDoc( withReturn, false, commentsLine );
    }
    public StringBuilder insertJavaDoc(
            boolean  withReturn,
            boolean  withSince,
            String ... commentsLine
    ){

        insertTabs( 1 ).append( "/**" );

        for ( String comment : commentsLine ) {
            if ( Utils.stringNotEmpty( comment ) ) {
                insertNewJavaDocLine();
                builder.append( comment );
            }
        }

        if( withReturn ) {
            insertNewJavaDocLine().append( "@return" );
        }

        if( withSince ) {
            insertNewJavaDocLine().append( "@since " )
                    .append( new SimpleDateFormat( "dd.MM.yyyy HH:mm" ).format( new Date() ) );
        }
        insertLine();
        insertTabs( 1 ).append( " */" );
        insertLine();

        return builder;
    }

    private StringBuilder insertNewJavaDocLine() {
        insertLine();
        return insertTabs( 1 ).append( " * " );
    }

    public String wrapCommentForSetter( String javaDoc ) {
        return "Установить " + javaDoc;
    }

    public String wrapCommentForGetter( String javaDoc ) {
        return "Получить " + javaDoc;
    }
}
