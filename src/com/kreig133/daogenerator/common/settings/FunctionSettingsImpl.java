package com.kreig133.daogenerator.common.settings;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.enums.ReturnType;
import com.kreig133.daogenerator.enums.SelectType;
import com.kreig133.daogenerator.enums.TestInfoType;
import com.kreig133.daogenerator.parameter.Parameter;
import com.kreig133.daogenerator.sql.wrappers.GenerateGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author eshangareev
 * @version 1.0
 */
public class FunctionSettingsImpl implements FunctionSettings {

    private final OperationSettings OPERATION_SETTING;
    private final List<Parameter>  INPUT_PARAMETER_LIST = new ArrayList<Parameter>();
    private final List<Parameter> OUTPUT_PARAMETER_LIST = new ArrayList<Parameter>();

    private final Map<Integer, String> TEST_PARAM = new HashMap<Integer, String>();

    private final StringBuilder QUERY         = new StringBuilder();
    private final StringBuilder TEST_QUERY    = new StringBuilder();

    private String MY_BATIS_QUERY;

    private String          NAME;
    private String          NAME_FOR_CALL;
    private SelectType      SELECT_TYPE  ;
    private ReturnType      RETURN_TYPE  ;
    private TestInfoType    TEST_INFO_TYPE = TestInfoType.NONE;

    public FunctionSettingsImpl( OperationSettings operationSettings ) {
        OPERATION_SETTING = operationSettings;
    }

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
    public String getNameForCall() {
        return NAME_FOR_CALL;
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
        switch ( TEST_INFO_TYPE ){
            case TQUERY:
                return TEST_QUERY.toString();
            case TGEN:
                final String query = GenerateGenerator.generateWrapper( this, true );
                return Utils.replaceQuestionMarkWithStrings( this, query );
            case TPARAM:
                switch ( SELECT_TYPE ){
                    case CALL:

                    default:
                        return Utils.replaceQuestionMarkWithStrings( this, TEST_QUERY.toString() );
                }

            case NONE:
                throw new AssertionError( "Ошибка! Строка для тестирования не задана." );
        }
        throw new AssertionError();
    }

    @Override
    public TestInfoType getTestInfoType() {
        return TEST_INFO_TYPE;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Map<Integer, String> getTestParams() {
        return TEST_PARAM;
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
    public void setName( String functionName ) {
        if( NAME_FOR_CALL == null ){
            NAME_FOR_CALL = functionName;
            NAME = Utils.convertPBNameToName( functionName );
        } else {
            throw new AssertionError( "NAME_FOR_CALL уже был устновлен!" );
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
    public void appendToQueryForTesting( String appendToQuery ) {
        TEST_QUERY.append( appendToQuery );
    }

    @Override
    public void setTestInfoType( TestInfoType testInfoType ) {
        TEST_INFO_TYPE = testInfoType;
    }
}
