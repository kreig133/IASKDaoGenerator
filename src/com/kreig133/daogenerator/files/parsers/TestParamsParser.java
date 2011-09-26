package com.kreig133.daogenerator.files.parsers;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.settings.OperationSettings;

/**
 * @author eshangareev
 * @version 1.0
 */
public class TestParamsParser implements IParser{
    @Override
    public void parse( OperationSettings operationSettings,  FunctionSettings input, String lineForParse ) {
        if( lineForParse.trim().length() == 0  ) return;
        final String[] split = lineForParse.trim().split( "\t" );
        assert split.length == 2;

        input.getTestParams().put( new Integer( split[0].trim() ), split[1].trim() );
    }
}
