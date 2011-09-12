package com.kreig133.daogenerator.common.settings;

import com.kreig133.daogenerator.enums.Type;

/**
 * @author eshangareev
 * @version 1.0
 */
public interface OperationSettings {
    Type getType();

    void setType( Type type );

    String getOperationName();

    void setOperationName(String operationName );

    String getOutputPath();

    void setOutputPath( String outputPath );

    String getEntityPackage();

    void setEntityPackage( String entityPackage );

    String getMapperPackage();

    void setMapperPackage( String mapperPackage );

    String getDaoPackage();

    void setDaoPackage( String daoPackage );

    String getSourcePath();

    void setSourcePath( String path );

    boolean skipTesting();

    void setSkipTesting( boolean skipTesting );
}
