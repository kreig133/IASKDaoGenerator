package com.kreig133.daogenerator.files.parsers;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.enums.InputOrOutputType;
import com.kreig133.daogenerator.parameter.InputParameter;
import com.kreig133.daogenerator.parameter.Parameter;

import javax.swing.text.html.parser.Parser;
import java.util.List;

import static com.kreig133.daogenerator.files.parsers.settings.SettingsReader.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class InputParameterParser implements IParser<List<Parameter>>{

    public void parse(
            OperationSettings operationSettings,
            List<Parameter> input,
            String lineForParse
    ) {
        final String[] params =  lineForParse.split( "\t" );

        String name;
        String type;
        String defaultValue = null;
        String comment = null;
        InputOrOutputType inOutType = InputOrOutputType.OUT;

        String suffix = InputOrOutputType.IN.toString();
        Integer placeOfParam;

        placeOfParam = operationSettings.getPlaceOfParameter( NAME + suffix );
        ParsersUtils.checkPlaceOfParameter( true, params, placeOfParam );
        name = params[ placeOfParam ];

        placeOfParam = operationSettings.getPlaceOfParameter( TYPE + suffix );
        ParsersUtils.checkPlaceOfParameter( true, params, placeOfParam );
        type = params[ placeOfParam ];

        placeOfParam = operationSettings.getPlaceOfParameter( DEFAULT + suffix );
        if( placeOfParam != null && params.length > placeOfParam ){
            defaultValue = params[ placeOfParam ];
        }

        placeOfParam = operationSettings.getPlaceOfParameter( COMMENT + suffix );
        if( placeOfParam != null && params.length > placeOfParam ){
            comment = params[ placeOfParam ];
        }

        placeOfParam = operationSettings.getPlaceOfParameter( IN_OUT + suffix );
        if( placeOfParam != null ){
            inOutType = InputOrOutputType.getByName( params[ placeOfParam ] );
        }

        input.add(
                new InputParameter(
                        name,
                        type,
                        defaultValue,
                        comment,
                        inOutType
                )
        );
    }


}
