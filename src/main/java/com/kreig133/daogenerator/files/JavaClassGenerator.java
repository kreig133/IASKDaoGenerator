package com.kreig133.daogenerator.files;

import com.kreig133.daogenerator.DaoGenerator;
import com.kreig133.daogenerator.common.FunctionalObjectWithoutFilter;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.enums.ClassType;
import com.kreig133.daogenerator.enums.MethodType;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.JavaType;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.settings.Settings;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.kreig133.daogenerator.common.Utils.iterateForParameterList;
import static com.kreig133.daogenerator.common.Utils.stringNotEmpty;

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

    protected void insertLine() {
        builder.append( "\n" );
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
                builder.append( convertNameForClassNaming( name ) ).append( "Out" );
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
                Utils.insertTabs( builder, 2 ).append( convertNameForClassNaming( name ) ).append( "In request\n" );
            } else {
                iterateForParameterList( builder, inputParameterList, 2, new FunctionalObjectWithoutFilter() {
                    @Override
                    public void writeString( StringBuilder builder, ParameterType p ) {
                        if (
                                Settings.settings().getType()
                                        ==
                                        Type.DEPO && methodType == MethodType.MAPPER
                                ) {
                            builder.append( "@Param(\"" ).append( p.getRenameTo() ).append( "\") " );
                        }
                        builder.append( p.getType().value() ).append( " " )
                                .append(
                                        Settings.settings().getType() == Type.DEPO ?
                                                p.getRenameTo()
                                                : "request"
                                );
                    }
                } );
            }
            Utils.insertTabs( builder, 1 );
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
        Utils.insertTabs( builder, 1 ).append( "public " ).append( className ).append( "(){\n    }\n\n" );
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

        Utils.insertTabs( builder, 2 ).append( "return " ).append( name ).append( ";\n");
        Utils.insertTabs( builder, 1 ).append( "}\n\n" );
    }

    private void generateGetterSignature( String javaDoc, JavaType javaType, String name ) {
        insertJavaDoc( new String[] { "Получить ", "\"" + javaDoc + "\"" } );
        Utils.insertTabs( builder, 1 ).append( "public " ).append( javaType.value() ).append( " get" );
        builder.append( convertNameForGettersAndSetters( name ) ).append( "(){\n" );
    }


    public void generateSetter(
            String javaDoc,
            JavaType javaType,
            String name
    ){
        generateSetterSignature( javaDoc, javaType, name );
        Utils.insertTabs( builder, 2 ).append( "this." ).append( name ).append( " = " ).append( name ).append( ";\n" );
        Utils.insertTabs( builder, 1 ).append( "}\n\n" );
    }

    private void generateSetterSignature( String javaDoc, JavaType javaType, String name ) {
        insertJavaDoc( new String[] { "Установить ", "\"" + javaDoc + "\"" } );
        Utils.insertTabs( builder, 1 ).append( "public void set" ).append( convertNameForGettersAndSetters( name ) );
        builder.append( "( " ).append( javaType.value() ).append( " " ).append( name ).append( " ){\n" );
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

    public static String convertNameForClassNaming( String name ) {
        final char[] chars = name.toCharArray();
        chars[ 0 ] = Character.toUpperCase( chars[ 0 ] );
        return new String( chars );
    }
}
