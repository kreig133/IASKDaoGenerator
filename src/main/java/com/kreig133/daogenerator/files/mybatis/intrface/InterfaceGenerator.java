package com.kreig133.daogenerator.files.mybatis.intrface;

import com.kreig133.daogenerator.DaoGenerator;
import com.kreig133.daogenerator.enums.ClassType;
import com.kreig133.daogenerator.enums.MethodType;
import com.kreig133.daogenerator.files.DaoJavaClassGenerator;
import com.kreig133.daogenerator.files.JavaClassGenerator;
import com.kreig133.daogenerator.jaxb.DaoMethod;

import java.io.File;
import java.io.IOException;

import static com.kreig133.daogenerator.common.Utils.addTabsBeforeLine;

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
        return daoFile( interfaceFileName() );
    }

    @Override
    public void generateHead() throws IOException {
        startingLinesOfDaoFiles();

        //TODO блок комментариев
        insertClassDeclaration(
                ClassType.Interface,
                interfaceFileName(),
                null,
                null
        );
    }

    @Override
    public void generateBody( DaoMethod daoMethod ) throws IOException {
        insertJavaDoc(
                daoMethod.getCommon().getComment().split( "\n" )
        ).append(
            addTabsBeforeLine( generateMethodSignature( daoMethod, MethodType.DAO ) + ";\n", 1 )
        );
    }

    public static String interfaceFileName() {
        return DaoGenerator.getCurrentOperationSettings().getOperationName() + "Dao";
    }

    protected static File daoFile( String fileName ) throws IOException {
        File file = new File(
                DaoGenerator.getCurrentOperationSettings().getOutputPath() + "/" +
                        replacePointBySlash( DaoGenerator.getCurrentOperationSettings().getDaoPackage() ) + "/" +
                        fileName + JAVA_EXTENSION );

        createDirsAndFile( file.getParentFile() );

        return file;
    }


}