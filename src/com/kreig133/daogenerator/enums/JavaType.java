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
        List<String> listOfStrings = new ArrayList<String>( 5 );
        listOfStrings.add( "int" );
        listOfStrings.add( "byte" );
        listOfStrings.add( "long" );
        listOfStrings.add( "smallint" );
        listOfStrings.add( "numeric" );

        map.put( Long, listOfStrings );

        listOfStrings = new ArrayList<String>( 1 );
        listOfStrings.add( "datetime" );

        map.put( Date, listOfStrings );

        listOfStrings = new ArrayList<String>( 2 );
        listOfStrings.add( "string" );
        listOfStrings.add( "varchar" );

        map.put( String, listOfStrings );

        listOfStrings = new ArrayList<String>( 1 );
        listOfStrings.add( "decimal" );

        map.put( Double, listOfStrings );
    }

    public static JavaType getBySqlType( String type ) {

        for( JavaType javaType: JavaType.values() ){
            for( java.lang.String typeAllias : map.get( javaType ) ){
                if( type.startsWith( typeAllias ) ){
                    return javaType;
                }
            }
        }

        throw new RuntimeException( "Упс! Нашелся баг: я не смогла преобразовать входной тип в java-тип. Ай-я-я-яй" );
    }
}
