package com.kreig133.daogenerator.files.parsers;

import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.enums.InputOrOutputType;
import com.kreig133.daogenerator.parameter.OutputParameter;
import com.kreig133.daogenerator.parameter.Parameter;

import java.util.List;

import static com.kreig133.daogenerator.files.parsers.settings.SettingsReader.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class OutputParametrParser implements IParser<List<Parameter>>{

    public void parse(
            OperationSettings operationSettings,
            List<Parameter> input,
            String lineForParse
    ) {
        final String[] params =  lineForParse.split( "\t" );

        String name;
        String type;
        String comment = null;

        String suffix = InputOrOutputType.OUT.toString();
        Integer placeOfParam;

        placeOfParam = operationSettings.getPlaceOfParameter( NAME + suffix );
        ParsersUtils.checkPlaceOfParameter( true, params.length, placeOfParam );
        name = params[ placeOfParam ];

        placeOfParam = operationSettings.getPlaceOfParameter( TYPE + suffix );
        ParsersUtils.checkPlaceOfParameter( true, params.length, placeOfParam );
        type = params[ placeOfParam ];

        placeOfParam = operationSettings.getPlaceOfParameter( COMMENT + suffix );
        if( placeOfParam != null && params.length > placeOfParam ){
            comment = params[ placeOfParam ];
        }

        input.add(
                new OutputParameter(
                        comment,
                        type,
                        name )
        );
    }
}
