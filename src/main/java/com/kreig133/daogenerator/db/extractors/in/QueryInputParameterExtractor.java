package com.kreig133.daogenerator.db.extractors.in;

import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.SelectType;
import org.jetbrains.annotations.NotNull;

import static com.kreig133.daogenerator.db.extractors.TableNameHelper.getTableName;

/**
 * @author eshangareev
 * @version 1.0
 */
abstract public class QueryInputParameterExtractor extends InputParameterExtractor{
    
    @NotNull
    @Override
    public DaoMethod fillMethodName( @NotNull DaoMethod daoMethod ) {
        SelectType type = daoMethod.getSelectType();
        daoMethod.getCommon().setMethodName(
                type.name().toLowerCase() + type.keyWord() + getTableName( daoMethod )
        );
        return daoMethod;
    }
}
