package com.kreig133.daogenerator.mybatis.wrappers;

import com.kreig133.daogenerator.common.Settings;

/**
 * @author eshangareev
 * @version 1.0
 */
public class WrapperGenerators {

    public static String generateWrapperProcedure (
        Settings settings
    ){
        switch ( settings.getSelectType() ){
            case GENERATE:
                return GenerateGenerator.generateWrapper( settings );
            case GENEROUT:
                return GeneroutGenerator.generateWrapper( settings );
        }
        throw new IllegalArgumentException();
    }
}
