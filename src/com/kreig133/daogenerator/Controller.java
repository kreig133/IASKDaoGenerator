package com.kreig133.daogenerator;

import com.kreig133.daogenerator.common.Settings;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.enums.InputOrOutputType;
import com.kreig133.daogenerator.enums.ReturnType;
import com.kreig133.daogenerator.enums.SelectType;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.files.InOutClass;
import com.kreig133.daogenerator.files.mybatis.MyBatis;
import com.kreig133.daogenerator.files.parsers.InputFileParser;
import com.kreig133.daogenerator.parameter.Parameter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static com.kreig133.daogenerator.common.Utils.checkToNeedOwnInClass;
import static com.kreig133.daogenerator.files.JavaFilesUtils.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class Controller {
    static void controller(
            File fileWithData,
            Settings settings
    ) throws IOException {

        //считываем название из файла ( название файла = название хранимки, запроса )
        settings.setFunctionName( fileWithData.getName().split(".txt")[0] );

        InputFileParser.readFileWithDataForGenerateDao( fileWithData, settings );

        if ( checkToNeedOwnInClass( settings ) ) {
            createJavaClassForInputOutputEntities( settings, InputOrOutputType.IN );
        }

        if ( settings.getOutputParameterList().size() > 1 ) {
            createJavaClassForInputOutputEntities( settings, InputOrOutputType.OUT );
        }

        MyBatis.generateFiles( settings );
    }



    static void createJavaClassForInputOutputEntities(
            Settings settings,
            InputOrOutputType type
    ) throws IOException {

        FileWriter writer = null;
        try {
            InOutClass inOutClass = new InOutClass(
                    settings.getEntityPackage(),
                    type == InputOrOutputType.IN ? settings.getInputParameterList(): settings.getOutputParameterList(),
                    Utils.convertNameForClassNaming( settings.getFunctionName() ) + type
            );

            File inClassFile = getInOrOutClassFile( settings, inOutClass );
            inClassFile.createNewFile();

            writer = new FileWriter(inClassFile);
            writer.write(inOutClass.toString());
        } finally {
            if (writer != null) writer.close();
        }
    }
}