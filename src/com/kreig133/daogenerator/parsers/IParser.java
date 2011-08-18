package com.kreig133.daogenerator.parsers;

/**
 * @author eshangareev
 * @version 1.0
 */
public interface IParser<T> {
    void parse( T input, String lineForParse );
}
