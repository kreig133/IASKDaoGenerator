package com.kreig133.daogenerator.db.extractors.in;

import com.kreig133.daogenerator.jaxb.DaoMethod;

/**
 * @author eshangareev
 * @version 1.0
 */
public class DefaultInputParameterExtractor extends InputParameterExtractor{
    @Override
    public DaoMethod extractInputParams( DaoMethod daoMethod ) {
        throw new RuntimeException();
    }

    @Override
    public DaoMethod fillTestValuesByInsertedQuery( DaoMethod daoMethod ) {
        throw new RuntimeException();
    }

    private static DefaultInputParameterExtractor INSTANCE;

    static DefaultInputParameterExtractor instance(){
        if ( INSTANCE == null ) {
            INSTANCE = new DefaultInputParameterExtractor();
        }
        return INSTANCE;
    }

}
