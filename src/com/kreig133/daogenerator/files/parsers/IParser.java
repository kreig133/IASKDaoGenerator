package com.kreig133.daogenerator.files.parsers;

import com.kreig133.daogenerator.common.settings.OperationSettings;

/**
 * @author eshangareev
 * @version 1.0
 */
public interface IParser<T> {
    void parse( OperationSettings operationSettings, T input, String lineForParse );
}
