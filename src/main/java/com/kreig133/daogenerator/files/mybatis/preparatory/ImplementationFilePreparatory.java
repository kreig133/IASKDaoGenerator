package com.kreig133.daogenerator.files.mybatis.preparatory;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.enums.ClassType;
import com.kreig133.daogenerator.enums.Type;

import java.io.IOException;
import java.util.ArrayList;

import static com.kreig133.daogenerator.files.JavaFilesUtils.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class ImplementationFilePreparatory extends InterfaceFilePreparatory {

    public static void prepareFile(
            final OperationSettings operationSettings
    ) throws IOException {
            final StringBuilder builder = new StringBuilder();
    
            startingLinesOfDaoFiles( builder );
    
            insertImport( builder, "com.luxoft.sbrf.iask.persistence.common.dao.AbstractDao" );
            insertImport( builder, "org.springframework.stereotype.Repository" );
    
            //TODO блок комментариев
            builder.append( "@Repository\n" );
    
            insertClassDeclaration(
                    ClassType.Class,
                    builder,
                    implementationFileName(),
                    "AbstractDao",
                    new ArrayList<String>() {
                        {
                            add( interfaceFileName() );
                        }
                    }
            );
            Utils.appendByteToFile( implementationFile(), builder.toString().getBytes() );
    }
}