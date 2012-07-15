package com.kreig133.daogenerator.files.mybatis.implementation;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.enums.ClassType;
import com.kreig133.daogenerator.enums.MethodType;
import com.kreig133.daogenerator.enums.Scope;
import com.kreig133.daogenerator.files.mybatis.intrface.InterfaceGenerator;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.settings.Settings;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

/**
 * @author kreig133
 * @version 1.0
 */
public class ImplementationGenerator extends InterfaceGenerator{

    private final static ImplementationGenerator INSTANCE = new ImplementationGenerator();

    private ImplementationGenerator() {
    }

    public static ImplementationGenerator instance(){
        return INSTANCE;
    }

    @NotNull
    protected String implementationFileName() {
        return Settings.settings().getOperationName() + "DaoImpl";
    }

    @Override
    public File getFile() {
        return daoFile( implementationFileName( ) );
    }

    @Override
    public void generateHead(){
        startingLinesOfDaoFiles();

        addImport( "ru.sbrf.iask.foundation.persistence.dao.AbstractDao" );
        addImport( "org.springframework.stereotype.Repository" );

        builder.append( "@Repository" );
        insertLine();

        insertClassDeclaration(
                ClassType.CLASS,
                implementationFileName(),
                "AbstractDao",
                new ArrayList<String>() {
                    {
                        add( getFileName() );
                    }
                }
        );
    }

    @Override
    public void generateBody( @NotNull DaoMethod daoMethod ) {
        insertTabs().append( "@Override" );
        insertLine();

        generateMethodSignature( daoMethod, MethodType.DAO );
        builder.append( " {" );
        insertLine();
        insertTabs();

        if ( Utils.collectionNotEmpty( daoMethod.getOutputParametrs().getParameter() ) ) {
            builder.append( "return " );
        }

        generateIaskStyleMethodCall( daoMethod, builder );

        closeStatement();
        closeMethodOrInnerClassDefinition();
    }

    private static void generateIaskStyleMethodCall(
            @NotNull DaoMethod  daoMethod,
            @NotNull StringBuilder     builder
    ) {
        builder.append( "select" )
               .append( daoMethod.getCommon().getConfiguration().isMultipleResult() ? "List" : "One" );

        builder.append( "(\"" ).append( Settings.settings().getDaoPackage() ).append( "." )
               .append( InterfaceGenerator.instance().getFileName() ).append( "." )
               .append( daoMethod.getCommon().getMethodName() ).append( "\" ").append( "," )
               .append( Utils.collectionNotEmpty( daoMethod.getInputParametrs().getParameter() ) ? "request" : "null" );
    }
}
