package com.kreig133.daogenerator.files;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

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
            @NotNull String ... commentsLine
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

    @NotNull
    public String wrapCommentForSetter( String javaDoc ) {
        return "Установить " + javaDoc;
    }

    @NotNull
    public String wrapCommentForGetter( String javaDoc ) {
        return "Получить " + javaDoc;
    }

    @NotNull
    public JavaDocBuilder getBuilder() {
        return new JavaDocBuilder();
    }

    public class JavaDocBuilder{
        @NotNull
        public JavaDocBuilder initialize(){
            JavaDocGenerator.this.initialize();
            return this;
        }
        @NotNull
        public JavaDocBuilder addComment( String comment ) {
            insertNewJavaDocLine().append( comment );
            return this;
        }
        @NotNull
        public JavaDocBuilder addParameter( String paramName, String comment ) {
            insertNewJavaDocLine().append( "@param " ).append( paramName ).append( " " ).append( comment );
            return this;
        }
        @NotNull
        public JavaDocBuilder addReturn( String comment ){
            insertReturn( comment );
            return this;
        }
        public void close(){
            JavaDocGenerator.this.close();
        }
    }
}
