package com.kreig133.daogenerator.files.mybatis;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.enums.SelectType;
import com.kreig133.daogenerator.parameter.Parameter;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class AnnotationGenerator {

    public static String generateAnnotation(
        FunctionSettings functionSettings
    ){
        SelectType selectType               = functionSettings.getSelectType();
        List<Parameter> inputParameterList  = functionSettings.getInputParameterList();
        String selectQuery                  = functionSettings.getSelectQuery().toString();
        String name                         = functionSettings.getFunctionName();

        StringBuilder builder = new StringBuilder();

        assert selectType != null ;
        
        builder.append( "    @" ).append( selectType.getAnnotation() ).append( "(\n" );

        builder.append( Utils.wrapWithQuotes( functionSettings.getMyBatisQuery() ) );

        builder.append( "    )\n" );

        return builder.toString();
    }
}
