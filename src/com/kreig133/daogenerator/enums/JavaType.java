package com.kreig133.daogenerator.enums;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public enum JavaType {
    Long, String, Date, Double; 

    private static final Map< JavaType, List<String> > map = new HashMap<JavaType, List<String>>( 6 );

    static {
        linkJavaTypeWithAliases( Long   , "int", "byte", "long", "smallint", "numeric" );
        linkJavaTypeWithAliases( Date   , "datetime" );
        linkJavaTypeWithAliases( String , "string", "varchar" );
        linkJavaTypeWithAliases( Double , "decimal" );
    }

    public static JavaType getBySqlType( String type ) {

        for( JavaType javaType: JavaType.values() ){
            for( java.lang.String typeAllias : map.get( javaType ) ){
                if( type.startsWith( typeAllias ) ){
                    return javaType;
                }
            }
        }

        throw new RuntimeException( "Упс! Нашелся баг: я не шмогла преобразовать входной тип в java-тип. Ай-я-я-яй" );
    }

    private static void linkJavaTypeWithAliases(JavaType type, String...aliases){
        List<String> listOfStrings = new ArrayList<String>( aliases.length );
        Collections.addAll( listOfStrings, aliases );
        map.put( type, listOfStrings );
    }
}
