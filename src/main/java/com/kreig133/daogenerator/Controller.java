package com.kreig133.daogenerator;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.files.Appender;
import com.kreig133.daogenerator.files.InOutClass;
import com.kreig133.daogenerator.files.mybatis.MyBatis;
import com.kreig133.daogenerator.gui.MainForm;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.InOutType;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.settings.PropertiesFileController;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
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

    protected static final Appender appender = new Appender() {
        @Override
        public void appendStringToFile( File file, String string ) {
            try {
                Utils.appendByteToFile( file, string.getBytes() );
            } catch ( IOException e ) {
                //TODO
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    };

    public static void doAction() {

        final OperationSettings opSettings = DaoGenerator.getCurrentOperationSettings();

        saveProperties();

        try {
            MyBatis.prepareFiles( appender );
        } catch ( Throwable e ) {
            throw new RuntimeException( "Ошибка! При предварительной записи в файлы или считывании настроек.", e );
        }

        for( String s : getXmlFileNamesInDirectory() ) {
            daoMethods.add( JaxbHandler.unmarshallFile(
                    Utils.getFileFromDirectoryByName( opSettings.getSourcePath(), s )
            ) );
        }

        try {
            writeFiles();
            MyBatis.closeFiles();
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    public static String[] getXmlFileNamesInDirectory( ) {
        return ( new File( DaoGenerator.getCurrentOperationSettings() .getSourcePath() ) )
                .list(
                        new FilenameFilter() {
                            public boolean accept( File dir, String name ) {
                                return name.endsWith( "xml" );
                            }
                        }
                );
    }

    protected static void writeFiles() throws IOException {

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

    protected static void createJavaClassForInputOutputEntities(
            DaoMethod daoMethod,
            InOutType type
    ) throws IOException {

        FileWriter writer = null;
        try {
            InOutClass inOutClass = getInOutClass( daoMethod, type );

            File inClassFile = getInOrOutClassFile( inOutClass );
            inClassFile.createNewFile();

            writer = new FileWriter(inClassFile);
            writer.write( inOutClass.toString() );
        } finally {
            if (writer != null) writer.close();
        }
    }

    protected static InOutClass getInOutClass( DaoMethod daoMethod, InOutType type ) {
        return new InOutClass(
                DaoGenerator.getCurrentOperationSettings().getEntityPackage(),
                type == InOutType.IN ?
                        daoMethod.getInputParametrs().getParameter():
                        daoMethod.getOutputParametrs().getParameter(),
                Utils.convertNameForClassNaming( daoMethod.getCommon().getMethodName() ) +
                        ( type == InOutType.IN ? "In" : "Out" )
        );
    }

    protected static void saveProperties() {
        Properties properties = new Properties();

        final OperationSettings operationSettings = DaoGenerator.getCurrentOperationSettings();

        properties.setProperty( IASK                , String.valueOf( operationSettings.getType() == Type.IASK ) );
        properties.setProperty( DEPO                , String.valueOf( operationSettings.getType() == Type.DEPO ) );

        properties.setProperty( WIDTH               , String.valueOf( ( int ) MainForm.getInstance().getSize().getWidth () ) );
        properties.setProperty( HEIGHT              , String.valueOf( ( int ) MainForm.getInstance().getSize().getHeight() ) );

        properties.setProperty( DEST_DIR            , operationSettings.getOutputPath() );
        properties.setProperty( ENTITY_PACKAGE      , operationSettings.getEntityPackage() );
        properties.setProperty( INTERFACE_PACKAGE   , operationSettings.getDaoPackage() );
        properties.setProperty( MAPPING_PACKAGE     , operationSettings.getMapperPackage() );

        PropertiesFileController.saveSpecificProperties( operationSettings.getSourcePath(), properties );

        properties.setProperty( SOURCE_DIR          , operationSettings.getSourcePath() );

        PropertiesFileController.saveCommonProperties( properties );

    }




}