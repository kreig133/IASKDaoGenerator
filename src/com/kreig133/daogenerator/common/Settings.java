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

    SelectType getSelectType();

    List<Parameter> getInputParameterList();

    List<Parameter> getOutputParameterList();

    StringBuilder getSelectQuery();

    String getFunctionName();

    String getOperationName();

    ReturnType getReturnType();

    String getOutputPath();

    String getEntityPackage();

    String getMapperPackage();

    String getDaoPackage();
}
