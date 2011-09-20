package com.kreig133.daogenerator.files.mybatis.preparatory;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.settings.OperationSettings;

import static com.kreig133.daogenerator.files.JavaFilesUtils.*;
/**
 * @author eshangareev
 * @version 1.0
 */
abstract public class Preparatory {

     protected static void commonImports( OperationSettings operationSettings, StringBuilder builder ) {
        insertImport( builder, operationSettings.getEntityPackage() + ".*;" );
    }
}
