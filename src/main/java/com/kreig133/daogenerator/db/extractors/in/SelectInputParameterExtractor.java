package com.kreig133.daogenerator.db.extractors.in;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.jaxb.*;
import com.kreig133.daogenerator.sql.creators.QueryCreator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author eshangareev
 * @version 1.0
 */
public class SelectInputParameterExtractor extends QueryInputParameterExtractor{

    private static SelectInputParameterExtractor INSTANCE;

    static SelectInputParameterExtractor instance(){
        if ( INSTANCE == null ) {
            INSTANCE = new SelectInputParameterExtractor();
        }
        return INSTANCE;
    }

    @Override
    public DaoMethod extractInputParams( DaoMethod daoMethod ) {
        final String query = daoMethod.getCommon().getQuery();



        daoMethod.getInputParametrs().getParameter().clear();
        for ( ParameterType parameterType : QueryCreator.extractInputParams( query ) ) {
            daoMethod.getInputParametrs().getParameter().add( parameterType );
        }

        return daoMethod;
    }

    @Override
    public DaoMethod fillTestValuesByInsertedQuery( DaoMethod daoMethod ) {
        return daoMethod;
    }
}
