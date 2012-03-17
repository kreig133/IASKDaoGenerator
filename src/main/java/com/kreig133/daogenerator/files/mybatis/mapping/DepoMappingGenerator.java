package com.kreig133.daogenerator.files.mybatis.mapping;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.enums.ClassType;
import com.kreig133.daogenerator.enums.MethodType;
import com.kreig133.daogenerator.files.Appender;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.SelectType;
import com.kreig133.daogenerator.sql.SqlQueryCreator;

import java.io.IOException;
import java.util.List;

import static com.kreig133.daogenerator.common.StringBuilderUtils.insertTabs;

/**
 * @author kreig133
 * @version 1.0
 */
public class DepoMappingGenerator extends MappingGenerator{

    public static final String MAPPER_PREFIX = "Mapper";

    @Override
    public void generateBody( DaoMethod daoMethod ) throws IOException{
        builder.append(
                generateAnnotation( daoMethod )
                        +"    public "
                        + generateMethodSignature(
                        daoMethod,
                        MethodType.MAPPER )
                        + ";\n"
        );
    }

    @Override
    protected String getFileNameEnding() {
        return MAPPER_PREFIX + JAVA_EXTENSION;
    }

    @Override
    public void generateHead() throws IOException {

        startingLinesOfDaoFiles();

        insertImport( "org.apache.ibatis.annotations.*" );
        //TODO блок комментариев
        insertClassDeclaration(
                ClassType.Interface,
                getFile().getName(),
                null,
                null
        );
    }

    private String generateAnnotation(
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
