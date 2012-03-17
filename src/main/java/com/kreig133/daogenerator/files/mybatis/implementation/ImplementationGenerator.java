package com.kreig133.daogenerator.files.mybatis.implementation;

import com.kreig133.daogenerator.DaoGenerator;
import com.kreig133.daogenerator.enums.ClassType;
import com.kreig133.daogenerator.enums.MethodType;
import com.kreig133.daogenerator.files.Appender;
import com.kreig133.daogenerator.files.mybatis.intrface.InterfaceGenerator;
import com.kreig133.daogenerator.jaxb.DaoMethod;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.kreig133.daogenerator.common.Utils.insertTabs;

/**
 * @author kreig133
 * @version 1.0
 */
public class ImplementationGenerator extends InterfaceGenerator{

    private static ImplementationGenerator INSTANCE;

    public static ImplementationGenerator instance(){
        if ( INSTANCE == null ) {
            INSTANCE = new ImplementationGenerator();
        }
        return INSTANCE;
    }

    protected String implementationFileName() {
        return DaoGenerator.getCurrentOperationSettings().getOperationName() + "DaoImpl";
    }

    @Override
    public File getFile() throws IOException {
        return daoFile( implementationFileName( ) );
    }

    @Override
    public void generateHead() throws IOException {
        startingLinesOfDaoFiles();

        insertImport( "com.luxoft.sbrf.iask.persistence.common.dao.AbstractDao" );
        insertImport( "org.springframework.stereotype.Repository" );

        //TODO блок комментариев
        builder.append( "@Repository\n" );

        insertClassDeclaration(
                ClassType.Class,
                implementationFileName(),
                "AbstractDao",
                new ArrayList<String>() {
                    {
                        add( interfaceFileName() );
                    }
                }
        );
    }

    @Override
    public void generateBody( DaoMethod daoMethod ) throws IOException {
        insertTabs( builder, 1 ).append( "@Override\n    public " );

        builder.append( generateMethodSignature(
                daoMethod,
                MethodType.DAO ) );
        builder.append( "{\n" );
        insertTabs( builder, 2 );

        if( ! daoMethod.getOutputParametrs().getParameter().isEmpty() ){
            builder.append( "return " );
        }


        generateIaskStyleMethodCall( daoMethod, builder );


        builder.append( ");\n" );
        insertTabs(builder, 1 ).append( "}\n\n" );
    }

    private static void generateIaskStyleMethodCall(
            DaoMethod  daoMethod,
            StringBuilder     builder
    ) {
        builder.append( "select" );
        if ( daoMethod.getCommon().getConfiguration().isMultipleResult() ) {
            builder.append( "List" );
        } else {
            builder.append( "One" );
        }

        builder.append( "(\"" ).append( DaoGenerator.getCurrentOperationSettings().getDaoPackage() ).append( "." )
                .append( interfaceFileName() ).append( "." )
                .append( daoMethod.getCommon().getMethodName() ).append( "\" ").append( "," );
        if( ! daoMethod.getInputParametrs().getParameter().isEmpty() ){
            builder.append( "request" );
        } else {
            builder.append( "null" );
        }
    }
}
