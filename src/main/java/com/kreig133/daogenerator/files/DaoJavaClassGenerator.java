package com.kreig133.daogenerator.files;

import com.kreig133.daogenerator.settings.Settings;

/**
 * @author kreig133
 * @version 1.0
 */
abstract public class DaoJavaClassGenerator extends JavaClassGenerator{

    protected void startingLinesOfDaoFiles() {
        insertPackageLine( Settings.settings().getDaoPackage() );
        daoFilesImports();
    }

    protected void daoFilesImports() {
        builder.append( "\n" );
        insertImport( "java.util.*" );
        insertImport( Settings.settings().getEntityPackage() + ".*" );
    }

}
