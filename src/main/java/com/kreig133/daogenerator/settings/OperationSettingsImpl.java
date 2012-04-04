package com.kreig133.daogenerator.settings;

import com.kreig133.daogenerator.common.SourcePathChangeListener;
import com.kreig133.daogenerator.common.TypeChangeListener;
import com.kreig133.daogenerator.enums.Type;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author eshangareev
 * @version 1.0
 */
final public class OperationSettingsImpl implements OperationSettings{

    private List<TypeChangeListener> typeChangelisteners = new ArrayList<TypeChangeListener>();
    private List<SourcePathChangeListener> sourcePathChangeListeners = new ArrayList<SourcePathChangeListener>();

    private Type TYPE = Type.TEST;

    private String ENTITY_PACKAGE = "ru.sbrf.aplana.entity";
    private String DAO_PACKAGE = "ru.sbrf.aplana.dao";
    private String MAPPER_PACKAGE = "ru.sbrf.aplana.data";
    private String OUTPUT_PATH_FOR_JAVA_CLASSES = "D:\\GeneratedDao";
    private String SOURCE_PATH = "";
    private String PROJECT_FOLDER = "";
    private int    FRAME_WIDHT = 1200;
    private int    FRAME_HEIGHT = 800;
    private String LAST_DIRECTORY = "C:\\";

    @Override
    public Type getType() {
        return TYPE;
    }

    @Override
    public String getOutputPathForJavaClasses() {
        return OUTPUT_PATH_FOR_JAVA_CLASSES;
    }

    @Override
    public String getPathForGeneratedSource() {
        return OUTPUT_PATH_FOR_JAVA_CLASSES + "\\src\\main\\java";
    }

    @Override
    public String getPathForGeneratedTests() {
        return OUTPUT_PATH_FOR_JAVA_CLASSES + "\\src\\test\\java";
    }

    @Override
    public String getPathForTestResources() {
        return OUTPUT_PATH_FOR_JAVA_CLASSES + "\\src\\test\\resources";
    }

    @Override
    public void addTypeChangeListener( TypeChangeListener listener ) {
        typeChangelisteners.add( listener );
    }

    @Override
    public String getOperationName() {
        return new File( SOURCE_PATH ).getName();
    }

    @Override
    public String getEntityPackage() {
        return ENTITY_PACKAGE;
    }

    @Override
    public String getMapperPackage() {
        return MAPPER_PACKAGE;
    }

    @Override
    public String getDaoPackage() {
        return DAO_PACKAGE;
    }

    @Override
    public String getSourcePath() {
        if( SOURCE_PATH == null ){
            throw new RuntimeException( "SOURCE_PATH еще не установлен." );
        }
        return SOURCE_PATH;
    }

    @Override
    public void setType( Type type ) {
        if( TYPE != type ){
            this.TYPE = type;

            for ( TypeChangeListener listener : typeChangelisteners ) {
                listener.typeChanged();
            }
        }
    }

    @Override
    public void setOutputPathForJavaClasses( String outputPath ) {
        OUTPUT_PATH_FOR_JAVA_CLASSES = outputPath;
    }

    @Override
    public int getFrameWidth() {
        return FRAME_WIDHT;
    }

    @Override
    public void setFrameWidth( int frameWidth ) {
        FRAME_WIDHT = frameWidth;
    }

    @Override
    public int getFrameHeight() {
        return FRAME_HEIGHT;
    }

    @Override
    public void setFrameHeight( int frameHeight ) {
        FRAME_HEIGHT = frameHeight;
    }

    @Override
    public void setEntityPackage( String entityPackage ) {
        ENTITY_PACKAGE = entityPackage;
    }

    @Override
    public void setMapperPackage( String mapperPackage ) {
        MAPPER_PACKAGE = mapperPackage;
    }

    @Override
    public void setDaoPackage( String daoPackage ) {
        DAO_PACKAGE = daoPackage;
    }

    @Override
    public void setSourcePath( String path ) {
        if ( ! SOURCE_PATH.equals( path ) ) {

            SOURCE_PATH = path;

            final Properties propertiesFromSourceDir =
                    PropertiesFileController.getPropertiesFromSourceDir( SOURCE_PATH );

            if ( propertiesFromSourceDir != null ) {
                Settings.loadSettingsFromProperties( propertiesFromSourceDir );
            }

            for ( SourcePathChangeListener sourcePathChangeListener : sourcePathChangeListeners ) {
                sourcePathChangeListener.sourcePathChanged();
            }
        }
    }

    @Override
    public void addSourcePathChangeListener( SourcePathChangeListener listener ) {
        sourcePathChangeListeners.add( listener );
    }

    @Override
    public String getProjectFolder() {
        return PROJECT_FOLDER;
    }

    @Override
    public void setProjectFolder( String projectFolder ) {
        PROJECT_FOLDER = projectFolder;
    }

    @Override
    public String getLastDirectory() {
        return LAST_DIRECTORY;
    }

    @Override
    public void setLastDirectory( String lastDirectory ) {
        LAST_DIRECTORY = lastDirectory;
    }
}
