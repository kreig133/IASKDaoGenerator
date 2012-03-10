package com.kreig133.old_version_converter.parsers;

import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.jaxb.DaoMethod;

/**
 * @author eshangareev
 * @version 1.0
 */
public class SelectQueryParser implements IParser{

    public void parse(
            DaoMethod daoMethod ,
            String lineForParse
    ) {
        daoMethod.getCommon().setQuery( daoMethod.getCommon().getQuery() + lineForParse + "\n" );
    }
}
