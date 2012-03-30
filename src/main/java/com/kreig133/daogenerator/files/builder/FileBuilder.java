package com.kreig133.daogenerator.files.builder;

import com.kreig133.daogenerator.files.JavaClassGenerator;
import com.kreig133.daogenerator.jaxb.DaoMethod;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author eshangareev
 * @version 1.0
 */
public abstract class FileBuilder {
    protected List<JavaClassGenerator> generators = new ArrayList<JavaClassGenerator>();

    final public Map<File, String> build( List<DaoMethod> daoMethod ) {
        prepareBuilder( daoMethod );
        generateHead();
        generateBody( daoMethod );

        HashMap<File, String> result = new HashMap<File, String>();

        for ( JavaClassGenerator generator : generators ) {
            result.put( generator.getFile(), generator.getResult() );
            generator.reset();
        }

        return result;
    }

    protected abstract void prepareBuilder( List<DaoMethod> daoMethod );

    final protected void generateHead(){
        for ( JavaClassGenerator generator : generators ) {
            generator.generateHead();
        }
    }

    public abstract void generateBody( List<DaoMethod> daoMethods );
}
