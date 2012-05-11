package com.kreig133.daogenerator.files.mybatis.intrface;

import com.kreig133.daogenerator.enums.ClassType;
import com.kreig133.daogenerator.enums.MethodType;
import com.kreig133.daogenerator.files.PackageAndFileUtils;
import com.kreig133.daogenerator.files.mybatis.DaoJavaClassGenerator;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.settings.Settings;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * @author kreig133
 * @version 1.0
 */
public class InterfaceGenerator extends DaoJavaClassGenerator {

    private final static InterfaceGenerator INSTANCE = new InterfaceGenerator();

    protected InterfaceGenerator() {
    }

    public static InterfaceGenerator instance(){
        return INSTANCE;
    }

    @Override
    public File getFile() {
        return daoFile( getFileName() );
    }

    @Override
    public void generateHead() {
        startingLinesOfDaoFiles();

        //TODO блок комментариев
        insertClassDeclaration(
                ClassType.INTERFACE,
                getFileName(),
                null,
                null
        );
    }

    @Override
    public void generateBody( DaoMethod daoMethod ) {
        generateJavaDocForDaoMethod( daoMethod );
        insertTabs();
        generateMethodSignature( daoMethod, MethodType.DAO );
        builder.append( ";" );
        insertLine();
    }

    @NotNull
    @Override
    public String getFileName() {
        return Settings.settings().getOperationName() + "Dao";
    }


    @NotNull
    protected File daoFile( String fileName ) {
        File file = new File(
                Settings.settings().getPathForGeneratedSource() + "/" +
                        PackageAndFileUtils.replacePointBySlash( Settings.settings().getDaoPackage() ) + "/" +
                        fileName + JAVA_EXTENSION );

        PackageAndFileUtils.createDirsAndFile( file.getParentFile() );

        return file;
    }


}