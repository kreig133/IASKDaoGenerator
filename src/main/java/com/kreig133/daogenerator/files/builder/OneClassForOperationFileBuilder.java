package com.kreig133.daogenerator.files.builder;

import com.kreig133.daogenerator.files.JavaClassGenerator;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
abstract public class OneClassForOperationFileBuilder extends FileBuilder {
    @Override
    public void generateBody( @NotNull List<DaoMethod> daoMethods ) {
        for ( DaoMethod daoMethod : daoMethods ) {
            for ( JavaClassGenerator generator : generators ) {
                generator.generateBody( daoMethod );
            }
        }
    }
}
