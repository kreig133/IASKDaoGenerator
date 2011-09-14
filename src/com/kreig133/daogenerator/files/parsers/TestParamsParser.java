package com.kreig133.daogenerator.files.parsers;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.settings.OperationSettings;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class TestParamsParser implements IParser<FunctionSettings>{
    @Override
    public void parse( OperationSettings operationSettings,  FunctionSettings input, String lineForParse ) {
        input.addToTestParams( lineForParse.trim() );
    }
}
