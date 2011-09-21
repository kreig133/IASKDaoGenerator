package com.kreig133.daogenerator.files.mybatis.preparatory;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.enums.ClassType;

import java.io.IOException;

import static com.kreig133.daogenerator.files.JavaFilesUtils.*;
/**
 * @author eshangareev
 * @version 1.0
 */
public class InterfaceFilePreparatory extends Preparatory{

    public static void prepareFile( OperationSettings operationSettings ) throws IOException {
        StringBuilder builder = new StringBuilder();

        startingLinesOfDaoFiles( operationSettings, builder );

        //TODO блок комментариев
        insertClassDeclaration(
                ClassType.Interface,
                builder,
                interfaceFileName( operationSettings ),
                null,
                null
        );

        Utils.appendByteToFile( interfaceFile( operationSettings ), builder.toString().getBytes() );
    }
}
