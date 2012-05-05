package com.kreig133.daogenerator.files.builder;

import com.kreig133.daogenerator.files.mybatis.mapping.DepoMappingGenerator;
import com.kreig133.daogenerator.files.mybatis.mapping.MappingGenerator;
import com.kreig133.daogenerator.files.mybatis.test.TesterClassGenerator;
import com.kreig133.daogenerator.jaxb.DaoMethod;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class DepoMapperFileBuilder extends OneClassForOperationFileBuilder {
    @Override
    protected void prepareBuilder( List<DaoMethod> daoMethod ) {
        generators.add( MappingGenerator.instance() );
        generators.add( TesterClassGenerator.instance() );
    }
}
