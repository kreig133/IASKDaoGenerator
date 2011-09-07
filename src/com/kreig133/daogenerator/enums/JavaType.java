package com.kreig133.daogenerator.enums;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author eshangareev
 * @version 1.0
 */
public enum JavaType {
    Long, Boolean, String, Date, Double, BigDecimal;

    public static JavaType getBySqlType( String type ) {
        if (
                "int"       .equals( type )
                ||
                "byte"      .equals( type )
                ||
                "long"      .equals( type )
                ||
                "smallint"  .equals( type )
                ||
                type.startsWith( "numeric" )
        ) {
            return JavaType.Long;
        }

        if (
                "datetime"  .equals( type )
        ) {
            return JavaType.Date ;
        }

        if (
                type != null
                &&
                (
                    type.startsWith( "varchar" )
                    ||
                    type .startsWith( "string" )
                )
        ) {
            return JavaType.String;
        }
        if (
                type != null && type.startsWith("decimal")
        ) {
            return JavaType.Double;
        }

        throw new RuntimeException( "Упс! Нашелся баг: я не смогла преобразовать входной тип в java-тип" );
    }
}
