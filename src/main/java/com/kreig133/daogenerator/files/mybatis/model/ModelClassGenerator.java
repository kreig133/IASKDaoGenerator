package com.kreig133.daogenerator.files.mybatis.model;

import com.kreig133.daogenerator.enums.ClassType;
import com.kreig133.daogenerator.files.JavaClassGenerator;
import com.kreig133.daogenerator.jaxb.*;
import com.kreig133.daogenerator.files.PackageAndFileUtils;
import com.kreig133.daogenerator.settings.Settings;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author eshangareev
 * @version 1.0
 */
abstract public class ModelClassGenerator extends JavaClassGenerator {

    final ParametersType parametersType;

    ModelClassGenerator( ParametersType parametersType ) {
        this.parametersType = parametersType;
    }

    @NotNull
    @Override
    public File getFile() {
        File file = new File(
                Settings.settings().getPathForGeneratedSource() +
                        "/" +
                        PackageAndFileUtils.replacePointBySlash( parametersType.getJavaClassName() ) +
                        JAVA_EXTENSION
        );
        PackageAndFileUtils.createDirsAndFile( file.getParentFile() );
        return file;
    }

    @Override
    public void generateHead() {
        setPackage( PackageAndFileUtils.getPackage( parametersType.getJavaClassName() ) );
        insertClassDeclarationAndDetermineParent();
        insertSerialVersionUID();
    }

    protected void insertClassDeclarationAndDetermineParent() {
        insertClassDeclarationAndParent( parametersType.getParent() );
    }

    protected void insertClassDeclarationAndParent( ParentType type ){
        String parent = "com.extjs.gxt.ui.client.data.BaseModelData";
        addImport( parent );
        insertClassDeclaration(
                ClassType.CLASS,
                PackageAndFileUtils.getShortName( parametersType.getJavaClassName() ),
                PackageAndFileUtils.getShortName( parent ),
                null
        );
    }

    @Override
    public void generateBody( DaoMethod daoMethod ) {
        List<ParameterType> parameter = filter(parametersType.getParameter());
        generateEnum( parameter );
        generateSetterAndGetters( parameter );
    }

    protected abstract List<ParameterType> filter( List<ParameterType> parameter );

    private void generateSetterAndGetters( @NotNull List<ParameterType> parameter ) {
        for ( ParameterType parameterType : parameter ) {
            generateGetterSignature(
                    getJavaDocString( parameterType ), parameterType.getType(), parameterType.getRenameTo()
            );
            insertTabs().append( "return get(Fields." )
                    .append( NamingUtils.convertNameForEnum( parameterType.getRenameTo() ) ).append( ".name()" );
            closeStatement();
            closeMethodOrInnerClassDefinition();

            generateSetterSignature(
                getJavaDocString(parameterType), parameterType.getType(), parameterType.getRenameTo(), true
            );
            insertTabs().append( "return set(Fields." )
                    .append( NamingUtils.convertNameForEnum( parameterType.getRenameTo() ) )
                    .append( ".name(), " ).append( parameterType.getRenameTo() );
            closeStatement();
            closeMethodOrInnerClassDefinition();
        }
    }
    
    @NotNull
    private String getJavaDocString( @NotNull ParameterType parameterType ) {
        return processComment( parameterType, true ) + " ({@link Fields#"
                + NamingUtils.convertNameForEnum( parameterType.getRenameTo() ) +"})";
    }

    @NotNull
    private String processComment( @NotNull ParameterType parameterType, boolean forJavaDoc ) {
        return StringUtils.isNotEmpty( parameterType.getComment() ) ? "\"" + parameterType.getComment() + "\"":
                ( forJavaDoc ? "значение" : "\"\"" );
    }

    private void generateEnum( @NotNull List<ParameterType> parameter ) {
        insertClassDeclaration( ClassType.ENUM, "Fields", true,  null, null );

        for ( int i = 0 ; i < parameter.size(); i ++ ) {
            insertTabs().append( NamingUtils.convertNameForEnum( parameter.get( i ).getRenameTo() ) ).append( "(" )
                    .append( processComment( parameter.get( i ), false ) ).append( "" );
            if ( i < parameter.size() - 1 ) {
                builder.append( ")," );
                insertLine();
            } else {
                closeStatement();
            }
        }
        insertLine();
        builder.append( enumBody );
        closeMethodOrInnerClassDefinition();
    }

    @Nullable
    @Override
    public String getFileName() {
        return null;  //TODO
    }
    
    @NotNull
    private String enumBody =
                    "\t\t/** Описание атрибута */\n" +
                    "\t\tprivate final String caption;\n" +
                    "\n" +
                    "\t\t/** \n" +
                    "\t\t * Конструктор по умолчанию \n" +
                    "\t\t * @param caption описание атрибута\n" +
                    "\t\t */\n" +
                    "\t\tprivate Fields(String caption) {\n" +
                    "\t\t\tthis.caption = caption;\n" +
                    "\t\t}\n" +
                    "\n" +
                    "\t\t/**\n" +
                    "\t\t * Вернуть описание атрибута\n" +
                    "\t\t * @return String\n" +
                    "\t\t */\n" +
                    "\t\tpublic String getCaption(){\n" +
                    "\t\t\treturn caption;\n" +
                    "\t\t}\n";
}
