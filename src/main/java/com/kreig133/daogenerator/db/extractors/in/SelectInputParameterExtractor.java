package com.kreig133.daogenerator.db.extractors.in;

import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.sql.creators.QueryCreator;
import org.jetbrains.annotations.NotNull;

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

    @NotNull
    @Override
    public DaoMethod extractInputParams( @NotNull DaoMethod daoMethod ) {
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
