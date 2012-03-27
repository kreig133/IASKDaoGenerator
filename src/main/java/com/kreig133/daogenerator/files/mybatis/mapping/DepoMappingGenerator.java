package com.kreig133.daogenerator.files.mybatis.mapping;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.enums.ClassType;
import com.kreig133.daogenerator.enums.MethodType;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.SelectType;
import com.kreig133.daogenerator.settings.Settings;
import com.kreig133.daogenerator.sql.creators.QueryCreator;

import java.io.IOException;
import java.util.List;

import static com.kreig133.daogenerator.common.Utils.stringNotEmpty;

/**
 * @author kreig133
 * @version 1.0
 */
public class DepoMappingGenerator extends MappingGenerator{

    public static final String MAPPER_PREFIX = "Dao";

    @Override
    public void generateBody( DaoMethod daoMethod ) throws IOException{
        insertLine();
        generateAnnotation( daoMethod );
        generateMethodSignature( daoMethod, MethodType.MAPPER );
        builder.append( ";" );
        insertLine();
    }

    @Override
    public String getFileName() {
        return Settings.settings().getOperationName() + MAPPER_PREFIX;
    }

    @Override
    protected String getFileNameEnding() {
        return  JAVA_EXTENSION;
    }

    @Override
    public void generateHead() throws IOException {

        setPackage( Settings.settings().getMapperPackage() );
        addDaoFilesImports();

        insertImport( "org.apache.ibatis.annotations.*" );
        insertImport( "org.apache.ibatis.mapping.StatementType" );
        insertLine();
        //TODO блок комментариев
        insertClassDeclaration(
                ClassType.Interface,
                Settings.settings().getOperationName()+MAPPER_PREFIX,
                null,
                null
        );
    }

    private void generateAnnotation(
            DaoMethod daoMethod
    ){

        if( daoMethod.getCommon().getConfiguration().isMultipleResult() ) {
            addImport( "java.util.List" );
        }
        
        SelectType selectType = daoMethod.getSelectType();

        assert selectType != null ;

        insertTabs(1).append( "@" ).append( selectType.getAnnotation() ).append( "(" );
        insertLine();

        builder.append( wrapWithQuotes(
                QueryCreator.newInstance( daoMethod ).generateExecuteQuery( daoMethod, false ).replaceAll( "\"",
                        "\\\\\"" )
        ) );

        insertTabs( 1 ).append( ")" );
        insertLine();
        if( daoMethod.getSelectType() == SelectType.CALL ) {
            insertTabs( 1 ).append( "@Options(statementType=StatementType.CALLABLE)" );
            insertLine();
        }

        final List<Integer> indexOfUnnamedParameters = daoMethod.getOutputParametrs().getIndexOfUnnamedParameters();

        if( ! indexOfUnnamedParameters.isEmpty() ) {
            if ( indexOfUnnamedParameters.size() == 1 ) {
                insertTabs( 1 ).append( "@Results(value = {@Result(property=\"" ).append(
                        daoMethod.getOutputParametrs().getParameter().get( indexOfUnnamedParameters.get( 1 ) ).getRenameTo()
                ).append( "\", column=\"\")})" );
            } else {
                throw new RuntimeException( "Не реализованная функциональность!" );
            }
        }

    }

    /**
     * Обрамляет каждую новую строку в кавычки и конкатенацию строк
     * @param string
     * @return
     */
    public static String wrapWithQuotes( String string ) {

        String[] strings = string.split( "[\n\r]" );

        strings = deleteEmptyStrings( strings );

        StringBuilder builder = new StringBuilder();

        for ( int i = 0; i < strings.length; i++ ) {
            if ( i != 0 ) {
                Utils.insertTabs( builder, 2 ).append( "+" );
            } else {
                Utils.insertTabs( builder, 2 );
            }
            builder.append( "\"" ).append( strings[ i ] ).append( "\\n\"\n" );
        }
        return builder.toString();
    }

    /**
     * Удаляет пустые строки из массива
     * @param in
     * @return
     */
    private static String[] deleteEmptyStrings( String[] in ) {
        String[] temp = new String[ in.length ];

        int length = 0;

        for ( String s : in ) {
            if ( stringNotEmpty( s ) ) {
                temp[ length ] = s;
                length++;
            }
        }

        String[] result = new String[ length ];

        System.arraycopy( temp, 0, result, 0, length );

        return result;
    }
}
