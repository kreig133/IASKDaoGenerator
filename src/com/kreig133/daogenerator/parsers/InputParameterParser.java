package com.kreig133.daogenerator.parsers;

import com.kreig133.daogenerator.parametr.InputParameter;
import com.kreig133.daogenerator.parametr.Parameter;

import java.util.List;
/**
 * @author eshangareev
 * @version 1.0
 */
public class InputParameterParser implements IParser<List<Parameter>>{

    public void parse( List<Parameter> input, String lineForParse ) {
        final String[] params =  lineForParse.split( ";" );

        if (!(params[1] == null || "".equals(params[1]))) {
            input.add(
                    new InputParameter(
                            params[ 1 ],
                            params[ 2 ],
                            params.length >= 5 ? params[ 4 ] : null,
                            params.length == 6 ? params[ 5 ] : null
                    )
            );
        }
    }
}
