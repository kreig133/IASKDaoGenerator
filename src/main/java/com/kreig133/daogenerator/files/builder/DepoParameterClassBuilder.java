package com.kreig133.daogenerator.files.builder;

import com.kreig133.daogenerator.files.mybatis.DaoJavaClassGenerator;
import com.kreig133.daogenerator.files.mybatis.model.InModelClassGenerator;
import com.kreig133.daogenerator.files.mybatis.model.OutModelClassGenerator;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
class DepoParameterClassBuilder extends ParameterClassBuilder{
    @Override
    protected void prepareBuilder( @NotNull List<DaoMethod> daoMethod ) {
        for ( DaoMethod method : daoMethod ) {
            if( DaoJavaClassGenerator.checkToNeedOwnInClass( method ) )
                generators.add( InModelClassGenerator .newInstance( method ) );
            if( DaoJavaClassGenerator.checkToNeedOwnOutClass( method ) )
                generators.add( OutModelClassGenerator.newInstance( method ) );
        }
    }
}
