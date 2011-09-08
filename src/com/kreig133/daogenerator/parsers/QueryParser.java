package com.kreig133.daogenerator.parsers;

/**
 * @author eshangareev
 * @version 1.0
 */
public class QueryParser implements IParser<StringBuilder>{

    public void parse( StringBuilder input, String lineForParse ) {
        input.append( lineForParse ).append( "\n" );
    }
}
