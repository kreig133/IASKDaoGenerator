package com.kreig133.daogenerator.files.mybatis.preparatory;

import com.kreig133.daogenerator.common.Settings;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.enums.Type;

import java.io.IOException;

import static com.kreig133.daogenerator.files.JavaFilesUtils.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class MappingFilePreparatory extends Preparatory{
    public static void prepareFile( Settings settings ) throws IOException {
        StringBuilder builder = new StringBuilder();

        if( settings.getType() == Type.DEPO ){
            insertPackageLine( settings.getMapperPackage(), builder );

            commonImports( settings, builder );

            builder.append( "import org.apache.ibatis.annotations.*;\n\n" );
            //TODO блок комментариев
            builder.append( "public interface " ).append( mapperFileName( settings ) ).append( "{\n\n" );
        } else {
            throw new RuntimeException( "Запили для ИАСКА. Быстро! " );
        }
        Utils.appendByteToFile( mappingFile( settings ), builder.toString().getBytes() );
    }
}
