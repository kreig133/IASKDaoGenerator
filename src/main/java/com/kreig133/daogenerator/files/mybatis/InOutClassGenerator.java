package com.kreig133.daogenerator.files.mybatis;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.kreig133.daogenerator.enums.ClassType;
import com.kreig133.daogenerator.enums.Scope;
import com.kreig133.daogenerator.files.JavaClassGenerator;
import com.kreig133.daogenerator.jaxb.NamingUtils;
import com.kreig133.daogenerator.files.PackageAndFileUtils;
import com.kreig133.daogenerator.jaxb.*;
import com.kreig133.daogenerator.settings.Settings;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class InOutClassGenerator extends JavaClassGenerator {

    @NotNull
    public static InOutClassGenerator newInstance( @NotNull DaoMethod daoMethod, InOutType type ){
        InOutClassGenerator inOutClassGenerator = new InOutClassGenerator(
                type == InOutType.IN ?
                        daoMethod.getInputParametrs().getParameter() :
                        daoMethod.getOutputParametrs().getParameter(),
                daoMethod.getCommon().getMethodName() +
                        ( type == InOutType.IN ? "In" : "Out" )
        );

        if ( type == InOutType.IN?
                daoMethod.getInputParametrs().containsDates()
                :daoMethod.getOutputParametrs().containsDates()
        ) {
            inOutClassGenerator.addImport( DATE_IMPORT );
        }

        return inOutClassGenerator;
    }

    @NotNull
    @Override
    public File getFile() {
        File file = new File(
                Settings.settings().getPathForGeneratedSource() +
                        "/" +
                        PackageAndFileUtils.replacePointBySlash( Settings.settings().getEntityPackage() ) +
                        "/" +
                        NamingUtils.convertNameForClassNaming( this.name ) + JAVA_EXTENSION);

        PackageAndFileUtils.createDirsAndFile( file.getParentFile() );

        return file;
    }

    @Override
    public void generateHead() {
        setPackage( Settings.settings().getEntityPackage() );

        addImport( "java.io.Serializable" );

        insertClassDeclaration(
                ClassType.CLASS,
                NamingUtils.convertNameForClassNaming( this.name ),
                null,
                new ArrayList<String>(){{add( "Serializable" );}}
        );

        insertSerialVersionUID();
        writeEmptyConstructor( NamingUtils.convertNameForClassNaming( this.name ) );
    }

    @Override
    public void generateBody( DaoMethod daoMethod ) {
        writeFullConstructor();

        for ( ParameterType p : parameters ) {
            insertFieldDeclaration( p );
        }

        for ( ParameterType p : parameters ) {
            generateGetter( p );
            generateSetter( p );
        }

        writeToString();
    }

    @Override
    public String getFileName() {
        return NamingUtils.convertNameForClassNaming( this.name );
    }

    private final List<ParameterType> parameters;
    private final String name;

    public InOutClassGenerator( List<ParameterType> parameters, String name ) {
        this.parameters = parameters;
        this.name = name;
    }

    private void writeFullConstructor() {
        if( parameters.size() > 5 ) return;

        insertTabs().append( Scope.PUBLIC.value() )
                .append( " " ).append( NamingUtils.convertNameForClassNaming( this.name ) ).append( "(" );
        insertLine();
        increaseNestingLevel();

        insertTabs().append( StringUtils.join( Iterators.transform(
                parameters.iterator(), new Function<ParameterType, String>() {
                    @NotNull
                    @Override
                    public String apply( @Nullable ParameterType p ) {
                        assert p != null;
                        return p.getType().value() + " " + p.getRenameTo();
                    }
        } ), ",\n\t\t" ) );

        insertTabs().append( ") {" );
        insertLine();
        for( ParameterType p: parameters ){
            insertTabs().append( String.format( "this.%s = %s;", p.getRenameTo(), p.getRenameTo() ) );
            insertLine();
        }
        closeMethodOrInnerClassDefinition();
    }

    private void writeToString(){
        insertTabs().append( "@Override\n" );
        insertTabs().append( "public String toString(){\n" );
        increaseNestingLevel();
        insertTabs().append( "return \"" ).append( name ).append( "[\"\n" );
        increaseNestingLevel();
        for( int i =  0; i < parameters.size(); i ++ ){
            ParameterType parameter = parameters.get( i );
            insertTabs().append( "+\"" ).append( i != 0 ? ", " : ""  )
                    .append( parameter.getRenameTo() ).append( " = \"+" )
                    .append( parameter.getRenameTo() ).append( "\n" );
        }
        decreaseNestingLevel();
        insertTabs().append( "+\"]\";" );
        insertLine();
        closeMethodOrInnerClassDefinition();
    }

    public void insertFieldDeclaration( @NotNull ParameterType p ) {

        jDoc.insertJavaDoc( p.getCommentForJavaDoc() );
        insertTabs().append( Scope.PRIVATE.value() ).append( " " ).append( p.getType().value() )
                .append( " " ).append( p.getRenameTo() );

        String defaultValue = p.getDefaultValue();
        if( StringUtils.isNotEmpty(  defaultValue ) ) {
            builder.append( " = ").append( p.getDefaultValueForJavaCode() );
        }

        builder.append( ";" );
        insertLine();
        insertLine();
    }

    private void generateGetter( @NotNull ParameterType parameterType ){
        super.generateGetter( 
                parameterType.getCommentForJavaDoc(),
                parameterType.getType(), 
                parameterType.getRenameTo() 
        );
    }

    public void generateSetter( @NotNull ParameterType parameterType ) {
        super.generateSetter(
                parameterType.getCommentForJavaDoc(),
                parameterType.getType(),
                parameterType.getRenameTo()
        );
    }

}
