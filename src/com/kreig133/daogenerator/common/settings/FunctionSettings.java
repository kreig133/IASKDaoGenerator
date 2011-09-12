package com.kreig133.daogenerator.common.settings;

import com.kreig133.daogenerator.enums.ReturnType;
import com.kreig133.daogenerator.enums.SelectType;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.parameter.Parameter;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public interface FunctionSettings {


    SelectType getSelectType();

    void setSelectType( SelectType selectType );

    List<Parameter> getInputParameterList();

    List<Parameter> getOutputParameterList();

    StringBuilder getSelectQuery();

    String getMyBatisQuery();

    void setMyBatisQuery( String myBatisQuery );

    String getQueryForTesting();

    void setQueryForTesting( String queryForTesting );

    String getFunctionName();

    void setFunctionName( String functionName );

    ReturnType getReturnType();

    void setReturnType( ReturnType returnType );

}
