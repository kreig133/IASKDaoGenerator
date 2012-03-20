package com.kreig133.daogenerator.files.mybatis.mapping;

import com.kreig133.daogenerator.enums.ClassType;
import com.kreig133.daogenerator.enums.MethodType;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.SelectType;
import com.kreig133.daogenerator.settings.Settings;
import com.kreig133.daogenerator.sql.creators.QueryCreator;

import java.io.IOException;
import java.util.List;

import static com.kreig133.daogenerator.common.Utils.insertTabs;
import static com.kreig133.daogenerator.common.Utils.stringNotEmpty;

/**
 * @author kreig133
 * @version 1.0
 */
public class DepoMappingGenerator extends MappingGenerator{

    public static final String MAPPER_PREFIX = "Dao";

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

        insertPackageLine( Settings.settings().getMapperPackage() );
        daoFilesImports();

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

    private String generateAnnotation(
            DaoMethod daoMethod
    ){
        SelectType selectType               = daoMethod.getSelectType();

        StringBuilder builder = new StringBuilder();

        assert selectType != null ;

        insertTabs(builder, 1).append( "@" ).append( selectType.getAnnotation() ).append( "(\n" );

        builder.append( wrapWithQuotes(
                QueryCreator.newInstance( daoMethod ).generateExecuteQuery( daoMethod, false ).replaceAll( "\"", "\\\\\"" )
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
                insertTabs(builder, 2).append( "+" );
            } else {
                insertTabs(builder, 2);
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
