package com.kreig133.daogenerator.files.builder;

import com.kreig133.daogenerator.files.mybatis.mapping.DepoMappingGenerator;
import com.kreig133.daogenerator.files.mybatis.test.TesterClassGenerator;
import com.kreig133.daogenerator.jaxb.DaoMethod;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class DepoMapperFileGenerator extends OneClassForOperationFileBuilder {
    @Override
    protected void prepareBuilder( List<DaoMethod> daoMethod ) {
        generators.add( DepoMappingGenerator.instance() );
        generators.add( TesterClassGenerator.instance() );
    }
}
