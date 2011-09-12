package com.kreig133.daogenerator.files.mybatis.wrappers;

import com.kreig133.daogenerator.common.settings.FunctionSettings;

/**
 * @author eshangareev
 * @version 1.0
 */
public class WrapperGenerators {

    public static String generateWrapperProcedure (
        FunctionSettings functionSettings
    ){
        switch ( functionSettings.getSelectType() ){
            case GENERATE:
                return GenerateGenerator.generateWrapper( functionSettings );
            case GENEROUT:
//                return GeneroutGenerator.generateWrapper( functionSettings );
        }
        throw new IllegalArgumentException();
    }
}
