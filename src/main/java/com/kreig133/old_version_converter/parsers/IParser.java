package com.kreig133.old_version_converter.parsers;

import com.kreig133.daogenerator.jaxb.DaoMethod;

/**
 * @author eshangareev
 * @version 1.0
 */
public interface IParser {
    void parse( DaoMethod daoMethod, String lineForParse );
}
