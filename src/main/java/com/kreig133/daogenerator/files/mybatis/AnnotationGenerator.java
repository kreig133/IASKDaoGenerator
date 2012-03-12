package com.kreig133.daogenerator.files.mybatis;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.SelectType;
import com.kreig133.daogenerator.sql.SqlQueryCreator;

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

        builder.append( Utils.wrapWithQuotes( SqlQueryCreator.createQueries( daoMethod, false ) ) );

        insertTabs( builder, 1 ).append( ")\n" );

        return builder.toString();
    }
}
