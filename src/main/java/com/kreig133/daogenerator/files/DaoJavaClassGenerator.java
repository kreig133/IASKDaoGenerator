package com.kreig133.daogenerator.files;

import com.kreig133.daogenerator.DaoGenerator;

/**
 * @author kreig133
 * @version 1.0
 */
abstract public class DaoJavaClassGenerator extends JavaClassGenerator{

    protected void startingLinesOfDaoFiles() {
        insertPackageLine( DaoGenerator.getCurrentOperationSettings().getDaoPackage() );
        daoFilesImports();
    }

    protected void daoFilesImports() {
        builder.append( "\n" );
        insertImport( "java.util.*" );
        insertImport( DaoGenerator.getCurrentOperationSettings().getEntityPackage() + ".*" );
    }

}
