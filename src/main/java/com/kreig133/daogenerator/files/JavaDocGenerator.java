package com.kreig133.daogenerator.files;

import org.apache.commons.lang.StringUtils;

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

        initialize();

        for ( String comment : commentsLine ) {
            if ( StringUtils.isNotEmpty( comment ) ) {
                insertNewJavaDocLine();
                builder.append( comment.trim() );
            }
        }

        if( withReturn ) {
            insertReturn( "" );
        }

        if( withSince ) {
            insertNewJavaDocLine().append( "@since " )
                    .append( new SimpleDateFormat( "dd.MM.yyyy HH:mm" ).format( new Date() ) );
        }
        close();

        return builder;
    }

    private void close() {
        insertLine();
        insertTabs().append( " */" );
        insertLine();
    }

    private void insertReturn( String comment ) {
        insertNewJavaDocLine().append( "@return " ).append( comment );
    }

    private void initialize() {
        insertTabs().append( "/**" );
    }

    private StringBuilder insertNewJavaDocLine() {
        insertLine();
        return insertTabs().append( " * " );
    }

    public String wrapCommentForSetter( String javaDoc ) {
        return "Установить " + javaDoc;
    }

    public String wrapCommentForGetter( String javaDoc ) {
        return "Получить " + javaDoc;
    }

    public JavaDocBuilder getBuilder() {
        return new JavaDocBuilder();
    }

    public class JavaDocBuilder{
        public JavaDocBuilder initialize(){
            JavaDocGenerator.this.initialize();
            return this;
        }
        public JavaDocBuilder addComment( String comment ) {
            insertNewJavaDocLine().append( comment );
            return this;
        }
        public JavaDocBuilder addParameter( String paramName, String comment ) {
            insertNewJavaDocLine().append( "@param " ).append( paramName ).append( " " ).append( comment );
            return this;
        }
        public JavaDocBuilder addReturn( String comment ){
            insertReturn( comment );
            return this;
        }
        public void close(){
            JavaDocGenerator.this.close();
        }
    }
}
