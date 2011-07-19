package com.kreig133.daogenerator;

/**
 * @author eshangareev
 * @version 1.0
 */
abstract public class Parameter {
    protected String comment;
    protected String type;
    protected String name;

    public Parameter(String comment, String type, String name) {
        this.comment = comment;
        if (
                "int".equals(type.trim().toLowerCase())
                        ||
                        "long".equals(type.trim().toLowerCase())
                        ||
                        "smallint".equals(type.trim().toLowerCase())
                ) {
            this.type = "Integer";
        } else if (
                "datetime".equals(type.trim().toLowerCase())
                ) {
            this.type = "Date";
        } else if (
                type != null
                        &&
                            (type.trim().toLowerCase().startsWith("varchar")
                                ||
                            type.trim().toLowerCase().startsWith("string"))
                ) {
            this.type = "String";
        } else if (
                type != null && type.trim().toLowerCase().startsWith("decimal")
                ) {
            this.type = "Double";
        }
        this.name = name.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {

        return Utils.getJavaDocString(new String[] {comment} ) +
                "\tprivate " + type + " " + name;
    }

    public String generateGetter(){
        return Utils.getJavaDocString(new String[] {"Получить ", "\""+comment+"\""})+
        "\tpublic " + type + " get" + Utils.convertNameForGettersAndSetters( name ) + "(){\n"+
            "\t\treturn " + name + ";\n"+
        "\t}\n\n";
    }


    public String generateSetter(){
        return Utils.getJavaDocString(new String[] {"Установить ", "\""+comment+"\""})+
        "\tpublic void set" + Utils.convertNameForGettersAndSetters( name ) + "( "+type +" "  + name +" ){\n"+
            "\t\tthis." + name + " = "+ name +";\n"+
        "\t}\n\n";
    }
}
