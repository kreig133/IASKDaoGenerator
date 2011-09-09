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
public class ImplementationFilePreparatory extends InterfaceFilePreparatory {
    public static void prepareFile( Settings settings ) throws IOException {
        StringBuilder builder = new StringBuilder();

        if( settings.getType() == Type.DEPO ){
            startingLinesOfDaoFiles( settings, builder );
            builder.append( "import " ).append( mapperFileName( settings ) ).append( ".*;\n" );
            builder.append( "import org.mybatis.spring.support.SqlSessionDaoSupport;\n" );
            builder.append( "import org.springframework.stereotype.Repository;\n\n" );

            //TODO блок комментариев
            builder.append( "@Repository\n" );
            builder.append( "public class " ).append( implementationFileName( settings ) ).
                    append( "extends SqlSessionDaoSupport implements CloseDepoAccountDao {\n\n" );

            Utils.appendByteToFile( implementationFile( settings ), builder.toString().getBytes() );
        } else {
            throw new RuntimeException( "Запили для ИАСКА. Быстро! " );
        }
    }

}
