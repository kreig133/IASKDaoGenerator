package com.kreig133.daogenerator.mybatis;

import com.kreig133.daogenerator.mybatis.wrappers.strategy.FuctionalObject;
import com.kreig133.daogenerator.parameter.Parameter;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class MyBatisUtils {

    public static void iterateForParametrList(
        StringBuilder builder,
        List<Parameter> parameterList,
        FuctionalObject functionalObject
    ){
        boolean first = true;

        for( Parameter p: parameterList ){
            if( functionalObject.filter( p ) ){
                builder.append( "    " );
                if(!first){
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
