package com.kreig133.daogenerator.parameter;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.enums.JavaType;

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

        Utils.getJavaDocString( builder, new String[] { getCommentForJavaDoc() } );
        builder.append( "    private " ).append( type ).append( " " ).append( name );

        return builder.toString();
    }

    public void generateGetter( StringBuilder builder){
        Utils.getJavaDocString( builder, new String[]{"Получить ","\""+ getCommentForJavaDoc()+"\""});

        builder.append( "    public " ).append( type ).append( " get" );
        builder.append( Utils.convertNameForGettersAndSetters( name ) ).append( "(){\n" );
        builder.append( "        return " ).append( name ).append( ";\n");
        builder.append( "    }\n\n" );
    }


    public void generateSetter( StringBuilder builder ){
        Utils.getJavaDocString( builder, new String[] { "Установить ", "\""+getCommentForJavaDoc()+"\""} );
        builder.append( "    public void set").append( Utils.convertNameForGettersAndSetters( name ) );
        builder.append( "( " ).append( type ).append( " " ).append( name ).append( " ){\n" );
        builder.append( "        this." ).append( name ).append( " = " ).append( name ).append( ";\n" );
        builder.append( "    }\n\n" );
    }

    private String getCommentForJavaDoc(){
        if( comment == null || "".equals( comment ) || comment.toLowerCase().equals( "null" ) ) return name;
        return comment;
    }
}
