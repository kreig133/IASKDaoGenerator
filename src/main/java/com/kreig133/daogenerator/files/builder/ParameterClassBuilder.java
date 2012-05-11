package com.kreig133.daogenerator.files.builder;

import com.kreig133.daogenerator.files.JavaClassGenerator;
import com.kreig133.daogenerator.files.mybatis.DaoJavaClassGenerator;
import com.kreig133.daogenerator.files.mybatis.InOutClassGenerator;
import com.kreig133.daogenerator.files.mybatis.model.InModelClassGenerator;
import com.kreig133.daogenerator.files.mybatis.model.OutModelClassGenerator;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.InOutType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
class ParameterClassBuilder extends FileBuilder {

    @Override
    public void generateBody( List<DaoMethod> daoMethods ) {
        for ( JavaClassGenerator generator : generators ) {
            generator.generateBody( new DaoMethod() );
        }
    }

    @NotNull
    public static FileBuilder newInstance() {
        return new ParameterClassBuilder();
    }

    @Override
    protected void prepareBuilder( @NotNull List<DaoMethod> daoMethod ) {
        for ( DaoMethod method : daoMethod ) {
            if( DaoJavaClassGenerator.checkToNeedOwnInClass( method ) )
                generators.add( InOutClassGenerator.newInstance( method, InOutType.IN ) );

            if( DaoJavaClassGenerator.checkToNeedOwnOutClass( method ) )
                generators.add( InOutClassGenerator.newInstance( method, InOutType.OUT ) );
        }
    }
}
