package com.kreig133.daogenerator.sql;

import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.sql.wrappers.GenerateGenerator;
import com.kreig133.daogenerator.sql.wrappers.GeneroutGenerator;

/**
 * @author eshangareev
 * @version 1.0
 */
public class SqlQueryCreator {
    public static String createQueries( DaoMethod daoMethod, boolean forTest) {
        switch ( daoMethod.getCommon().getConfiguration().getType() ){
            case CALL:
                return ProcedureCallCreator.generateProcedureCall( daoMethod, forTest );
            case GENERATE:
                return GenerateGenerator.generateWrapper( daoMethod );
            case GENEROUT:
                return GeneroutGenerator.generateWrapper( daoMethod );
            default:
                return SelectQueryConverter.processSelectQueryString( daoMethod );
        }
    }
}