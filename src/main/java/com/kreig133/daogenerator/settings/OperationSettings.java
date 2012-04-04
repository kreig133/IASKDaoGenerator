package com.kreig133.daogenerator.settings;

import com.kreig133.daogenerator.common.SourcePathChangeListener;
import com.kreig133.daogenerator.common.TypeChangeListener;
import com.kreig133.daogenerator.enums.Type;

/**
 * @author eshangareev
 * @version 1.0
 */
public interface OperationSettings {
    Type getType();

    void setType( Type type );

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

    void addTypeChangeListener( TypeChangeListener listener );

    void addSourcePathChangeListener( SourcePathChangeListener listener );
    
    String getProjectFolder();

    void setProjectFolder( String projectFolder );

    String getLastDirectory();

    public void setLastDirectory( String lastDirectory );
}
