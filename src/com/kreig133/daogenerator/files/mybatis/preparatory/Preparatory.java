package com.kreig133.daogenerator.files.mybatis.preparatory;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.settings.OperationSettings;

import static com.kreig133.daogenerator.files.JavaFilesUtils.*;
/**
 * @author eshangareev
 * @version 1.0
 */
abstract public class Preparatory {
    protected static void startingLinesOfDaoFiles( OperationSettings operationSettings, StringBuilder builder ) {
        insertPackageLine( builder, operationSettings.getDaoPackage() );
        builder.append( "\n" );
        insertImport( builder, "java.util.*" );
        insertImport( builder, operationSettings.getEntityPackage() + ".*;" );
    }
}
