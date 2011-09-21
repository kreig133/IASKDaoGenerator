package com.kreig133.daogenerator.files.mybatis;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.enums.SelectType;
import com.kreig133.daogenerator.parameter.Parameter;

import java.util.List;

import static com.kreig133.daogenerator.common.StringBufferUtils.insertTabs;

/**
 * @author eshangareev
 * @version 1.0
 */
public class AnnotationGenerator {

    public static String generateAnnotation(
        FunctionSettings functionSettings
    ){
        SelectType selectType               = functionSettings.getSelectType();

        StringBuilder builder = new StringBuilder();

        assert selectType != null ;
        
        insertTabs(builder, 1).append( "@" ).append( selectType.getAnnotation() ).append( "(\n" );

        builder.append( Utils.wrapWithQuotes( functionSettings.getMyBatisQuery() ) );

        insertTabs( builder, 1 ).append( ")\n" );

        return builder.toString();
    }
}
