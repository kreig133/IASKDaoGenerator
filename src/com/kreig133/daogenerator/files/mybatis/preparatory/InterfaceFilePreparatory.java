package com.kreig133.daogenerator.files.mybatis.preparatory;

import com.kreig133.daogenerator.common.Settings;
import com.kreig133.daogenerator.common.Utils;

import java.io.IOException;

import static com.kreig133.daogenerator.files.JavaFilesUtils.*;
/**
 * @author eshangareev
 * @version 1.0
 */
public class InterfaceFilePreparatory extends Preparatory{
    protected static void startingLinesOfDaoFiles( Settings settings, StringBuilder builder ) {
        insertPackageLine( settings.getDaoPackage(), builder );
        commonImports( settings, builder );
    }

    public static void prepareFile( Settings settings ) throws IOException {
        StringBuilder builder = new StringBuilder();

        startingLinesOfDaoFiles( settings, builder );
        //TODO блок комментариев
        builder.append( "public interface ").append( interfaceFileName( settings ) ).append( "{\n\n" );

        Utils.appendByteToFile( interfaceFile( settings ), builder.toString().getBytes() );
    }
}
