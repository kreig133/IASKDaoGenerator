package com.kreig133.daogenerator;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.common.settings.FunctionSettingsImpl;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.enums.InputOrOutputType;
import com.kreig133.daogenerator.files.InOutClass;
import com.kreig133.daogenerator.files.mybatis.MyBatis;
import com.kreig133.daogenerator.files.parsers.InputFileParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.kreig133.daogenerator.common.Utils.checkToNeedOwnInClass;
import static com.kreig133.daogenerator.files.JavaFilesUtils.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class Controller {

    private static List< FunctionSettings > settingsList = new ArrayList<FunctionSettings>();

    private static FunctionSettings currentSettings;

    static void controller(
            File fileWithData,
            OperationSettings operationSettings
    ) throws IOException {

        currentSettings = new FunctionSettingsImpl();
        settingsList.add( currentSettings );

        //считываем название из файла ( название файла = название хранимки, запроса )
        currentSettings.setFunctionName( fileWithData.getName().split( ".txt" )[ 0 ] );

        InputFileParser.readFileWithDataForGenerateDao( fileWithData, currentSettings );

        if ( checkToNeedOwnInClass( operationSettings, currentSettings ) ) {
            createJavaClassForInputOutputEntities( operationSettings, currentSettings, InputOrOutputType.IN );
        }

        if ( currentSettings.getOutputParameterList().size() > 1 ) {
            createJavaClassForInputOutputEntities( operationSettings, currentSettings, InputOrOutputType.OUT );
        }

        MyBatis.generateFiles( operationSettings, currentSettings );
    }



    static void createJavaClassForInputOutputEntities(
            OperationSettings operationSettings,
            FunctionSettings functionSettings,
            InputOrOutputType type
    ) throws IOException {

        FileWriter writer = null;
        try {
            InOutClass inOutClass = new InOutClass(
                    operationSettings.getEntityPackage(),
                    type == InputOrOutputType.IN ? functionSettings.getInputParameterList(): functionSettings.getOutputParameterList(),
                    Utils.convertNameForClassNaming( functionSettings.getFunctionName() ) + type
            );

            File inClassFile = getInOrOutClassFile( operationSettings, inOutClass );
            inClassFile.createNewFile();

            writer = new FileWriter(inClassFile);
            writer.write(inOutClass.toString());
        } finally {
            if (writer != null) writer.close();
        }
    }
}