package com.kreig133.daogenerator.mybatis;

import com.kreig133.daogenerator.enums.SelectType;
import com.kreig133.daogenerator.enums.Type;

/**
 * @author eshangareev
 * @version 1.0
 */
public class MyBatis {

    public static String generateMapping( Type type, SelectType selectType ){
        String method = "";

        switch ( selectType ){
            case CALL:
                if( type == Type.DEPO ){
                    method =
                }
                if( type == Type.IASK ){
                    //TODO генерируем маппинги
                }
                break;
            case GENERATE:
                break;
            case SELECT:
                break;
        }

        return null;
    }
}
