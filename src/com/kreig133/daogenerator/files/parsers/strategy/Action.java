package com.kreig133.daogenerator.files.parsers.strategy;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.settings.OperationSettings;

/**
 * @author eshangareev
 * @version 1.0
 */
public interface Action{
    public void doAction(
            String[] strings,
            Integer place,
            OperationSettings operationSettings,
            FunctionSettings functionSettings);
}
