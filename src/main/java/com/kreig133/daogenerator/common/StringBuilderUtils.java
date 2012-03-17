package com.kreig133.daogenerator.common;

import com.kreig133.daogenerator.common.strategy.FuctionalObject;
import com.kreig133.daogenerator.jaxb.ParameterType;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class StringBuilderUtils {

    //TODO что это тут делает?
    public static void insertEscapedParamName(StringBuilder builder, ParameterType parameterType, boolean fullFormat ){
        builder.append( "#{" ).append( parameterType.getRenameTo() );
        if ( fullFormat ) {
            builder.append( ", mode=" ).append( parameterType.getInOut())
            .append( ", jdbcType=" ).append( parameterType.getJdbcType() );
        }
        builder.append( "}" );
    }

    public static void iterateForParameterList(
            StringBuilder builder,
            List<ParameterType> parameterList,
            FuctionalObject functionalObject
    ) {
        iterateForParameterList( builder, parameterList, 1, functionalObject );
    }

    public static void iterateForParameterList(
            StringBuilder builder,
            List<ParameterType> parameterList,
            int tabs,
            FuctionalObject functionalObject

    ) {
        boolean first = true;

        for ( ParameterType p : parameterList ) {
            if ( functionalObject.filter( p ) ) {
                for ( int i = 0; i < tabs; i++ ) {
                    builder.append( "    " );
                }
                if ( ! first ) {
                    builder.append( "," );
                } else {
                    first = false;
                }
                functionalObject.writeString( builder, p );
                builder.append( "\n" );
            }
        }
    }




}
