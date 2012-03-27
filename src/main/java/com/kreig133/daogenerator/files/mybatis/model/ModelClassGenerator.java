package com.kreig133.daogenerator.files.mybatis.model;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.enums.ClassType;
import com.kreig133.daogenerator.enums.Scope;
import com.kreig133.daogenerator.files.JavaClassGenerator;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.jaxb.ParametersType;
import com.kreig133.daogenerator.settings.Settings;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class ModelClassGenerator extends JavaClassGenerator {
    private boolean generated;

    final ParametersType parametersType;

    public ModelClassGenerator( ParametersType parametersType ) {
        this.parametersType = parametersType;
    }

    public static ModelClassGenerator newInstance( DaoMethod method ){
        return new ModelClassGenerator( method.getOutputParametrs() );
    }

    @Override
    public File getFile() throws IOException {
        File file = new File(
                Settings.settings().getPathForGeneratedSource() +
                        "/" +
                        replacePointBySlash( parametersType.getJavaClassName() ) +
                        JAVA_EXTENSION
        );
        createDirsAndFile( file.getParentFile() );
        return file;
    }

    @Override
    public void generateHead() throws IOException {
        setPackage( getPackage( parametersType.getJavaClassName() ) );
        addImport( "com.aplana.sbrf.deposit.web.common.client.operation.data.DepoModelData" );
        insertLine();
        insertClassDeclaration(
                ClassType.Class,
                getShortName( parametersType.getJavaClassName() ),
                "DepoModelData",
                null
        );
        insertLine();
    }



    @Override
    public void generateBody( DaoMethod daoMethod ) throws IOException {
        if ( generated ) {
            return;
        }
        List<ParameterType> parameter = daoMethod.getOutputParametrs().getParameter();
        generateEnum( parameter );
        generateSetterAndGetters( parameter );
        generated = true;
    }

    private void generateSetterAndGetters( List<ParameterType> parameter ) {
        for ( ParameterType parameterType : parameter ) {
            generateGetterSignature(
                    getJavaDocString( parameterType ), parameterType.getType(), parameterType.getRenameTo()
            );
            insertTabs( 2 ).append( "return get(Fields." )
                    .append( convertForEnum( parameterType.getRenameTo() ) ).append( ".name()" );
            closeStatement();
            closeMethodOrInnerClassDefinition();

            generateSetterSignature(
                getJavaDocString(parameterType), parameterType.getType(), parameterType.getRenameTo()
            );
            insertTabs( 2 ).append( "set(Fields." ).append( convertForEnum( parameterType.getRenameTo() ) )
                    .append( ".name(), " ).append( parameterType.getRenameTo() );
            closeStatement();
            closeMethodOrInnerClassDefinition();
        }
    }
    
    private String getJavaDocString( ParameterType parameterType ) {
        return processComment( parameterType, true ) + " ({@link Fields#"
                + convertForEnum( parameterType.getRenameTo() ) +"})";
    }

    private String processComment( ParameterType parameterType, boolean forJavaDoc ) {
        return Utils.stringNotEmpty( parameterType.getComment() ) ? parameterType.getComment() :
                ( forJavaDoc ? parameterType.getRenameTo() : "" );
    }

    private void generateEnum( List<ParameterType> parameter ) {
        insertTabs( 1 ).append( "public enum Fields{" );
        insertLine();
        for ( int i = 0 ; i < parameter.size(); i ++ ) {
            insertTabs( 2 ).append( convertForEnum( parameter.get( i ).getRenameTo() ) ).append( "(\"" )
                    .append( processComment( parameter.get( i ), false ) ).append( "\"" );
            if ( i < parameter.size() - 1 ) {
                builder.append( ")," );
                insertLine();
            } else {
                closeStatement();
            }
        }
        insertLine();
        String description = "description";
        insertTabs( 2 ).append( "private final String " + description + ";" );
        insertLine();
        insertLine();
        insertTabs( 2 ).append( "private Fields( String " + description + " ) {" );
        insertLine();
        insertTabs( 3 ).append( "this." + description + " = " + description + ";" );
        closeMethodOrInnerClassDefinition( 2 );
        insertTabs( 2 ).append( "public String getDescription(){" );
        insertLine();
        insertTabs( 3 ).append( "return " + description + ";" );
        insertLine();
        closeMethodOrInnerClassDefinition( 2 );
        closeMethodOrInnerClassDefinition();
    }

    @Override
    public String getFileName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
    
    public static String convertForEnum( String name ) {
        StringBuilder builder = new StringBuilder();
        char[] chars = name.toCharArray();
        for(  int i = 0 ; i< chars.length; i ++ ) {
            if ( Character.isUpperCase( chars[ i ] ) ) {
                if ( i > 0 && !Character.isUpperCase( chars[ i - 1 ] ) ) {
                    builder.append( "_" );
                }
            }
            builder.append( chars[ i ] );
        }
        return builder.toString().toUpperCase();
    }
}
