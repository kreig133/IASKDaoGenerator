package com.kreig133.daogenerator.files.mybatis;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.SelectType;
import com.kreig133.daogenerator.sql.SqlQueryCreator;

import java.util.List;

import static com.kreig133.daogenerator.common.StringBuilderUtils.insertTabs;

/**
 * @author eshangareev
 * @version 1.0
 */
public class AnnotationGenerator {

    public static String generateAnnotation(
        DaoMethod daoMethod
    ){
        SelectType selectType               = daoMethod.getSelectType();

        StringBuilder builder = new StringBuilder();

        assert selectType != null ;
        
        insertTabs(builder, 1).append( "@" ).append( selectType.getAnnotation() ).append( "(\n" );

        builder.append( Utils.wrapWithQuotes(
                SqlQueryCreator.createQueries( daoMethod, false ).replaceAll( "\"", "\\\\\"" )
        ) );

        insertTabs( builder, 1 ).append( ")\n" );
        if( daoMethod.getSelectType() == SelectType.CALL ) {
            insertTabs( builder, 1 ).append( "@Options(statementType=StatementType.CALLABLE)\n" );
        }

        final List<Integer> indexOfUnnamedParameters = daoMethod.getOutputParametrs().getIndexOfUnnamedParameters();

        if( ! indexOfUnnamedParameters.isEmpty() ) {
            if ( indexOfUnnamedParameters.size() == 1 ) {
                insertTabs( builder, 1 ).append( "@Results(value = {@Result(property=\"" ).append(
                    daoMethod.getOutputParametrs().getParameter().get( indexOfUnnamedParameters.get( 1 ) ).getRenameTo()
                ).append( "\", column=\"\")})" );
            } else {
                throw new RuntimeException( "Не реализованная функциональность!" );
            }
        }

        return builder.toString();
    }
}
