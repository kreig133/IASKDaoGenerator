package com.kreig133.daogenerator.common.settings;

import com.kreig133.daogenerator.enums.ReturnType;
import com.kreig133.daogenerator.enums.SelectType;
import com.kreig133.daogenerator.parameter.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class FunctionSettingsImpl implements FunctionSettings {

    private final List<Parameter> INPUT_PARAMETER_LIST  = new ArrayList<Parameter>();
    private final List<Parameter> OUTPUT_PARAMETER_LIST = new ArrayList<Parameter>();

    private StringBuilder QUERY = new StringBuilder();

    private String MY_BATIS_QUERY;
    private String QUERY_FOR_TESTING;

    private String     FUNCTION_NAME;
    private SelectType SELECT_TYPE  ;
    private ReturnType RETURN_TYPE  ;

    @Override
    public SelectType getSelectType() {
        return SELECT_TYPE;
    }

    @Override
    public List<Parameter> getInputParameterList() {
        return INPUT_PARAMETER_LIST;
    }
    @Override
    public List<Parameter> getOutputParameterList() {
        return OUTPUT_PARAMETER_LIST;
    }
    @Override
    public StringBuilder getSelectQuery() {
        return QUERY;
    }
    @Override
    public String getFunctionName() {
        return FUNCTION_NAME;
    }

    @Override
    public ReturnType getReturnType() {
        return RETURN_TYPE;
    }

    @Override
    public String getMyBatisQuery() {
        if( MY_BATIS_QUERY == null ){
            throw new AssertionError( "MY_BATIS_QUERY еще не был установлен!" );
        }
        return MY_BATIS_QUERY;
    }

    @Override
    public String getQueryForTesting() {
        if( QUERY_FOR_TESTING == null ){
            throw new AssertionError( "QUERY_FOR_TESTING еще не был установлен!" );
        }
        return QUERY_FOR_TESTING;
    }

    @Override
    public void setSelectType( SelectType selectType ) {
        if( SELECT_TYPE == null ){
            this.SELECT_TYPE = selectType;
        } else {
            throw new AssertionError( "SELECTION_TYPE уже был устновлен!" );
        }
    }

    @Override
    public void setReturnType( ReturnType returnType ) {
        if( RETURN_TYPE == null ){
            RETURN_TYPE = returnType;
        } else {
            throw new AssertionError( "RETURN_TYPE уже был устновлен!" );
        }
    }

    @Override
    public void setFunctionName( String functionName ) {
        if( FUNCTION_NAME == null ){
            FUNCTION_NAME = functionName;
        } else {
            throw new AssertionError( "FUNCTION_NAME уже был устновлен!" );
        }
    }

    @Override
    public void setMyBatisQuery( String myBatisQuery ) {
        if( MY_BATIS_QUERY != null ){
            throw new AssertionError( "MY_BATIS_QUERY уже был установлен!" );
        }
        MY_BATIS_QUERY = myBatisQuery;
    }

    @Override
    public void setQueryForTesting( String queryForTesting ) {
        if( QUERY_FOR_TESTING != null ){
            throw new AssertionError( "QUERY_FOR_TESTING уже был установлен" );
        }
        QUERY_FOR_TESTING = queryForTesting;
    }
}
