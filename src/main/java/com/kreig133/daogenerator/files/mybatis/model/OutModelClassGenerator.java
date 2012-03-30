package com.kreig133.daogenerator.files.mybatis.model;

import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.jaxb.ParametersType;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class OutModelClassGenerator extends ModelClassGenerator {
    OutModelClassGenerator( ParametersType parametersType ) {
        super( parametersType );
    }

    @Override
    protected List<ParameterType> filter( List<ParameterType> parameter ) {
        return parameter;
    }

    public static ModelClassGenerator newInstance( DaoMethod daoMethod ){
        return new OutModelClassGenerator( daoMethod.getOutputParametrs() );
    }
}
