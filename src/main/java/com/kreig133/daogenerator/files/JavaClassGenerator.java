package com.kreig133.daogenerator.files;

import com.kreig133.daogenerator.DaoGenerator;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.common.strategy.FunctionalObjectWithoutFilter;
import com.kreig133.daogenerator.enums.ClassType;
import com.kreig133.daogenerator.enums.MethodType;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.JavaType;
import com.kreig133.daogenerator.jaxb.ParameterType;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.kreig133.daogenerator.common.StringBuilderUtils.insertTabs;
import static com.kreig133.daogenerator.common.StringBuilderUtils.iterateForParameterList;

/**
 * @author kreig133
 * @version 1.0
 */
abstract public class JavaClassGenerator {

    public static final String JAVA_EXTENSION = ".java";

    protected StringBuilder builder = new StringBuilder();

    abstract public File getFile() throws IOException ;
    abstract public void generateHead() throws IOException;
    abstract public void generateBody( DaoMethod daoMethod ) throws IOException;


    public void generateFoot() throws IOException {
        builder.append( "\n}" );
    }
    public String getResult(){
        return builder.toString();
    }

    public static String generateMethodSignature(
            final DaoMethod daoMethod,
            final MethodType methodType
    ) {

        final List<ParameterType>  inputParameterList = daoMethod.getInputParametrs().getParameter();
        final List<ParameterType> outputParameterList = daoMethod.getOutputParametrs().getParameter();
        final String     name       = daoMethod.getCommon().getMethodName();

        StringBuilder builder = new StringBuilder();

        if ( outputParameterList.isEmpty() ) {
            builder.append( "void " );
        } else {
            if ( daoMethod.getCommon().getConfiguration().isMultipleResult() ) {
                builder.append( "List<" );
            }
            if ( outputParameterList.size() == 1 ) {
                builder.append( outputParameterList.get( 0 ).getType().value() );
            } else {
                builder.append( Utils.convertNameForClassNaming( name ) ).append( "Out" );
            }
            if ( daoMethod.getCommon().getConfiguration().isMultipleResult() ) {
                builder.append( ">" );
            }
            builder.append( " " );
        }

        builder.append( name ).append( "(" );
        if ( ! inputParameterList.isEmpty() ) {
            builder.append( "\n" );
            if ( InOutClassGenerator.checkToNeedOwnInClass( daoMethod ) ) {
                insertTabs( builder, 2 ).append( Utils.convertNameForClassNaming( name ) ).append( "In request\n" );
            } else {
                iterateForParameterList( builder, inputParameterList, 2, new FunctionalObjectWithoutFilter() {
                    @Override
                    public void writeString( StringBuilder builder, ParameterType p ) {
                        if (
                                DaoGenerator.getCurrentOperationSettings().getType()
                                        ==
                                        Type.DEPO && methodType == MethodType.MAPPER
                                ) {
                            builder.append( "@Param(\"" ).append( p.getRenameTo() ).append( "\") " );
                        }
                        builder.append( p.getType().value() ).append( " " )
                                .append(
                                        DaoGenerator.getCurrentOperationSettings().getType() == Type.DEPO ?
                                                p.getRenameTo()
                                                : "request"
                                );
                    }
                } );
            }
            insertTabs( builder, 1 );
        }
        builder.append( ")" );

        return builder.toString();
    }




    protected void insertPackageLine( String packageName ) {
        builder.append( "package " ).append( packageName ).append( ";\n\n" );
    }

    protected static void createDirsAndFile( File file ) {
        if(!file.exists()){
            file.mkdirs();
        }
    }

    protected static String replacePointBySlash( String string ){
        if( string != null ){
            return string.replace( '.', '/' );
        }

        return null;
    }

    protected void insertImport( String path ){
        builder.append( "import " ).append( path ).append( ";\n" );
    }

    protected void writeSerialVersionUID() {
        builder.append( "\n    private static final long serialVersionUID = " );
        builder.append( (long)( Math.random() * Long.MAX_VALUE ) ).append( "L;\n\n" );
    }

    protected void writeEmptyConstructor( String className ) {
        insertTabs( builder, 1 ).append( "public " ).append( className ).append( "(){\n    }\n\n" );
    }

    protected void insertClassDeclaration(
            ClassType classType,
            String name,
            @Nullable String parentClassName,
            @Nullable List< String > interfaces
    ){
        builder.append( "public " ).append( classType ).append( " " ).append( name );

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

        builder.append( "{\n\n" );
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
            builder.append( "\n" );
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

        insertTabs( builder, 2 ).append( "return " ).append( name ).append( ";\n");
        insertTabs( builder, 1 ).append( "}\n\n" );
    }

    private void generateGetterSignature( String javaDoc, JavaType javaType, String name ) {
        insertJavaDoc( new String[] { "Получить ", "\"" + javaDoc + "\"" } );
        insertTabs( builder, 1 ).append( "public " ).append( javaType.value() ).append( " get" );
        builder.append( Utils.convertNameForGettersAndSetters( name ) ).append( "(){\n" );
    }


    public void generateSetter(
            String javaDoc,
            JavaType javaType,
            String name
    ){
        generateSetterSignature( javaDoc, javaType, name );
        insertTabs( builder, 2 ).append( "this." ).append( name ).append( " = " ).append( name ).append( ";\n" );
        insertTabs( builder, 1 ).append( "}\n\n" );
    }

    private void generateSetterSignature( String javaDoc, JavaType javaType, String name ) {
        insertJavaDoc( new String[] { "Установить ", "\"" + javaDoc + "\"" } );
        insertTabs( builder, 1 ).append( "public void set" ).append( Utils.convertNameForGettersAndSetters( name ) );
        builder.append( "( " ).append( javaType.value() ).append( " " ).append( name ).append( " ){\n" );
    }
}
