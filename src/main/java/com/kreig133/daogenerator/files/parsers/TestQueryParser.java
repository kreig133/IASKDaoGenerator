package com.kreig133.daogenerator.files.parsers;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.settings.OperationSettings;

/**
 * @author eshangareev
 * @version 1.0
 */
public class TestQueryParser implements IParser{
    @Override
    public void parse(
            OperationSettings operationSettings,
            FunctionSettings  functionSettings,
            String lineForParse
    ) {
        functionSettings.appendToQueryForTesting( lineForParse + "\n" );
    }
}
