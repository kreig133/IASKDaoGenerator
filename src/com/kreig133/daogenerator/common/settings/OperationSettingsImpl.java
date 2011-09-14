package com.kreig133.daogenerator.common.settings;

import com.kreig133.daogenerator.enums.Type;

import java.io.File;
import java.util.Map;

/**
 * @author eshangareev
 * @version 1.0
 */
public class OperationSettingsImpl implements OperationSettings{
    private Type TYPE;
    private boolean SKIP_TESTING;


    private String OPERATION_NAME;
    private String ENTITY_PACKAGE;
    private String DAO_PACKAGE;
    private String MAPPER_PACKAGE;
    private String OUTPUT_PATH;
    private String SOURCE_PATH;

    private File FILE_WITH_SETTINGS;

    private Map<String, Integer> SETTINGS;

    @Override
    public Type getType() {
        return TYPE;
    }

    @Override
    public String getOutputPath() {
        return OUTPUT_PATH;
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
    public File getFileWithSettings() {
        return FILE_WITH_SETTINGS;
    }


    @Override
    public boolean skipTesting() {
        return SKIP_TESTING;
    }

    @Override
    public Integer getPlaceOfParameter( String parameterKey ) {
        if( SETTINGS == null ){
            throw new AssertionError( "SETTINGS еще не был установлен!" );
        }
        return SETTINGS.get( parameterKey );
    }

    @Override
    public void setType( Type type ) {
        if( TYPE == null ){
            this.TYPE = type;
        } else {
            throw new RuntimeException( "TYPE уже был установлен!" );
        }
    }

    @Override
    public void setOperationName( String operationName ) {
        if( OPERATION_NAME == null ){
            OPERATION_NAME = operationName;
        } else {
            throw new RuntimeException( "OPERATION_NAME уже установлен!" );
        }
    }

    @Override
    public void setOutputPath( String outputPath ) {
        if( OUTPUT_PATH == null ){
            OUTPUT_PATH = outputPath;
        } else {
            throw new RuntimeException( "OUTPUT_PATH уже установлен!" );
        }
    }

    @Override
    public void setEntityPackage( String entityPackage ) {
        if( ENTITY_PACKAGE == null ){
            ENTITY_PACKAGE = entityPackage;
        } else {
            throw new RuntimeException( "ENTITY_PACKAGE уже установлен!" );
        }
    }

    @Override
    public void setMapperPackage( String mapperPackage ) {
        if( MAPPER_PACKAGE == null ){
            MAPPER_PACKAGE = mapperPackage;
        } else {
            throw new RuntimeException( "MAPPER_PACKAGE уже установлен!" );
        }
    }

    @Override
    public void setDaoPackage( String daoPackage ) {
        if( DAO_PACKAGE == null ){
            DAO_PACKAGE = daoPackage;
        } else {
            throw new RuntimeException( "DAO_PACKAGE уже установлен!" );
        }
    }


    @Override
    public void setSourcePath( String path ) {
        if( SOURCE_PATH == null ){
            SOURCE_PATH = path;
        } else {
            throw new RuntimeException( "SOURCE_PATH уже установле!" );
        }
    }


    @Override
    public void setSkipTesting( boolean skipTesting ) {
        this.SKIP_TESTING = skipTesting;
    }

    @Override
    public void setFileWithSettings( File fileWithSettings ) {
        FILE_WITH_SETTINGS = fileWithSettings;
    }

    @Override
    public void setParameterPlaces( Map<String, Integer> settings ) {
        if( SETTINGS != null ){
            throw new AssertionError( "SETTINGS уже был установлен!" );
        }
        SETTINGS = settings;
    }
}
