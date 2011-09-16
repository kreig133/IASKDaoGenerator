package com.kreig133.daogenerator.files.mybatis.preparatory;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.files.JavaFilesUtils;

import java.io.IOException;

import static com.kreig133.daogenerator.files.JavaFilesUtils.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class ImplementationFilePreparatory extends InterfaceFilePreparatory {
    public static void prepareFile(
            OperationSettings operationSettings
    ) throws IOException {
        StringBuilder builder = new StringBuilder();

        if( operationSettings.getType() == Type.DEPO ){
            startingLinesOfDaoFiles( operationSettings, builder );
            builder.append( "import " ).append( mapperFileName( operationSettings ) ).append( ".*;\n" );
            builder.append( "import org.mybatis.spring.support.SqlSessionDaoSupport;\n" );
            builder.append( "import org.springframework.stereotype.Repository;\n\n" );

            //TODO блок комментариев
            builder.append( "@Repository\n" );
            builder.append( "public class " ).append( implementationFileName( operationSettings ) ).
                    append( "extends SqlSessionDaoSupport implements ").
                    append( interfaceFileName( operationSettings ) ).append( "{\n\n" );
        } else {
            JavaFilesUtils.insertPackageLine( operationSettings.getDaoPackage(), builder );
            builder.append( "import java.util.Date;\n" );
            builder.append( "import java.util.List;\n\n" );
            builder.append( "import com.luxoft.sbrf.iask.persistence.common.dao.AbstractDao;\n" );
            builder.append( "import " ).append( operationSettings.getEntityPackage() ).append( ".*;\n\n" );
            builder.append( "public class " ).append( implementationFileName( operationSettings ) )
                    .append( " extends AbstractDao implements " ).append( interfaceFileName( operationSettings ) )
                    .append( "{\n\n" );

        }
        Utils.appendByteToFile( implementationFile( operationSettings ), builder.toString().getBytes() );
    }

}
