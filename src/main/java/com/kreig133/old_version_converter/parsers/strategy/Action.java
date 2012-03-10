package com.kreig133.old_version_converter.parsers.strategy;

import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.jaxb.DaoMethod;

/**
 * @author eshangareev
 * @version 1.0
 */
public interface Action{
    public void doAction(
            String[] strings,
            Integer place,
            DaoMethod daoMethod
    );
}
