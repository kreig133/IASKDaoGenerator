package com.kreig133.daogenerator;

import com.kreig133.daogenerator.enums.ReturnType;
import com.kreig133.daogenerator.enums.SelectType;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.parametr.Parameter;
import com.sun.org.apache.bcel.internal.generic.Select;

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
}
