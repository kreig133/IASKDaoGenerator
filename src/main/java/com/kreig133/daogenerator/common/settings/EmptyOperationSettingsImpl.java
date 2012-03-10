package com.kreig133.daogenerator.common.settings;

import com.kreig133.daogenerator.enums.Type;

import java.io.File;
import java.util.Map;

/**
 * @author eshangareev
 * @version 1.0
 */
public class EmptyOperationSettingsImpl implements OperationSettings{
    @Override
    public Type getType() {
        return null;
    }

    @Override
    public void setType( Type type ) { }

    @Override
    public String getOperationName() {
        return null;
    }

    @Override
    public void setOperationName( String operationName ) { }

    @Override
    public String getOutputPath() {
        return null;
    }

    @Override
    public void setOutputPath( String outputPath ) { }

    @Override
    public String getEntityPackage() {
        return null;
    }

    @Override
    public void setEntityPackage( String entityPackage ) { }

    @Override
    public String getMapperPackage() {
        return null;
    }

    @Override
    public void setMapperPackage( String mapperPackage ) { }

    @Override
    public String getDaoPackage() {
        return null;
    }

    @Override
    public void setDaoPackage( String daoPackage ) { }

    @Override
    public String getSourcePath() {
        return null;
    }

    @Override
    public void setSourcePath( String path ) {}

    @Override
    public boolean skipTesting() {
        return false;
    }

    @Override
    public void setSkipTesting( boolean skipTesting ) { }

}
