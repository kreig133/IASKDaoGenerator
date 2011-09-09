package com.kreig133.daogenerator.common;

import com.kreig133.daogenerator.enums.ReturnType;
import com.kreig133.daogenerator.enums.SelectType;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.parameter.Parameter;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public interface Settings {
    Type getType();

    void setType( Type type );

    SelectType getSelectType();

    void setSelectType( SelectType selectType );

    List<Parameter> getInputParameterList();

    List<Parameter> getOutputParameterList();

    StringBuilder getSelectQuery();

    void clearSelectQuery();

    String getFunctionName();

    void setFunctionName( String functionName );

    String getOperationName();

    void setOperationName(String operationName );

    ReturnType getReturnType();

    void setReturnType( ReturnType returnType );

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
}
