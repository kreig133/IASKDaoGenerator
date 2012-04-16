package com.kreig133.daogenerator.sql.creators;

import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.SelectType;
import org.jetbrains.annotations.NotNull;

/**
 * @author eshangareev
 * @version 1.0
 */
public class QueryCreatorFactory {
    @NotNull
    public static QueryCreator newInstance( @NotNull DaoMethod daoMethod ){
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
