package com.kreig133.daogenerator;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.common.settings.FunctionSettingsImpl;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.enums.InputOrOutputType;
import com.kreig133.daogenerator.files.InOutClass;
import com.kreig133.daogenerator.files.mybatis.MyBatis;
import com.kreig133.daogenerator.files.mybatis.WrapperGenerators;
import com.kreig133.daogenerator.files.parsers.InputFileParser;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
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

    public static void doAction() throws IOException {

        final OperationSettings operationSettings = DaoGenerator.getCurrentOperationSettings();

        MyBatis.prepareFiles( operationSettings );

        for(
                String s:
                ( new File( operationSettings.getSourcePath() ) )
                        .list(
                                new FilenameFilter() {
                                    public boolean accept(File dir, String name) {
                                        return name.endsWith("txt");
                                    }
                                }
                        )
        ) {
            Controller.readFile( new File( operationSettings.getSourcePath() + "/" + s ), operationSettings );
        }

        createQueries( operationSettings );

        //TODO добавить тестирование

        writeFiles( operationSettings );

        MyBatis.closeFiles( operationSettings );
    }

    private static void createQueries( OperationSettings operationSettings ) {
        for( FunctionSettings settings : settingsList ){
            switch ( settings.getSelectType() ){
                case CALL:

                    break;

                case GENERATE:
                case GENEROUT:
                    builder.append(
                        Utils.wrapWithQuotes( WrapperGenerators.generateWrapperProcedure( functionSettings ) ) );
                    break;

                default:

                    break;
            }
        }
    }

    static void readFile(
            File fileWithData,
            OperationSettings operationSettings
    ) throws IOException {

        FunctionSettings currentSettings = new FunctionSettingsImpl();

        settingsList.add( currentSettings );

        //считываем название из файла ( название файла = название хранимки, запроса )
        currentSettings.setFunctionName( fileWithData.getName().split( ".txt" )[ 0 ] );

        InputFileParser.readFileWithDataForGenerateDao( fileWithData, currentSettings );
    }

    static void writeFiles(
        OperationSettings operationSettings
    ) throws IOException {

        for ( FunctionSettings settings: settingsList ) {
            if ( checkToNeedOwnInClass( operationSettings, settings ) ) {
                createJavaClassForInputOutputEntities( operationSettings, settings, InputOrOutputType.IN );
            }

            if ( settings.getOutputParameterList().size() > 1 ) {
                createJavaClassForInputOutputEntities( operationSettings, settings, InputOrOutputType.OUT );
            }

            MyBatis.generateFiles( operationSettings, settings );
        }
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