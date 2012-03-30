package com.kreig133.daogenerator.files.builder;

import com.kreig133.daogenerator.files.JavaClassGenerator;
import com.kreig133.daogenerator.jaxb.DaoMethod;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author eshangareev
 * @version 1.0
 */
public abstract class FileBuilder {
    protected List<JavaClassGenerator> generators = new ArrayList<JavaClassGenerator>();

    public abstract Map<File, String> build( List<DaoMethod> daoMethod );
}
