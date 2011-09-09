package com.kreig133.daogenerator.files.parsers;

import com.kreig133.daogenerator.parameter.OutputParameter;
import com.kreig133.daogenerator.parameter.Parameter;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class OutputParametrParser implements IParser<List<Parameter>>{

    public void parse(
            List<Parameter> input,
            String lineForParse
    ) {
        final String[] params =  lineForParse.split( "\t" );

        if (!(params[2] == null || "".equals(params[2]))) {
            input.add(
                    new OutputParameter(
                            params.length >= 5 ? params[ 4 ] : null,
                            params[ 3 ],
                            params[ 2 ] )
            );
        }
    }
}
