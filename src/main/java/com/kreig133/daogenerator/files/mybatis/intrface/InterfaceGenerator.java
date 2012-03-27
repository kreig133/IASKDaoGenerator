package com.kreig133.daogenerator.files.mybatis.intrface;

import com.kreig133.daogenerator.enums.ClassType;
import com.kreig133.daogenerator.enums.MethodType;
import com.kreig133.daogenerator.files.DaoJavaClassGenerator;
import com.kreig133.daogenerator.files.PackageAndFileUtils;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.settings.Settings;

import java.io.File;
import java.io.IOException;

/**
 * @author kreig133
 * @version 1.0
 */
public class InterfaceGenerator extends DaoJavaClassGenerator {

    private static InterfaceGenerator INSTANCE;

    public static InterfaceGenerator instance(){
        if ( INSTANCE == null ) {
            INSTANCE = new InterfaceGenerator();
        }
        return INSTANCE;
    }



    @Override
    public File getFile() throws IOException {
        return daoFile( getFileName() );
    }

    @Override
    public void generateHead() throws IOException {
        startingLinesOfDaoFiles();

        //TODO блок комментариев
        insertClassDeclaration(
                ClassType.Interface,
                getFileName(),
                null,
                null
        );
    }

    @Override
    public void generateBody( DaoMethod daoMethod ) throws IOException {
        jDoc.insertJavaDoc( daoMethod.getCommon().getComment().split( "\n" ) );
        insertTabs( 1 );
        generateMethodSignature( daoMethod, MethodType.DAO );
        builder.append( ";\n" );
    }

    @Override
    public String getFileName() {
        return Settings.settings().getOperationName() + "Dao";
    }


    protected File daoFile( String fileName ) throws IOException {
        File file = new File(
                Settings.settings().getPathForGeneratedSource() + "/" +
                        PackageAndFileUtils.replacePointBySlash( Settings.settings().getDaoPackage() ) + "/" +
                        fileName + JAVA_EXTENSION );

        PackageAndFileUtils.createDirsAndFile( file.getParentFile() );

        return file;
    }


}