package com.kreig133.daogenerator.sql;

import com.kreig133.daogenerator.jaxb.DaoMethod;

/**
 * @author eshangareev
 * @version 1.0
 */
public class SqlQueryCreator {
    public static String createQueries( DaoMethod daoMethod, boolean forTest) {
        switch ( daoMethod.getSelectType() ){
            case CALL:
                return ProcedureCallCreator.generateProcedureCall( daoMethod, forTest );
            default:
                return SelectQueryConverter.getSelectQueryString( daoMethod, false );
        }
    }
}
