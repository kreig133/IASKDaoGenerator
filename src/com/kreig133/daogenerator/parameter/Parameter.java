package com.kreig133.daogenerator.parameter;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.enums.JavaType;

import static com.kreig133.daogenerator.common.StringBuilderUtils.getJavaDocString;
import static com.kreig133.daogenerator.common.StringBuilderUtils.insertTabs;

/**
 * @author eshangareev
 * @version 1.0
 */
abstract public class Parameter {
    protected String comment;
    protected JavaType type;
    protected String name;
    protected String sqlType;

    public Parameter( String comment, String type, String name ) {
        this.comment = comment;

        type = type.trim().toLowerCase();

        sqlType = type;

        this.type = JavaType.getBySqlType( type );

        this.name = name.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public JavaType getType() {
        return type;
    }

    public void setType( JavaType type ) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType( String sqlType ) {
        this.sqlType = sqlType;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        getJavaDocString( builder, new String[] { getCommentForJavaDoc() } );
        insertTabs( builder, 1 ).append( "private " ).append( type ).append( " " ).append( name );

        return builder.toString();
    }

    public void generateGetter( StringBuilder builder){
        getJavaDocString( builder, new String[] { "Получить ", "\"" + getCommentForJavaDoc() + "\"" } );

        insertTabs( builder, 1 ).append( "public " ).append( type ).append( " get" );
        builder.append( Utils.convertNameForGettersAndSetters( name ) ).append( "(){\n" );
        insertTabs( builder, 2 ).append( "return " ).append( name ).append( ";\n");
        insertTabs( builder, 1 ).append( "}\n\n" );
    }


    public void generateSetter( StringBuilder builder ){
        getJavaDocString( builder, new String[] { "Установить ", "\"" + getCommentForJavaDoc() + "\"" } );
        insertTabs( builder, 1 ).append( "public void set" ).append( Utils.convertNameForGettersAndSetters( name ) );
        builder.append( "( " ).append( type ).append( " " ).append( name ).append( " ){\n" );
        insertTabs( builder, 2 ).append( "this." ).append( name ).append( " = " ).append( name ).append( ";\n" );
        insertTabs( builder, 1 ).append( "}\n\n" );
    }

    private String getCommentForJavaDoc(){
        if( comment == null || "".equals( comment ) || comment.toLowerCase().equals( "null" ) ) return name;
        return comment;
    }
}
