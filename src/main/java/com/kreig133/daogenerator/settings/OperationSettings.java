package com.kreig133.daogenerator.settings;

import com.kreig133.daogenerator.common.SourcePathChangeListener;

/**
 * @author eshangareev
 * @version 1.0
 */
public interface OperationSettings {
    String getOperationName();

    String getOutputPathForJavaClasses();
    
    String getPathForGeneratedSource();

    void setOutputPathForJavaClasses( String outputPath );
    
    int getFrameWidth();

    void setFrameWidth( int frameWidth );

    int getFrameHeight();

    void setFrameHeight( int frameHeight );

    String getEntityPackage();

    void setEntityPackage( String entityPackage );

    String getMapperPackage();

    void setMapperPackage( String mapperPackage );

    String getDaoPackage();

    void setDaoPackage( String daoPackage );

    String getSourcePath();

    void setSourcePath( String path );

    String getPathForGeneratedTests();

    String getPathForTestResources();

    void addSourcePathChangeListener( SourcePathChangeListener listener );
    
    String getProjectFolder();

    void setProjectFolder( String projectFolder );

    String getLastDirectory();

    public void setLastDirectory( String lastDirectory );
}
