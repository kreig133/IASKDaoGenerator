package com.kreig133.daogenerator.files.mybatis.mapping;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.files.PackageAndFileUtils;
import com.kreig133.daogenerator.files.mybatis.DaoJavaClassGenerator;
import com.kreig133.daogenerator.files.mybatis.InOutClassGenerator;
import com.kreig133.daogenerator.jaxb.*;
import com.kreig133.daogenerator.files.mybatis.intrface.InterfaceGenerator;
import com.kreig133.daogenerator.settings.Settings;
import com.kreig133.daogenerator.sql.creators.QueryCreatorFactory;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

/**
 * @author kreig133
 * @version 1.0
 */
public class IaskMappingGenerator extends DaoJavaClassGenerator{

    public static IaskMappingGenerator instance (){
        return new IaskMappingGenerator();
    }

    @NotNull
    @Override
    public File getFile() {

        File file = new File(
                Settings.settings().getPathForJavaResources() + "/" +
                        PackageAndFileUtils.replacePointBySlash( Settings.settings().getMapperPackage() ) + "/"
                        + getFileName()
        );

        PackageAndFileUtils.createDirsAndFile( file.getParentFile() );

        return file;
    }

    @Override
    public void generateBody( DaoMethod daoMethod ) {
        generateResultMap( daoMethod );
        generateXmlMapping( daoMethod );
    }

    private void generateResultMap( DaoMethod daoMethod ) {
        if( ! DaoJavaClassGenerator.checkToNeedOwnOutClass( daoMethod ) ) return;
        insertTabs();
        builder.append( String.format( "<resultMap id=\"%sResult\"", daoMethod.getCommon().getMethodName() ) );
        insertLine();
        increaseNestingLevel();
        insertTabs().append( String.format( "type=\"%s\">", InOutClassGenerator.getOutClassName( daoMethod ) ) );
        insertLine();
        for ( ParameterType parameterType : daoMethod.getOutputParametrs().getParameter() ) {
            insertTabs().append( String.format( "<result property=\"%s\" column=\"%s\" />",
                    parameterType.getRenameTo(), parameterType.getName()
            ) );
            insertLine();
        }
        decreaseNestingLevel();
        insertTabs().append( "</resultMap>" );
        insertLine();
        insertLine();
    }

    @Override
    public String getFileName() {
        return Settings.settings().getOperationName() + ".map.xml";
    }

    @NotNull
    @Override
    public String getResult() {
        try {
            generateFoot();
            return builder.toString();
        } finally {
            updateBuilder();
        }
    }

    @Override
    public void generateFoot() {
        decreaseNestingLevel();
        builder.append( "</mapper>" );
    }

    @Override
    public void generateHead() {
        builder.append( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" );
        builder.append( "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis" +
                ".org/dtd/mybatis-3-mapper.dtd\">\n" );
        builder.append( "<mapper namespace=\"" ).append( Settings.settings().getDaoPackage() )
                .append( "." ).append( InterfaceGenerator.instance().getFileName() ).append( "\">\n" );
        increaseNestingLevel();
    }

    private void generateXmlMapping(
            @NotNull final DaoMethod daoMethod
    ){
        final List<ParameterType> inputParameterList  = daoMethod.getInputParametrs ().getParameter();
        final List<ParameterType> outputParameterList = daoMethod.getOutputParametrs().getParameter();
        final String name                         = daoMethod.getCommon().getMethodName();
        final String package_                     = Settings.settings().getEntityPackage();

        insertTabs().append( "<" ) .append( daoMethod.getSelectType().annotation().toLowerCase() )
                .append( " id=\"" ).append( name ).append( "\" " );
        if( daoMethod.getSelectType() == SelectType.CALL ) {
            builder.append( "statementType=\"CALLABLE\"" );
        }

        writeInputParameterType( inputParameterList, name, package_, builder );
        writeOutputParameterType( daoMethod, name, package_, builder );
        builder.append( ">\n\n" );
        increaseNestingLevel();
        for( String s: QueryCreatorFactory.newInstance( daoMethod )
                .generateExecuteQuery( daoMethod, false ).split( "[\\n\\r]+" )
        ) {
            insertTabs().append( s );
            insertLine();
        }
        insertLine();
        decreaseNestingLevel();

        insertTabs().append( "</" ).append( daoMethod.getSelectType().annotation().toLowerCase() ).append( ">" );
        insertLine();
        insertLine();
    }

    private void writeOutputParameterType(
            @NotNull DaoMethod daoMethod,
            String name,
            String package_,
            @NotNull StringBuilder builder
    ) {
        if ( Utils.collectionNotEmpty( daoMethod.getOutputParametrs().getParameter() ) ) {
            insertLine();
            increaseNestingLevel();
            boolean needOwnClass = DaoJavaClassGenerator.checkToNeedOwnOutClass( daoMethod );
            insertTabs().append( String.format( "%s=\"%s\"",
                    ( needOwnClass ? "resultMap" : "resultType" ),
                    ( needOwnClass ? daoMethod.getCommon().getMethodName() + "Result" :
                        ( daoMethod.getOutputParametrs().getParameter().get( 0 ).getType() == JavaType.DATE ?
                            "java.util.Date" : "java.lang."+daoMethod.getOutputParametrs()
                                .getParameter().get( 0 ).getType().value()
                        )
                    )
            ));
            decreaseNestingLevel();
        }
    }

    private void writeInputParameterType(
            @NotNull List<ParameterType> parameterTypeList,
            String name,
            String package_,
            @NotNull StringBuilder builder
    ) {
        if ( Utils.collectionNotEmpty( parameterTypeList ) ) {
            insertLine();
            increaseNestingLevel();
            insertTabs().append( "parameterType=\"" );
            if ( parameterTypeList.size() > 1 ) {
                builder.append( package_ ).append( "." );
                builder.append( NamingUtils.convertNameForClassNaming( name ) ).append( "In" );
            } else {
                builder.append( "java.lang." ).append( parameterTypeList.get( 0 ).getType().value() );
            }
            builder.append( "\"" );
            decreaseNestingLevel();
        }
    }
}
