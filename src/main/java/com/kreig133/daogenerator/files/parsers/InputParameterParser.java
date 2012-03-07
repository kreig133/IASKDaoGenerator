package com.kreig133.daogenerator.files.parsers;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.jaxb.InOutType;
import com.kreig133.daogenerator.parameter.InputParameter;
import com.kreig133.daogenerator.parameter.Parameter;

import java.util.List;

import static com.kreig133.daogenerator.files.parsers.settings.SettingsReader.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class InputParameterParser implements IParser{

    public void parse(
            OperationSettings operationSettings,
            FunctionSettings  functionSettings ,
            String lineForParse
    ) {
        List<Parameter> input = functionSettings.getInputParameterList();
        final String[] params =  lineForParse.split( "\t" );

        String name;
        String type;
        String defaultValue = null;
        String comment = null;
        InOutType inOutType = InOutType.OUT;

        String suffix = InOutType.IN.toString();
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
            inOutType = InOutType.getByName( params[ placeOfParam ] );
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
