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
abstract public class JavaClassGenerator {

    public static final String JAVA_EXTENSION = ".java";
    protected static final String DATE_IMPORT = "java.util.Date";

    protected String _package;
    protected Set<String> imports = new HashSet<String>();

    protected StringBuilder builder = new StringBuilder();

    abstract public File getFile() throws IOException ;
    abstract public void generateHead() throws IOException;
    abstract public void generateBody( DaoMethod daoMethod ) throws IOException;
    abstract public String getFileName();


    public void generateFoot() throws IOException {
        insertLine().append( "}" );
    }

    protected void setPackage( String packageString ){
        this._package = packageString;
    }
    
    final public String getResult(){
        String s = builder.toString();

        builder = new StringBuilder();

        insertPackageLine( _package );

        for ( String anImport : imports ) {
            insertImport( anImport );
        }
        insertLine();

        return builder.toString() + s;
    }

    protected StringBuilder insertLine() {
        return builder.append( "\n" );
    }

    protected StringBuilder insertTabs( int tabsCount ) {
        Utils.insertTabs( builder, tabsCount );

        return builder;
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
                    outputClass.append( convertNameForClassNaming( methodName ) ).append( "Out" );
                } else {
                    outputClass.append( getShortName( daoMethod.getOutputParametrs().getJavaClassName() ) );
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
                inputParams.add( convertNameForClassNaming( methodName ) + "In request" );
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

    protected void addImport( String clazz ) {
        imports.add( clazz );
    }

    protected void generateMethodSignature(
            @NotNull Scope scope, 
            @Nullable String outputClass,
            @NotNull String methodName,
            @Nullable List<String> inputParams,
            @Nullable List<String> throwsing,
            boolean signatureOnly
    ){
        insertTabs( 1 ).append( scope.value() ).append( " " )
                .append( stringNotEmpty( outputClass ) ? outputClass : "void" ).append( " " ).append( methodName )
                .append( "(" );
        
        if ( inputParams != null && ! inputParams.isEmpty() ) {
            boolean  first = true;
            for ( String inputParam : inputParams ) {
                if ( ! first ) {
                    builder.append( "," );
                }
                first = false;
                insertLine();
                Utils.insertTabs( builder, 2 ).append( inputParam );
            }
            insertLine();
            insertTabs( 1 );
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
    }

    private void insertPackageLine( String packageName ) {
        builder.append( "package " ).append( packageName ).append( ";" );
        insertLine();
        insertLine();
    }

    protected static void createDirsAndFile( File file ) {
        if(!file.exists()){
            file.mkdirs();
        }
    }

    public static String replacePointBySlash( String string ){
        if( string != null ){
            return string.replace( '.', '/' );
        }
        return null;
    }

    protected void insertImport( String path ){
        builder.append( "import " ).append( path ).append( ";" );
        insertLine();
    }

    protected void insertSerialVersionUID() {
        insertLine();
        insertTabs( 1 ).append( Scope.PRIVATE.value() ).append( " static final long serialVersionUID = " );
        builder.append( (long)( Math.random() * Long.MAX_VALUE ) ).append( "L;" );
        insertLine();
        insertLine();
    }

    protected void writeEmptyConstructor( String className ) {
        Utils.insertTabs( builder, 1 ).append( Scope.PUBLIC.value() ).
                append( " " ).append( className ).append( "(){");
        insertLine();
        closeMethodOrInnerClassDefinition();
    }


    protected void insertClassDeclaration(
            ClassType classType,
            String name,
            @Nullable String parentClassName,
            @Nullable List<String> interfaces
    ){
        builder.append( Scope.PUBLIC.value() ).append( " " ).append( classType ).append( " " ).append( name );

        if( ! ( parentClassName == null || "".equals( parentClassName.trim() ) ) ){
            builder.append( " extends " ).append( parentClassName );
        }

        if( interfaces!= null && !interfaces.isEmpty() ){
            builder.append( " implements " );
            for( int i = 0; i < interfaces.size(); i++ ){
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
    }
    
    protected StringBuilder insertJavaDoc( String[] commentsLine ) {

        boolean commentsNotEmpty = false;
        for ( String string : commentsLine ) {
            if ( string != null && ! ( "".equals( string ) ) ) {
                commentsNotEmpty = true;
                break;
            }
        }

        if ( ! commentsNotEmpty ) return builder;

        builder.append( "\t/**\n" );
        for ( String comment : commentsLine ) {
            builder.append( "\t * " );
            builder.append( comment );
            insertLine();
        }
        builder.append( "\t */\n" );

        return builder;
    }

    protected void generateGetter(
            String javaDoc,
            JavaType javaType,
            String name
            
    ){
        generateGetterSignature( javaDoc, javaType, name );

        insertTabs( 2 ).append( "return " ).append( name ).append( ";");
        insertLine();
        closeMethodOrInnerClassDefinition();
    }

    protected void generateGetterSignature( String javaDoc, JavaType javaType, String name ) {
        if ( javaType == JavaType.DATE ) {
            addImport( DATE_IMPORT );
        }

        insertJavaDoc( wrapCommentForGetter( javaDoc ) );

        generateMethodSignature(
                Scope.PUBLIC,
                javaType.value(),
                "get" + convertNameForGettersAndSetters( name ),
                null,
                null,
                false
        );
        insertLine();
    }

    protected String[] wrapCommentForGetter( String javaDoc ) {
        return new String[] { "Получить " + javaDoc };
    }

    public void generateSetter(
            String javaDoc,
            JavaType javaType,
            String name
    ){
        generateSetterSignature( javaDoc, javaType, name );
        insertTabs( 2 ).append( "this." ).append( name ).append( " = " ).append( name ).append( ";" );
        insertLine();
        closeMethodOrInnerClassDefinition();
    }

    protected void generateSetterSignature( String javaDoc, JavaType javaType, String name ) {
        insertJavaDoc( wrapCommentForSetter( javaDoc ) );
        generateMethodSignature(
                Scope.PUBLIC,
                null,
                "set" +convertNameForGettersAndSetters( name ),
                Arrays.asList( javaType.value() + " " + name ),
                null,
                false
        );
        insertLine();
    }

    protected String[] wrapCommentForSetter( String javaDoc ) {
        return new String[] { "Установить "+ javaDoc };
    }

    /**
     * Конвертирует имя для использования в геттерах и сеттерах
     * @param name
     * @return
     */
    protected static String convertNameForGettersAndSetters( String name ) {

        if ( ! stringNotEmpty( name ) ) throw new IllegalArgumentException();

        final char[] chars = name.toCharArray();

        if ( Character.isLowerCase( chars[ 0 ] ) ) {
            if ( chars.length == 1 || Character.isLowerCase( chars[ 1 ] ) ) {
                chars[ 0 ] = Character.toUpperCase( chars[ 0 ] );
                name = new String( chars );
            }
        }

        return name;
    }

    protected static String convertNameForClassNaming( String name ) {
        final char[] chars = name.toCharArray();
        chars[ 0 ] = Character.toUpperCase( chars[ 0 ] );
        return new String( chars );
    }

    public static String convertNameForNonClassNaming( String name ) {
        final char[] chars = name.toCharArray();
        chars[ 0 ] = Character.toLowerCase( chars[ 0 ] );
        return new String( chars );
    }

    public void reset(){
        imports.clear();
        builder = new StringBuilder();
        _package = null;
    }

    protected void closeMethodOrInnerClassDefinition() {
        closeMethodOrInnerClassDefinition( 1 );
    }

    protected void closeMethodOrInnerClassDefinition( int tabs ) {
        insertTabs( tabs ).append( "}" );
        insertLine();
        insertLine();
    }

    protected void closeStatement() {
        builder.append( ");" );
        insertLine();
    }

    protected static String getShortName( String fullJavaClassName ) {
        return fullJavaClassName.substring( fullJavaClassName.lastIndexOf( '.' ) + 1 );
    }

    protected static String getPackage( String fullJavaClassName ) {
        return fullJavaClassName.substring( 0, fullJavaClassName.lastIndexOf( '.' ) );
    }
}
