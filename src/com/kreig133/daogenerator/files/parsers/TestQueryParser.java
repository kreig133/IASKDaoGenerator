package com.kreig133.daogenerator.files.parsers;

import com.kreig133.daogenerator.common.settings.FunctionSettings;

/**
 * @author eshangareev
 * @version 1.0
 */
public class TestQueryParser implements IParser<FunctionSettings>{
    @Override
    public void parse( FunctionSettings input, String lineForParse ) {
        input.appendToQueryForTesting( lineForParse + "\n" );
    }
}
