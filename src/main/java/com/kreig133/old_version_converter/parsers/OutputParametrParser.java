package com.kreig133.old_version_converter.parsers;

import com.kreig133.daogenerator.DaoGenerator;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.InOutType;
import com.kreig133.daogenerator.jaxb.JavaType;
import com.kreig133.daogenerator.jaxb.ParameterType;

import java.util.List;

import static com.kreig133.old_version_converter.parsers.settings.SettingsReader.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class OutputParametrParser implements IParser{

    public void parse(
            DaoMethod daoMethod ,
            String lineForParse
    ) {
        List<ParameterType> output = daoMethod.getOutputParametrs().getParameter();
        final String[] params =  lineForParse.split( "\t" );

        String name;
        String type;
        String comment = null;

        String suffix = InOutType.OUT.toString();
        Integer placeOfParam;

        placeOfParam = DaoGenerator.getCurrentOperationSettings().getPlaceOfParameter( NAME + suffix );
        ParsersUtils.checkPlaceOfParameter( true, params, placeOfParam );
        name = params[ placeOfParam ];

        placeOfParam = DaoGenerator.getCurrentOperationSettings().getPlaceOfParameter( TYPE + suffix );
        ParsersUtils.checkPlaceOfParameter( true, params, placeOfParam );
        type = params[ placeOfParam ];

        placeOfParam = DaoGenerator.getCurrentOperationSettings().getPlaceOfParameter( COMMENT + suffix );
        if( placeOfParam != null && params.length > placeOfParam ){
            comment = params[ placeOfParam ];
        }

        final ParameterType e = new ParameterType();
        e.setName( name );
        e.setType( JavaType.getBySqlType( type ) );
        e.setComment( comment );

        output.add( e );
    }
}
