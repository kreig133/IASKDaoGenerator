package com.kreig133.daogenerator.files.builder;

import com.kreig133.daogenerator.files.mybatis.model.InModelClassGenerator;
import com.kreig133.daogenerator.files.mybatis.model.OutModelClassGenerator;
import com.kreig133.daogenerator.jaxb.DaoMethod;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
class DepoParameterClassBuilder extends ParameterClassBuilder{
    @Override
    protected void prepareBuilder( List<DaoMethod> daoMethod ) {
        for ( DaoMethod method : daoMethod ) {
            generators.add( InModelClassGenerator .newInstance( method ) );
            generators.add( OutModelClassGenerator.newInstance( method ) );
        }
    }
}
