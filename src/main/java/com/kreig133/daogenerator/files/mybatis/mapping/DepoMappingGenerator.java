package com.kreig133.daogenerator.files.mybatis.mapping;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.kreig133.daogenerator.enums.ClassType;
import com.kreig133.daogenerator.enums.MethodType;
import com.kreig133.daogenerator.files.JavaDocGenerator;
import com.kreig133.daogenerator.files.mybatis.DaoJavaClassGenerator;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.jaxb.SelectType;
import com.kreig133.daogenerator.settings.Settings;
import com.kreig133.daogenerator.sql.creators.QueryCreatorFactory;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * @author kreig133
 * @version 1.0
 */
public class DepoMappingGenerator extends MappingGenerator{

    private static final String MAPPER_PREFIX = "Dao";

    @Override
    public void generateBody( @NotNull DaoMethod daoMethod ) {
        insertLine();

        JavaDocGenerator.JavaDocBuilder javaDocBuilder =
                jDoc.getBuilder().initialize().addComment( daoMethod.getCommon().getComment() );

        if( DaoJavaClassGenerator.checkToNeedOwnInClass( daoMethod ) ){
            javaDocBuilder.addParameter( "request" /**TODO хардкод*/, "объект, содержащий входные данные для запроса" );
        } else {
            for ( ParameterType type : daoMethod.getInputParametrs().getParameter() ) {
                javaDocBuilder.addParameter( type.getRenameTo(), type.getComment() );
            }
        }
        if ( ! daoMethod.getOutputParametrs().getParameter().isEmpty() ) {
            javaDocBuilder.addReturn( "объект, содержащий данные, которые вернул запрос" );
        }
        javaDocBuilder.close();

        generateAnnotation( daoMethod );
        generateMethodSignature( daoMethod, MethodType.MAPPER );
        builder.append( ";" );
        decreaseNestingLevel();
        insertLine();
    }

    @NotNull
    @Override
    public String getFileName() {
        return Settings.settings().getOperationName() + MAPPER_PREFIX;
    }

    @Override
    protected String getFileNameEnding() {
        return  JAVA_EXTENSION;
    }

    @Override
    public void generateHead() {
        setPackage( Settings.settings().getMapperPackage() );
        addImport( "org.apache.ibatis.annotations.*" );
        addImport( "org.apache.ibatis.mapping.StatementType" );
        addImport( "org.apache.ibatis.annotations.CacheNamespace" );
        builder.append( "@CacheNamespace(implementation = org.mybatis.caches.ehcache.EhcacheCache.class)" );
        insertLine();
        insertClassDeclaration(
                ClassType.INTERFACE,
                Settings.settings().getOperationName() + MAPPER_PREFIX,
                null,
                null
        );
    }

    private void generateAnnotation(
            @NotNull DaoMethod daoMethod
    ){
        if( daoMethod.getCommon().getConfiguration().isMultipleResult() ) {
            addImport( "java.util.List" );
        }
        
        SelectType selectType = daoMethod.getSelectType();

        assert selectType != null ;

        insertTabs().append( "@" ).append( selectType.annotation() ).append( "(" );
        insertLine();

        increaseNestingLevel();
        wrapWithQuotesAndWrite(
                QueryCreatorFactory.newInstance( daoMethod )
                        .generateExecuteQuery( daoMethod, false ).replaceAll( "\"", "\\\\\"" )
        );
        decreaseNestingLevel();

        insertTabs().append( ")" );
        insertLine();
        if( daoMethod.getSelectType() == SelectType.CALL ) {
            insertTabs().append( "@Options(statementType=StatementType.CALLABLE");
            if( daoMethod.getInputParametrs().isWithPaging() ) {
                builder.append( ", useCache=false" );
            }
            builder.append( ")" );
            insertLine();
        }

        generateNameMapping( daoMethod );
    }

    private void generateNameMapping( @NotNull DaoMethod daoMethod ) {
        if(
                ! daoMethod.getOutputParametrs().getParameter().isEmpty() &&
                  daoMethod.getOutputParametrs().getIndexOfUnnamedParameters().size() < 2 &&
                ! daoMethod.getOutputParametrs().containsSameNames()

        ){
            final NamingMapFormatter namingMapFormatter = new NamingMapFormatter();
            namingMapFormatter.determineMaxLength( daoMethod );
            insertTabs().append( "@Results({");
            insertLine();
            increaseNestingLevel();
            insertTabs().append( StringUtils.join( Iterators.transform(
                    daoMethod.getOutputParametrs().getParameter().iterator(),
                    new Function<ParameterType, String>() {
                        @Override
                        public String apply( @Nullable ParameterType parameterType ) {
                            assert parameterType != null;
                            return namingMapFormatter.getNameMappingString( parameterType );
                        }
                    }
            ), ",\n\t\t" ) );
            decreaseNestingLevel();
            insertLine();
            insertTabs().append( "})" );
            insertLine();
        }
    }

    /**
     * Обрамляет каждую новую строку в кавычки и конкатенацию строк
     * @param string
     * @return
     */
    public StringBuilder wrapWithQuotesAndWrite( @NotNull String string ) {
        String[] strings = string.split( "[\n\r][\n\r]?" );

        strings = deleteEmptyStrings( strings );

        for ( int i = 0; i < strings.length; i++ ) {
            if ( i != 0 ) {
                builder.append( " +" );
                insertLine();
            }
            insertTabs().append( "\" " ).append( strings[ i ] ).append( "\"" );
        }
        insertLine();
        return builder;
    }

    /**
     * Удаляет пустые строки из массива
     * @param in
     * @return
     */
    @NotNull
    private static String[] deleteEmptyStrings( @NotNull String[] in ) {
        String[] temp = new String[ in.length ];

        int length = 0;

        for ( String s : in ) {
            if ( StringUtils.isNotEmpty( s ) ) {
                temp[ length ] = s;
                length++;
            }
        }
        String[] result = new String[ length ];

        System.arraycopy( temp, 0, result, 0, length );

        return result;
    }

    private static class NamingMapFormatter{
        int maxLengthProperty;
        int maxLenghtColumn;
        void determineMaxLength( @NotNull DaoMethod daoMethod ){
            for ( ParameterType type : daoMethod.getOutputParametrs().getParameter() ) {
                maxLenghtColumn = type.getName().length() > maxLenghtColumn ?
                        type.getName().length() :
                        maxLenghtColumn;
                maxLengthProperty = type.getRenameTo().length() > maxLengthProperty ?
                        type.getRenameTo().length():
                        maxLengthProperty;
            }
        }

        private String getNameMappingString( @NotNull ParameterType parameterType ) {
            return String.format(
                    "@Result(property = %s, column = %s)",
                    format( parameterType.getRenameTo(), maxLengthProperty ),
                    format( parameterType.getName    (), maxLenghtColumn   )
            );
        }

        @NotNull
        private String format( @NotNull String string, int max ) {
            return "\"" + string + "\"" + fillString( max - string.length() );
        }

        private String fillString( int i ) {
            StringBuilder stringBuilder = new StringBuilder();
            for ( int j = 0; j < i; j++ ) {
                stringBuilder.append( " " );
            }
            return stringBuilder.toString();
        }
    }
}
