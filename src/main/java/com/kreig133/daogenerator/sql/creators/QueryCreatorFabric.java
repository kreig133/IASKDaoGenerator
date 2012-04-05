package com.kreig133.daogenerator.sql.creators;

import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.SelectType;

/**
 * @author eshangareev
 * @version 1.0
 */
public class QueryCreatorFabric {
    public static QueryCreator newInstance( DaoMethod daoMethod ){
        if( daoMethod.getSelectType() == SelectType.CALL ){
            if(
                    daoMethod.getOutputParametrs().getIndexOfUnnamedParameters().size() > 1 ||
                            daoMethod.getOutputParametrs().containsSameNames()
                    ) {
                return new WrapperGenerator();
            }
            return  new ProcedureCallCreator();
        }

        return new SelectQueryCreator();
    }
}
