package com.kreig133.daogenerator.files.mybatis.preparatory;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.enums.Type;

import java.io.IOException;

import static com.kreig133.daogenerator.files.JavaFilesUtils.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class MappingFilePreparatory extends Preparatory{

    public static void prepareFile( OperationSettings operationSettings ) throws IOException {
        StringBuilder builder = new StringBuilder();

        if( operationSettings.getType() == Type.DEPO ){
            insertPackageLine( operationSettings.getMapperPackage(), builder );

            commonImports( operationSettings, builder );

            builder.append( "import org.apache.ibatis.annotations.*;\n\n" );
            //TODO блок комментариев
            builder.append( "public interface " ).append( mapperFileName( operationSettings ) ).append( "{\n\n" );
        } else {
            throw new RuntimeException( "Запили для ИАСКА. Быстро! " );
        }
        Utils.appendByteToFile( mappingFile( operationSettings ), builder.toString().getBytes() );
    }
}
