package com.kreig133.daogenerator.files.builder;

import com.kreig133.daogenerator.files.JavaClassGenerator;
import com.kreig133.daogenerator.files.mybatis.model.ModelClassGenerator;
import com.kreig133.daogenerator.jaxb.DaoMethod;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author eshangareev
 * @version 1.0
 */
class DepoParameterClassBuilder extends ParameterClassBuilder{
    @Override
    protected void prepareBuilder( List<DaoMethod> daoMethod ) {
        for ( DaoMethod method : daoMethod ) {
            generators.add( ModelClassGenerator.newInstance( method.getInputParametrs () ) );
            generators.add( ModelClassGenerator.newInstance( method.getOutputParametrs() ) );
        }
    }
}
