package com.kreig133.daogenerator;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.common.settings.FunctionSettingsImpl;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.enums.InputOrOutputType;
import com.kreig133.daogenerator.files.InOutClass;
import com.kreig133.daogenerator.files.mybatis.MyBatis;
import com.kreig133.daogenerator.files.parsers.InputFileParser;
import com.kreig133.daogenerator.files.parsers.settings.SettingsReader;
import com.kreig133.daogenerator.sql.ProcedureCallCreator;
import com.kreig133.daogenerator.sql.SelectQueryConverter;
import com.kreig133.daogenerator.sql.wrappers.GenerateGenerator;
import com.kreig133.daogenerator.sql.wrappers.GeneroutGenerator;
import com.kreig133.daogenerator.testing.Tester;

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

    private static final List< FunctionSettings > settingsList = new ArrayList<FunctionSettings>();

    public static void doAction() {

        final OperationSettings operationSettings = DaoGenerator.getCurrentOperationSettings();

        try {
            MyBatis.prepareFiles( operationSettings );

            SettingsReader.readProperties( operationSettings );

        } catch ( Throwable e ) {
            System.err.println( ">>>Controller: Ошибка! При предварительной записи в файлы или " +
                    "считывание настроек, произошла ошибка!" );
            e.printStackTrace();
        }

        for(
                String s:
                    ( new File( operationSettings.getSourcePath() ) )
                            .list(
                                    new FilenameFilter() {
                                        public boolean accept( File dir, String name ) {
                                            return name.endsWith( "txt" );
                                        }
                                    }
                            )
        ) {
            Controller.readFile( new File( operationSettings.getSourcePath() + "/" + s ), operationSettings );
        }

        createQueries();

        try {
            Tester.startFunctionTesting( operationSettings, settingsList.get( 0 ) );
        } catch ( Exception e ) {
            e.printStackTrace(); 
        }

        try {
            writeFiles( operationSettings );
            MyBatis.closeFiles( operationSettings );
        } catch ( IOException e ) {
            System.err.println( ">>>Controller: Ошибка! При записи в файлы, произошла ошибка!" );
            e.printStackTrace();
        }

        System.exit( 0 );
    }

    private static void createQueries() {
        for( FunctionSettings settings : settingsList ){
            switch ( settings.getSelectType() ){
                case CALL:
                    ProcedureCallCreator.generateProcedureCall( settings );
                    break;
                case GENERATE:
                    GenerateGenerator.generateWrapper( settings );
                    break;
                case GENEROUT:
                    GeneroutGenerator.generateWrapper( settings );
                    break;
                default:
                    SelectQueryConverter.processSelectQueryString( settings );
                    break;
            }
        }
    }

    private static void readFile(
            File fileWithData,
            OperationSettings operationSettings
    ) {
        try {
            FunctionSettings currentSettings = new FunctionSettingsImpl( operationSettings );

            settingsList.add( currentSettings );

            //считываем название из файла ( название файла = название хранимки, запроса )
            currentSettings.setName( fileWithData.getName().split( ".txt" )[ 0 ] );

            InputFileParser.readFileWithDataForGenerateDao( fileWithData, operationSettings, currentSettings );
        } catch ( Throwable e ) {
            System.err.println( ">>>Controller: Ошибка! Файл - " + fileWithData.getName() );
            e.printStackTrace();
        }
    }

    private static void writeFiles(
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

    private static void createJavaClassForInputOutputEntities(
            OperationSettings operationSettings,
            FunctionSettings functionSettings,
            InputOrOutputType type
    ) throws IOException {

        FileWriter writer = null;
        try {
            InOutClass inOutClass = new InOutClass(
                    operationSettings.getEntityPackage(),
                    type == InputOrOutputType.IN ? functionSettings.getInputParameterList(): functionSettings.getOutputParameterList(),
                    Utils.convertNameForClassNaming( functionSettings.getName() ) +
                            ( type == InputOrOutputType.IN ? "In" : "Out" )
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