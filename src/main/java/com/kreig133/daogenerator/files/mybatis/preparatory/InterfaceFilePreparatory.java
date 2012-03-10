package com.kreig133.daogenerator.files.mybatis.preparatory;

import com.kreig133.daogenerator.DaoGenerator;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.enums.ClassType;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.files.Appender;

import java.io.IOException;

import static com.kreig133.daogenerator.files.JavaFilesUtils.*;
/**
 * @author eshangareev
 * @version 1.0
 */
public class InterfaceFilePreparatory extends Preparatory{

    public static void prepareFile( Appender appender ) throws IOException {

        StringBuilder builder = new StringBuilder();

        startingLinesOfDaoFiles( builder );

        //TODO блок комментариев
        insertClassDeclaration(
                ClassType.Interface,
                builder,
                interfaceFileName(),
                null,
                null
        );

        appender.appendStringToFile( interfaceFile(), builder.toString() );
    }
}
