package com.kreig133.daogenerator.files.mybatis;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.SelectType;

import static com.kreig133.daogenerator.common.StringBuilderUtils.insertTabs;

/**
 * @author eshangareev
 * @version 1.0
 */
public class AnnotationGenerator {

    public static String generateAnnotation(
        DaoMethod daoMethod
    ){
        SelectType selectType               = daoMethod.getCommon().getConfiguration().getType();

        StringBuilder builder = new StringBuilder();

        assert selectType != null ;
        
        insertTabs(builder, 1).append( "@" ).append( selectType.getAnnotation() ).append( "(\n" );
//TODO
//        builder.append( Utils.wrapWithQuotes( daoMethod.getMyBatisQuery() ) );

        insertTabs( builder, 1 ).append( ")\n" );

        return builder.toString();
    }
}
