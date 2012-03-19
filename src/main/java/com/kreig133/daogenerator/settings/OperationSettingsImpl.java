package com.kreig133.daogenerator.settings;

import com.kreig133.daogenerator.enums.Type;

/**
 * @author eshangareev
 * @version 1.0
 */
public class OperationSettingsImpl implements OperationSettings{
    private Type TYPE;

    private String OPERATION_NAME;
    private String ENTITY_PACKAGE;
    private String DAO_PACKAGE;
    private String MAPPER_PACKAGE;
    private String OUTPUT_PATH;
    private String SOURCE_PATH;

    @Override
    public Type getType() {
        return TYPE;
    }

    @Override
    public String getOutputPath() {
        return OUTPUT_PATH;
    }

    @Override
    public String getPathForGeneratedSource() {
        return OUTPUT_PATH + "\\src\\main\\java";
    }

    @Override
    public String getPathForGeneratedTests() {
        return OUTPUT_PATH + "\\src\\test\\java";
    }

    @Override
    public String getPathForTestResources() {
        return OUTPUT_PATH + "\\src\\test\\resources";
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
    public void setOutputPath( String outputPath ) {
        OUTPUT_PATH = outputPath;
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
