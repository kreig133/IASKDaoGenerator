package com.kreig133.daogenerator.files.mybatis.preparatory;

import com.kreig133.daogenerator.DaoGenerator;
import com.kreig133.daogenerator.common.settings.OperationSettings;

import static com.kreig133.daogenerator.files.JavaFilesUtils.insertImport;
import static com.kreig133.daogenerator.files.JavaFilesUtils.insertPackageLine;
/**
 * @author eshangareev
 * @version 1.0
 */
abstract public class Preparatory {
    protected static void startingLinesOfDaoFiles( StringBuilder builder ) {
        insertPackageLine( builder, DaoGenerator.getCurrentOperationSettings().getDaoPackage() );
        builder.append( "\n" );
        insertImport( builder, "java.util.*" );
        insertImport( builder, DaoGenerator.getCurrentOperationSettings().getEntityPackage() + ".*" );
    }
}
