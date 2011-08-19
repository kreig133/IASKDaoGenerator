package com.kreig133.daogenerator.parsers;

import com.kreig133.daogenerator.Utils;
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

        int i = Utils.stringNotEmpty( params[1])? 1 : ( Utils.stringNotEmpty( params[2]) ? 2 : Integer.MIN_VALUE );

        input.add(
                new InputParameter(
                        params[ i ],
                        params[ i+1 ],
                        params.length > i+2 ? params[ i+2 ] : null,
                        params.length > i+3 ? params[ i+2 ] : null
                )
        );
    }


}
