package com.kreig133.daogenerator.files;

import com.kreig133.daogenerator.common.FunctionalObjectWithoutFilter;
import com.kreig133.daogenerator.enums.ClassType;
import com.kreig133.daogenerator.enums.Scope;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.jaxb.*;
import com.kreig133.daogenerator.settings.Settings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.kreig133.daogenerator.common.Utils.iterateForParameterList;
import static com.kreig133.daogenerator.jaxb.ParametersType.getParameterByName;

/**
 * @author eshangareev
 * @version 1.0
 */
public class InOutClassGenerator extends JavaClassGenerator{

    public static InOutClassGenerator newInstance( DaoMethod daoMethod, InOutType type ){
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

    private boolean generated;
    
    @Override
    public File getFile() throws IOException {
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
    public void generateHead() throws IOException {
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
    public void generateBody( DaoMethod daoMethod ) throws IOException {
        if ( generated ) {
            return;
        }
        writeConstructorForPagination();
        writeFullConstructor();

        for ( ParameterType p : parameters ) {
            insertFieldDeclaration( p );
        }

        for ( ParameterType p : parameters ) {
            generateGetter( p );
            generateSetter( p );
        }

        writeToString();

        generated = true;
    }

    private void writeConstructorForPagination() {
        if( ParametersType.isWithPaging( parameters ) ){
            addImport( "com.extjs.gxt.ui.client.data.PagingLoadConfig" );

            insertTabs().append( Scope.PUBLIC.value() ).append( " " )
                    .append( NamingUtils.convertNameForClassNaming( this.name ) )
                    .append( "(Long session, PagingLoadConfig loadConfig) {" );
            increaseNestingLevel();
            insertLine();
            insertTabs().append( "this." ).append(
                    getParameterByName( ParametersType.WithPagingType.ID_SESSION_DS, parameters ).getRenameTo()
            ).append( " = session;" );
            insertLine();
            insertTabs().append( "this." ).append(
                    getParameterByName( ParametersType.WithPagingType.I_START, parameters ).getRenameTo()
            ).append( " = loadConfig == null ? 0L : loadConfig.getOffset();" );
            insertLine();
            insertTabs().append( "this." ).append(
                    getParameterByName( ParametersType.WithPagingType.I_PAGE_LIMIT, parameters ).getRenameTo()
            ).append( " = loadConfig == null ? 0L : loadConfig.getLimit();" );
            insertLine();
            insertTabs().append( "this." ).append(
                    getParameterByName( ParametersType.WithPagingType.I_END, parameters ).getRenameTo()
            ).append( " = 0L;" );
            insertLine();
            insertTabs().append( "this." ).append(
                    getParameterByName( ParametersType.WithPagingType.S_SORT, parameters ).getRenameTo()
            ).append( " = \"\";" );
            insertLine();
            insertTabs().append( "this." ).append(
                    getParameterByName( ParametersType.WithPagingType.I_ROW_COUNT, parameters ).getRenameTo()
            ).append( " = 0L;" );
            insertLine();
            closeMethodOrInnerClassDefinition();
        }
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
        iterateForParameterList( builder, parameters, 2, new FunctionalObjectWithoutFilter() {
            @Override
            public void writeString( StringBuilder builder, ParameterType p ) {
                builder.append( p.getType().value() ).append( " " ).append( p.getRenameTo() );
            }
        } );

        insertTabs().append( ") {" );
        insertLine();
        for( ParameterType p: parameters ){
            insertTabs().append( "this." ).append( p.getRenameTo() ).append( " = " ).append( p.getRenameTo() )
                    .append( ";" );
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

    public void insertFieldDeclaration( ParameterType p ) {

        jDoc.insertJavaDoc( new String[] { p.getCommentForJavaDoc() } );
        insertTabs().append( Scope.PRIVATE.value() ).append( " " ).append( p.getType().value() )
                .append( " " ).append( p.getRenameTo() );

        String defaultValue = p.getDefaultValue();
        if( defaultValue != null  && ! defaultValue.isEmpty() ) {
            builder.append( " = ").append( p.getDefaultValueForJavaCode() );
        }

        builder.append( ";" );
        insertLine();
        insertLine();
    }

    private void generateGetter( ParameterType parameterType ){
        super.generateGetter( 
                parameterType.getCommentForJavaDoc(),
                parameterType.getType(), 
                parameterType.getRenameTo() 
        );
    }

    public void generateSetter( ParameterType parameterType ) {
        super.generateSetter(
                parameterType.getCommentForJavaDoc(),
                parameterType.getType(),
                parameterType.getRenameTo()
        );
    }

    /**
     * Проверяет нужно ли создавать in-класс
     * @param daoMethod
     * @return
     */
    public static boolean checkToNeedOwnInClass(
            DaoMethod daoMethod
    ) {
        final List<ParameterType> parameters = daoMethod.getInputParametrs().getParameter();

        final Type type = Settings.settings().getType();

        //TODO магические цифры, да и вообще вынести отсюда например, в тот же Parametrs
        return  ( parameters.size() > 3 && type == Type.DEPO ) ||
                ( parameters.size() > 1 && type == Type.IASK );
    }
}
