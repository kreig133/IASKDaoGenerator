package com.kreig133.daogenerator.files.parsers;

import com.kreig133.daogenerator.common.settings.OperationSettings;

/**
 * @author eshangareev
 * @version 1.0
 */
public class SimpleParser implements IParser<StringBuilder>{

    public void parse( OperationSettings operationSettings, StringBuilder input, String lineForParse ) {
        input.append( lineForParse ).append( "\n" );
    }
}
