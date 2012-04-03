package com.kreig133.daogenerator.files.builder;

import com.kreig133.daogenerator.settings.Settings;

/**
 * @author eshangareev
 * @version 1.0
 */
abstract public class MapperFileBuilder extends OneClassForOperationFileBuilder {
    public static FileBuilder newInstance() {
        switch ( Settings.settings().getType() ){
            case DEPO:
                return new DepoMapperFileGenerator();
            default:
                throw new IllegalArgumentException( "Не реализовано" );
        }
    }
}
