package com.kreig133.daogenerator.files.mybatis.preparatory;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.settings.OperationSettings;

/**
 * @author eshangareev
 * @version 1.0
 */
abstract public class Preparatory {
     protected static void commonImports( OperationSettings operationSettings, StringBuilder builder ) {
        builder.append( "import " ).append( operationSettings.getEntityPackage() ).append( ".*;\n\n" );
        builder.append( "import java.util.List;\n\n" );
    }
}
