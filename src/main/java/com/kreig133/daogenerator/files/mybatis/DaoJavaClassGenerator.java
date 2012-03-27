package com.kreig133.daogenerator.files.mybatis;

import com.kreig133.daogenerator.files.JavaClassGenerator;
import com.kreig133.daogenerator.settings.Settings;

/**
 * @author kreig133
 * @version 1.0
 */
abstract public class DaoJavaClassGenerator extends JavaClassGenerator {

    protected void startingLinesOfDaoFiles() {
        setPackage( Settings.settings().getDaoPackage() );
        addDaoFilesImports();
    }

    protected void addDaoFilesImports() {
        addImport( Settings.settings().getEntityPackage() + ".*" );
    }
}
