package com.kreig133.daogenerator.files.builder;

import com.kreig133.daogenerator.files.JavaClassGenerator;
import com.kreig133.daogenerator.files.mybatis.implementation.ImplementationGenerator;
import com.kreig133.daogenerator.files.mybatis.intrface.InterfaceGenerator;
import com.kreig133.daogenerator.files.mybatis.mapping.MappingGenerator;
import com.kreig133.daogenerator.files.mybatis.test.TesterClassGenerator;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class MapperFileBuilder extends FileBuilder {

    @Override
    public void generateBody( @NotNull List<DaoMethod> daoMethods ) {
        for ( DaoMethod daoMethod : daoMethods ) {
            for ( JavaClassGenerator generator : generators ) {
                generator.generateBody( daoMethod );
            }
        }
    }

    @NotNull
    public static FileBuilder newInstance() {
        return new MapperFileBuilder();
    }

    @Override
    protected void prepareBuilder( List<DaoMethod> daoMethod ) {
        generators.add( MappingGenerator        .instance() );
        generators.add( InterfaceGenerator      .instance() );
        generators.add( ImplementationGenerator .instance() );
        generators.add( TesterClassGenerator    .instance() );
    }
}
