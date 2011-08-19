package com.kreig133.daogenerator.parsers;

import com.kreig133.daogenerator.parametr.InputParameter;
import com.kreig133.daogenerator.parametr.Parameter;

import java.util.List;
/**
 * @author eshangareev
 * @version 1.0
 */
public class InputParameterParser implements IParser<List<Parameter>>{

    public void parse(
            List<Parameter> input,
            String lineForParse
    ) {
        final String[] params =  lineForParse.split( "\t" );

        if (!(params[2] == null || "".equals(params[2]))) {
            input.add(
                    new InputParameter(
                            params[ 2 ],
                            params[ 3 ],
                            params.length > 4 ? params[ 4 ] : null,
                            params.length > 5 ? params[ 5 ] : null
                    )
            );
        }
    }
}
