package com.kreig133.daogenerator.files.builder;

import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.settings.Settings;

/**
 * @author eshangareev
 * @version 1.0
 */
abstract public class ParameterClassBuilder extends OneClassForEachMethodFileBuilder {

    public static FileBuilder newInstance() {
        switch ( Settings.settings().getType() ) {
            case DEPO:
                return new DepoParameterClassBuilder();
            default:
                throw new IllegalArgumentException( "Не рализовано" );
        }
    }
}
