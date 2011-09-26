package com.kreig133.daogenerator.files.parsers;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.settings.OperationSettings;

/**
 * @author eshangareev
 * @version 1.0
 */
public class TestParser implements IParser{

    final TestParamsParser testParamsParser = new TestParamsParser();
    final TestQueryParser  testQueryParser  = new TestQueryParser ();

    @Override
    public void parse( OperationSettings operationSettings, FunctionSettings functionSettings, String lineForParse ) {
        switch ( functionSettings.getTestInfoType() ){
            case TQUERY:
                testQueryParser.parse( operationSettings, functionSettings, lineForParse );
                break;
            case TPARAM:
            case TGEN:
                testParamsParser.parse( operationSettings,functionSettings, lineForParse );
                break;
        }
    }
}
