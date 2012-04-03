package com.kreig133.daogenerator.files.mybatis.model;

import com.kreig133.daogenerator.enums.ClassType;
import com.kreig133.daogenerator.files.JavaClassGenerator;
import com.kreig133.daogenerator.files.NamingUtils;
import com.kreig133.daogenerator.files.PackageAndFileUtils;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.jaxb.ParametersType;
import com.kreig133.daogenerator.settings.Settings;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
abstract public class ModelClassGenerator extends JavaClassGenerator {

    final ParametersType parametersType;

    ModelClassGenerator( ParametersType parametersType ) {
        this.parametersType = parametersType;
    }

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
        addImport( "com.aplana.sbrf.deposit.web.common.client.operation.data.DepoModelData" );
        insertClassDeclaration(
                ClassType.CLASS,
                PackageAndFileUtils.getShortName( parametersType.getJavaClassName() ),
                "DepoModelData",
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

    private void generateSetterAndGetters( List<ParameterType> parameter ) {
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
    
    private String getJavaDocString( ParameterType parameterType ) {
        return processComment( parameterType, true ) + " ({@link Fields#"
                + NamingUtils.convertNameForEnum( parameterType.getRenameTo() ) +"})";
    }

    private String processComment( ParameterType parameterType, boolean forJavaDoc ) {
        return StringUtils.isNotEmpty( parameterType.getComment() ) ? "\"" + parameterType.getComment() + "\"":
                ( forJavaDoc ? "значение" : "\"\"" );
    }

    private void generateEnum( List<ParameterType> parameter ) {
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

    @Override
    public String getFileName() {
        return null;  //TODO
    }
    
    private String enumBody =
                    "         /** Описание атрибута */\n" +
                    "        private final String caption;\n" +
                    "        \n" +
                    "        /** \n" +
                    "         * Конструктор по умолчанию \n" +
                    "         * @param caption описание атрибута\n" +
                    "         */\n" +
                    "        private Fields(String caption) {\n" +
                    "            this.caption = caption;\n" +
                    "        }\n" +
                    "        \n" +
                    "        /**\n" +
                    "         * Вернуть описание атрибута\n" +
                    "         * @return String\n" +
                    "         */\n" +
                    "        public String getCaption(){\n" +
                    "            return caption;\n" +
                    "        }\n";
}
