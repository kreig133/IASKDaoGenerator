package com.kreig133.daogenerator.files.parsers;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.settings.OperationSettings;

/**
 * @author eshangareev
 * @version 1.0
 */
public class TestParser implements IParser<FunctionSettings>{

    TestParamsParser testParamsParser = new TestParamsParser();
    TestQueryParser  testQueryParser  = new TestQueryParser ();

    @Override
    public void parse( OperationSettings operationSettings, FunctionSettings input, String lineForParse ) {
        switch ( input.getTestInfoType() ){
            case TQUERY:
                testQueryParser.parse( operationSettings, input, lineForParse );
                break;
            case TPARAM:
                testParamsParser.parse( operationSettings,input, lineForParse );
        }
    }
}
