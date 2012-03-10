package com.kreig133.daogenerator;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.files.InOutClass;
import com.kreig133.daogenerator.files.mybatis.MyBatis;
import com.kreig133.old_version_converter.parsers.settings.SettingsReader;
import com.kreig133.daogenerator.gui.MainForm;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.InOutType;
import com.kreig133.daogenerator.settings.PropertiesFileController;
import com.kreig133.daogenerator.sql.ProcedureCallCreator;
import com.kreig133.daogenerator.sql.SelectQueryConverter;
import com.kreig133.daogenerator.sql.wrappers.GenerateGenerator;
import com.kreig133.daogenerator.sql.wrappers.GeneroutGenerator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.kreig133.daogenerator.settings.SettingName.*;
import static com.kreig133.daogenerator.common.Utils.checkToNeedOwnInClass;
import static com.kreig133.daogenerator.files.JavaFilesUtils.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class Controller {

    private static final List<DaoMethod> daoMethods = new ArrayList<DaoMethod>();
    private static JAXBContext jc;
    private static Unmarshaller unmarshaller;
    
//    static {
//        try{
//            jc = JAXBContext.newInstance(  );
//        } catch (  ){
//
//        }
//    }

    public static void doAction() {

        final OperationSettings operationSettings = DaoGenerator.getCurrentOperationSettings();

        saveProperties( operationSettings );

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
            Controller.unmarshallFile( Utils.getFileFromDirectoryByName( operationSettings.getSourcePath(), s ) );
        }

        try {
            writeFiles();
            MyBatis.closeFiles();
        } catch ( IOException e ) {
            System.err.println( ">>>Controller: Ошибка! При записи в файлы, произошла ошибка!" );
            throw new RuntimeException( e );
        }
    }

    private static String createQueries( DaoMethod daoMethod ) {
        switch ( daoMethod.getCommon().getConfiguration().getType() ){
            case CALL:
                return ProcedureCallCreator.generateProcedureCall( daoMethod );
            case GENERATE:
                return GenerateGenerator.generateWrapper( daoMethod );
            case GENEROUT:
                return GeneroutGenerator.generateWrapper( daoMethod );
            default:
                return SelectQueryConverter.processSelectQueryString( daoMethod );
        }
    }

    private static DaoMethod unmarshallFile(
            File fileWithData
    ) {
        try {
            DaoMethod daoMethod = new DaoMethod();
//
//            daoMethods.add( currentSettings );
//
//            //считываем название из файла ( название файла = название хранимки, запроса )
//            currentSettings.setName( fileWithData.getName().split( ".txt" )[ 0 ] );
//
//            InputFileParser.readFileWithDataForGenerateDao( fileWithData, operationSettings, currentSettings );

            return daoMethod;
        } catch ( Throwable e ) {
            System.err.println( ">>>Controller: Ошибка! Файл - " + fileWithData.getName() );
            e.printStackTrace();
            return null;
        }
    }

    private static void writeFiles() throws IOException {

        for ( DaoMethod daoMethod: daoMethods ) {
            if ( checkToNeedOwnInClass( daoMethod ) ) {
                createJavaClassForInputOutputEntities( daoMethod, InOutType.IN );
            }

            if ( daoMethod.getOutputParametrs().getParameter().size() > 1 ) {
                createJavaClassForInputOutputEntities( daoMethod, InOutType.OUT );
            }

            MyBatis.generateFiles( daoMethod );
        }
    }

    private static void createJavaClassForInputOutputEntities(
            DaoMethod daoMethod,
            InOutType type
    ) throws IOException {

        FileWriter writer = null;
        try {
            InOutClass inOutClass = new InOutClass(
                    DaoGenerator.getCurrentOperationSettings().getEntityPackage(),
                    type == InOutType.IN ?
                            daoMethod.getInputParametrs().getParameter():
                            daoMethod.getOutputParametrs().getParameter(),
                    Utils.convertNameForClassNaming( daoMethod.getCommon().getMethodName() ) +
                            ( type == InOutType.IN ? "In" : "Out" )
            );

            File inClassFile = getInOrOutClassFile( inOutClass );
            inClassFile.createNewFile();

            writer = new FileWriter(inClassFile);
            writer.write(inOutClass.toString());
        } finally {
            if (writer != null) writer.close();
        }
    }

    private static void saveProperties( OperationSettings operationSettings ) {
        Properties properties = new Properties();

        properties.setProperty( SKIP_TESTS          , String.valueOf( operationSettings.skipTesting() ) );

        properties.setProperty( IASK                , String.valueOf( operationSettings.getType() == Type.IASK ) );
        properties.setProperty( DEPO                , String.valueOf( operationSettings.getType() == Type.DEPO ) );

        properties.setProperty( WIDTH               , String.valueOf( ( int ) MainForm.getInstance().getSize().getWidth () ) );
        properties.setProperty( HEIGHT              , String.valueOf( ( int ) MainForm.getInstance().getSize().getHeight() ) );

        properties.setProperty( DEST_DIR            , operationSettings.getOutputPath   () );
        properties.setProperty( ENTITY_PACKAGE      , operationSettings.getEntityPackage() );
        properties.setProperty( INTERFACE_PACKAGE   , operationSettings.getDaoPackage   () );
        properties.setProperty( MAPPING_PACKAGE     , operationSettings.getMapperPackage() );

        PropertiesFileController.saveSpecificProperties( operationSettings.getSourcePath(), properties );

        properties.setProperty( SOURCE_DIR          , operationSettings.getSourcePath() );

        PropertiesFileController.saveCommonProperties( properties );

    }
}