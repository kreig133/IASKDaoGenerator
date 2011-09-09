package com.kreig133.daogenerator.files.mybatis.preparatory;

import com.kreig133.daogenerator.common.Settings;

/**
 * @author eshangareev
 * @version 1.0
 */
abstract public class Preparatory {
     protected static void commonImports( Settings settings, StringBuilder builder ) {
        builder.append( "import " ).append( settings.getEntityPackage() ).append( ".*;\n\n" );
        builder.append( "import java.util.List;\n\n" );
    }
}
