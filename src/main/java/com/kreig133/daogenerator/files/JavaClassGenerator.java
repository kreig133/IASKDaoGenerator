package com.kreig133.daogenerator.files;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.enums.ClassType;
import com.kreig133.daogenerator.enums.MethodType;
import com.kreig133.daogenerator.enums.Scope;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.JavaType;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.settings.Settings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.kreig133.daogenerator.common.Utils.stringNotEmpty;

/**
 * @author kreig133
 * @version 1.0
 */
abstract public class JavaClassGenerator extends Generator {

    protected static final String JAVA_EXTENSION = ".java";
    protected static final String DATE_IMPORT = "java.util.Date";

    protected String        javaDocForFile = "Generated by DaoGenerator 2.5";
    protected String        _package;
    protected Set<String>   imports = new HashSet<String>();
    protected JavaDocGenerator jDoc = new JavaDocGenerator();

    abstract public File getFile() throws IOException ;
    abstract public void generateHead() throws IOException;
    abstract public void generateBody( DaoMethod daoMethod ) throws IOException;
    abstract public String getFileName();

    protected JavaClassGenerator() {
        updateBuilder();
    }

    public void reset(){
        imports.clear();
        builder = new StringBuilder();
        _package = null;
//        javaDocForFile = null;
    }

    private void generateFoot() {
        insertLine().append( "}" );
    }

    final public String getResult(){
        generateFoot();
        String s = builder.toString().replaceAll( "\\n{2,}", "\n\n" );

        updateBuilder();

        if( javaDocForFile != null ) {
            jDoc.insertJavaDoc( javaDocForFile );
        }
        insertPackageLine( _package );

        for ( String anImport : imports ) {
            insertImport( anImport );
        }
        insertLine();

        jDoc.insertJavaDoc( false, true, "" );

        return builder.toString() + s;
    }

    protected void setPackage( String packageString ){
        this._package = packageString;
    }

    protected void generateMethodSignature(
            final DaoMethod daoMethod,
            final MethodType methodType
    ) {

        final List<ParameterType>  inputParameterList = daoMethod.getInputParametrs().getParameter();
        final List<ParameterType> outputParameterList = daoMethod.getOutputParametrs().getParameter();
        final String methodName = daoMethod.getCommon().getMethodName();

        StringBuilder outputClass = new StringBuilder();

        if ( ! outputParameterList.isEmpty() ) {
            if ( daoMethod.getCommon().getConfiguration().isMultipleResult() ) {
                outputClass.append( "List<" );
            }
            if ( outputParameterList.size() == 1 ) {
                outputClass.append( outputParameterList.get( 0 ).getType().value() );
                if( outputParameterList.get( 0 ).getType() == JavaType.DATE ) {
                    addImport( DATE_IMPORT );
                }
            } else {
                if ( Settings.settings().getType() == Type.IASK ) {
                    outputClass.append( NamingUtils.convertNameForClassNaming( methodName ) ).append( "Out" );
                } else {
                    outputClass.append(
                            PackageAndFileUtils.getShortName( daoMethod.getOutputParametrs().getJavaClassName() )
                    );
                    addImport( daoMethod.getOutputParametrs().getJavaClassName() );
                }
            }
            if ( daoMethod.getCommon().getConfiguration().isMultipleResult() ) {
                outputClass.append( ">" );
            }
        }

        List<String> inputParams = new ArrayList<String>( inputParameterList.size() );
        if ( ! inputParameterList.isEmpty() ) {
            if ( InOutClassGenerator.checkToNeedOwnInClass( daoMethod ) ) {
                inputParams.add( NamingUtils.convertNameForClassNaming( methodName ) + "In request" );
            } else {
                for ( ParameterType p : inputParameterList ) {
                    if( p.getType() == JavaType.DATE ) {
                        addImport( DATE_IMPORT );
                    }

                    StringBuilder inputParam = new StringBuilder();
                    if (
                            Settings.settings().getType() == Type.DEPO && methodType == MethodType.MAPPER
                    ) {
                        inputParam.append( "@Param(\"" ).append( p.getRenameTo() ).append( "\") " );
                    }
                    inputParam.append( p.getType().value() ).append( " " )
                            .append( Settings.settings().getType() == Type.DEPO ? p.getRenameTo() : "request" );
                    inputParams.add( inputParam.toString() );
                }

            }
        }
        
        generateMethodSignature( Scope.PUBLIC, outputClass.toString(), methodName, inputParams, null, true );
    }

    protected void generateMethodSignature(
            @NotNull Scope scope,
            @Nullable String outputClass,
            @NotNull String methodName,
            @Nullable List<String> inputParams,
            @Nullable List<String> throwsing,
            boolean signatureOnly
    ){
        insertTabs().append( scope.value() ).append( " " )
                .append( stringNotEmpty( outputClass ) ? outputClass : "void" ).append( " " ).append( methodName )
                .append( "(" );

        boolean needNewLineForParam = inputParams != null && inputParams.size() > 2;

        if ( needNewLineForParam ) {
            insertLine();
            increaseNestingLevel();
            insertTabs();
        }
        if ( inputParams != null && ! inputParams.isEmpty() ) {
            boolean  first = true;
            for ( String inputParam : inputParams ) {
                if ( ! first ) {
                    builder.append( ", " );
                }
                first = false;
                builder.append( inputParam );
            }
        }
        if(  needNewLineForParam ){
            insertLine();
            decreaseNestingLevel();
            insertTabs();
        }
        builder.append( ")" );

        if ( throwsing != null && ! throwsing.isEmpty() ) {
            builder.append( " throws " );
            boolean  first = true;
            for ( String th : throwsing ) {
                if ( ! first ) {
                    builder.append( "," );
                }
                first = false;
                builder.append( th );
            }
        }

        builder.append( signatureOnly ? "" : " {" );
        increaseNestingLevel();
    }

    private void insertPackageLine( String packageName ) {
        builder.append( "package " ).append( packageName ).append( ";" );
        insertLine();
        insertLine();
    }

/**********************************************************************************************************************
 *  Работа с импортами
 *********************************************************************************************************************/
    /**
     * @param clazz
     */
    protected void addImport( String clazz ) {
        imports.add( clazz );
    }

    private void insertImport( String path ){
        builder.append( "import " ).append( path ).append( ";" );
        insertLine();
    }

    protected void insertSerialVersionUID() {
        insertLine();
        insertTabs().append( Scope.PRIVATE.value() ).append( " static final long serialVersionUID = " );
        builder.append( ( long ) ( Math.random() * Long.MAX_VALUE ) ).append( "L;" );
        insertLine();
        insertLine();
    }

    protected void writeEmptyConstructor( String className ) {
        Utils.insertTabs( builder, 1 ).append( Scope.PUBLIC.value() ).
                append( " " ).append( className ).append( "(){");
        insertLine();
        increaseNestingLevel();
        closeMethodOrInnerClassDefinition();
    }


    protected void insertClassDeclaration(
            ClassType classType,
            String name,
            @Nullable String parentClassName,
            @Nullable List<String> interfaces
    ){
        insertTabs().append( Scope.PUBLIC.value() ).append( " " ).append( classType ).append( " " ).append( name );

        if( ! ( parentClassName == null || "".equals( parentClassName.trim() ) ) ){
            builder.append( " extends " ).append( parentClassName );
        }

        if( interfaces!= null && !interfaces.isEmpty() ) {
            builder.append( " implements " );
            for ( int i = 0; i < interfaces.size(); i++ ){
                if( i > 0 ){
                    builder.append( "," );
                }
                builder.append( interfaces.get( i ) );
                builder.append( " " );
            }
        }

        builder.append( "{" );
        insertLine();
        insertLine();
        increaseNestingLevel();
    }

    protected void generateGetter(
            String javaDoc,
            JavaType javaType,
            String name
            
    ){
        generateGetterSignature( javaDoc, javaType, name );

        insertTabs().append( "return " ).append( name ).append( ";");
        insertLine();
        closeMethodOrInnerClassDefinition();
    }

    protected void generateGetterSignature( String javaDoc, JavaType javaType, String name ) {
        if ( javaType == JavaType.DATE ) {
            addImport( DATE_IMPORT );
        }

        jDoc.insertJavaDoc( true, jDoc.wrapCommentForGetter( javaDoc ) );

        generateMethodSignature(
                Scope.PUBLIC,
                javaType.value(),
                "get" + NamingUtils.convertNameForGettersAndSetters( name ),
                null,
                null,
                false
        );
        insertLine();
    }

    public void generateSetter(
            String javaDoc,
            JavaType javaType,
            String name
    ){
        generateSetterSignature( javaDoc, javaType, name );
        insertTabs().append( "this." ).append( name ).append( " = " ).append( name ).append( ";" );
        insertLine();
        closeMethodOrInnerClassDefinition();
    }

    protected void generateSetterSignature( String javaDoc, JavaType javaType, String name ) {
        generateSetterSignature( javaDoc, javaType, name, false );
    }

    protected void generateSetterSignature( String javaDoc, JavaType javaType, String name, boolean forModel ) {
        jDoc.insertJavaDoc( forModel, jDoc.wrapCommentForSetter( javaDoc ) );
        generateMethodSignature(
                Scope.PUBLIC,
                forModel ? javaType.value() : null,
                "set" +NamingUtils.convertNameForGettersAndSetters( name ),
                Arrays.asList( javaType.value() + " " + name ),
                null,
                false
        );
        insertLine();
    }

    protected void closeMethodOrInnerClassDefinition() {
        decreaseNestingLevel();
        insertTabs().append( "}" );
        insertLine();
        insertLine();
    }

    protected void closeStatement() {
        builder.append( ");" );
        insertLine();
    }

    private void updateBuilder() {
        setNestingLevel( 0 );
        jDoc.setNestingLevel( 0 );
        builder = new StringBuilder();
        jDoc.setBuilder( builder );
    }

    @Override
    protected void increaseNestingLevel() {
        super.increaseNestingLevel();
        jDoc.setNestingLevel( nestingLevel );
    }

    @Override
    protected void decreaseNestingLevel() {
        super.decreaseNestingLevel();
        jDoc.setNestingLevel( nestingLevel );
    }
}
