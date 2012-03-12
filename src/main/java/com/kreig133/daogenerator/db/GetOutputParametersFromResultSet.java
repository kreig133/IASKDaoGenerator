package com.kreig133.daogenerator.db;

import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.sql.SqlQueryCreator;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class GetOutputParametersFromResultSet {

    public static void getOutputParameters( DaoMethod daoMethod ){
        final String queries = SqlQueryCreator.createQueries( daoMethod, true );


    }

}

