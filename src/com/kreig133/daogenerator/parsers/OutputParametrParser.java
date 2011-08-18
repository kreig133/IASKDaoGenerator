package com.kreig133.daogenerator.parsers;

import com.kreig133.daogenerator.parametr.OutputParameter;
import com.kreig133.daogenerator.parametr.Parameter;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class OutputParametrParser implements IParser<List<Parameter>>{

    public void parse( List<Parameter> input, String lineForParse ) {
        final String[] params =  lineForParse.split( ";" );

        if (!(params[1] == null || "".equals(params[1]))) {
            input.add(
                    new OutputParameter(
                            params.length > 3 ? params[ 3 ] : null,
                            params[ 2 ], params[ 1 ] )
            );
        }
    }
}
