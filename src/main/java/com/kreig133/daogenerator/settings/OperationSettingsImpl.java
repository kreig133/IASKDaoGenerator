package com.kreig133.daogenerator.settings;

import com.kreig133.daogenerator.enums.Type;

/**
 * @author eshangareev
 * @version 1.0
 */
public class OperationSettingsImpl implements OperationSettings{
    private Type TYPE;

    private String OPERATION_NAME = "defaultOperationName";
    private String ENTITY_PACKAGE = "ru.sbrf.aplana.entity";
    private String DAO_PACKAGE = "ru.sbrf.aplana.dao";
    private String MAPPER_PACKAGE = "ru.sbrf.aplana.data";
    private String OUTPUT_PATH_FOR_JAVA_CLASSES = "D:\\GeneratedDao";
    private String SOURCE_PATH = "";
    private int    FRAME_WIDHT = 1200;
    private int    FRAME_HEIGHT = 800;

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
    public String getOperationName() {
        return OPERATION_NAME;
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
        this.TYPE = type;
    }

    @Override
    public void setOperationName( String operationName ) {
        OPERATION_NAME = operationName;
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
        SOURCE_PATH = path;
    }
}
