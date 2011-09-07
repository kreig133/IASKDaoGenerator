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

        return Utils.getJavaDocString( new String[] { getCommentForJavaDoc() } ) +
                "\tprivate " + type + " " + name;
    }

    public String generateGetter(){
        return Utils.getJavaDocString(new String[] {"Получить ", "\""+getCommentForJavaDoc()+"\""})+
        "\tpublic " + type + " get" + Utils.convertNameForGettersAndSetters( name ) + "(){\n"+
            "\t\treturn " + name + ";\n"+
        "\t}\n\n";
    }


    public String generateSetter(){
        return Utils.getJavaDocString(new String[] {"Установить ", "\""+getCommentForJavaDoc()+"\""})+
        "\tpublic void set" + Utils.convertNameForGettersAndSetters( name ) + "( "+type +" "  + name +" ){\n"+
            "\t\tthis." + name + " = "+ name +";\n"+
        "\t}\n\n";
    }

    private String getCommentForJavaDoc(){
        if( comment == null || "".equals( comment )) return name;
        return comment;
    }
}
