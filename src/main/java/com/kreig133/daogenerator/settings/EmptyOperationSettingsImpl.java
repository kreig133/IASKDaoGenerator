package com.kreig133.daogenerator.settings;

import com.kreig133.daogenerator.common.SourcePathChangeListener;
import com.kreig133.daogenerator.common.TypeChangeListener;
import com.kreig133.daogenerator.enums.Type;

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
    public String getPathForGeneratedSource() {
        return null;
    }

    @Override
    public void setType( Type type ) { }

    @Override
    public String getOperationName() {
        return null;
    }

    @Override
    public String getOutputPathForJavaClasses() {
        return null;
    }

    @Override
    public void setOutputPathForJavaClasses( String outputPath ) { }

    @Override
    public int getFrameWidth() {
        return 0;
    }

    @Override
    public void setFrameWidth( int frameWidthd ) { }

    @Override
    public int getFrameHeight() {
        return 0;
    }

    @Override
    public void setFrameHeight( int frameHeight ) { }

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
    public String getPathForGeneratedTests() {
        return null;
    }

    @Override
    public String getPathForTestResources() {
        return null;
    }

    @Override
    public void addTypeChangeListener( TypeChangeListener listener ) { }

    @Override
    public void addSourcePathChangeListener( SourcePathChangeListener listener ) { }

    @Override
    public String getModelPackage() {
        return null;
    }

    @Override
    public void setModelPackage( String modelPackage ) { }

    @Override
    public String getProjectFolder() {
        return null;
    }

    @Override
    public void setProjectFolder( String projectFolder ) { }
}
