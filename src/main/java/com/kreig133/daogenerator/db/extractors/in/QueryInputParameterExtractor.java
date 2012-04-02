package com.kreig133.daogenerator.db.extractors.in;

import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.SelectType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.kreig133.daogenerator.db.extractors.TableNameHelper.getTableName;

/**
 * @author eshangareev
 * @version 1.0
 */
abstract public class QueryInputParameterExtractor extends InputParameterExtractor{
    
    @Override
    public DaoMethod fillMethodName( DaoMethod daoMethod ) {
        SelectType type = daoMethod.getSelectType();
        daoMethod.getCommon().setMethodName(
                type.name().toLowerCase() + type.keyWord() + getTableName( daoMethod )
        );
        return daoMethod;
    }
}
