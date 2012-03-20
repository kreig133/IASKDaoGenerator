package com.kreig133.daogenerator.files;

import com.kreig133.daogenerator.common.FunctionalObjectWithoutFilter;
import com.kreig133.daogenerator.enums.ClassType;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.InOutType;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.settings.Settings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.kreig133.daogenerator.common.Utils.insertTabs;
import static com.kreig133.daogenerator.common.Utils.iterateForParameterList;

/**
 * @author eshangareev
 * @version 1.0
 */
public class InOutClassGenerator extends JavaClassGenerator{

    public static InOutClassGenerator newInstance( DaoMethod daoMethod, InOutType type ){
        return new InOutClassGenerator(
                type == InOutType.IN ?
                        daoMethod.getInputParametrs().getParameter():
                        daoMethod.getOutputParametrs().getParameter(),
                daoMethod.getCommon().getMethodName() +
                        ( type == InOutType.IN ? "In" : "Out")
        );
    }

    private boolean generated;
    
    @Override
    public File getFile() throws IOException {
        File file = new File(
                Settings.settings().getPathForGeneratedSource() +
                        "/" +
                        replacePointBySlash( Settings.settings().getEntityPackage() ) +
                        "/" +
                        convertNameForClassNaming( this.name ) + JAVA_EXTENSION);

        createDirsAndFile( file.getParentFile() );

        return file;
    }

    @Override
    public void generateHead() throws IOException {

        insertPackageLine( Settings.settings().getEntityPackage() );

        insertImport( "java.io.Serializable" );
        insertImport( "java.util.*" );
        builder.append( "\n" );

        builder.append( COMMENT_TO_CLASS );

        insertClassDeclaration(
                ClassType.Class,
                convertNameForClassNaming( this.name ),
                null,
                new ArrayList<String>(){{add( "Serializable" );}}
        );

        writeSerialVersionUID();
        writeEmptyConstructor( convertNameForClassNaming( this.name ) );
    }

    @Override
    public void generateBody( DaoMethod daoMethod ) throws IOException {
        if ( generated ) {
            return;
        }
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

    private static final String COMMENT_TO_CLASS = "/**\n" +
                                           " * @author DaoGenerator 2.3\n" +
                                           " * @version 1.0\n" +
                                           " */\n";

    private final List<ParameterType> parameters;
    private final String name;

    public InOutClassGenerator( List<ParameterType> parameters, String name ) {
        this.parameters = parameters;
        this.name = name;
    }

    private void writeFullConstructor() {
        insertTabs( builder, 1 ).append( "public " ).append( convertNameForClassNaming( this.name ) ).append( "(\n" );
        iterateForParameterList( builder, parameters, 2, new FunctionalObjectWithoutFilter() {
            @Override
            public void writeString( StringBuilder builder, ParameterType p ) {
                builder.append( p.getType().value() ).append( " " ).append( p.getRenameTo() );
            }
        } );

        insertTabs( builder, 1 ).append( "){\n" );
        for( ParameterType p: parameters ){
            insertTabs( builder, 2 ).append( "this." ).append( p.getRenameTo() ).append( " = " ).append( p.getRenameTo() )
                    .append( ";\n" );
        }
        builder.append( "    }\n\n" );
    }

    private void writeToString(){
        insertTabs( builder, 1 ).append( "@Override\n" );
        insertTabs( builder, 1 ).append( "public String toString(){\n" );
        insertTabs( builder, 2 ).append( "return \"" ).append( name ).append( "[\"\n" );

        for( int i =  0; i < parameters.size(); i ++ ){
            ParameterType parameter = parameters.get( i );
            insertTabs( builder,3 ).append( "+\"" ).append( i != 0 ? ", " : ""  )
                    .append( parameter.getRenameTo() ).append( " = \"+" )
                    .append( parameter.getRenameTo() ).append( "\n" );
        }

        insertTabs( builder, 2 ).append( "+\"]\";\n" );
        insertTabs( builder, 1 ).append( "}" );
    }

    public void insertFieldDeclaration( ParameterType p ) {

        insertJavaDoc( new String[] { p.getCommentForJavaDoc() } );
        insertTabs( builder, 1 ).append( "private " ).append( p.getType().value() ).append( " " ).append( p.getRenameTo() );

        String defaultValue = p.getDefaultValue();
        if( defaultValue != null  && ! defaultValue.isEmpty() ) {
            builder.append( " = ").append( p.getDefaultValueForJavaCode() );
        }

        builder.append( ";\n\n" );
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