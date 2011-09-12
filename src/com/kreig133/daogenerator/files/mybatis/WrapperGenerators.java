package com.kreig133.daogenerator.files.mybatis;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.sql.wrappers.GenerateGenerator;

/**
 * @author eshangareev
 * @version 1.0
 */
public class WrapperGenerators {

    //TODO разобраться куда это пихать
    public static void generateWrapperProcedure (
        FunctionSettings functionSettings
    ){
        switch ( functionSettings.getSelectType() ){
            case GENERATE:
                GenerateGenerator.generateWrapper( functionSettings );
                break;
            case GENEROUT:
//              GeneroutGenerator.generateWrapper( functionSettings );
                break;
        }
        throw new IllegalArgumentException();
    }
}
