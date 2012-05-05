package com.kreig133.daogenerator.files.builder;

import com.kreig133.daogenerator.settings.Settings;
import org.jetbrains.annotations.NotNull;

/**
 * @author eshangareev
 * @version 1.0
 */
abstract public class ParameterClassBuilder extends OneClassForEachMethodFileBuilder {

    @NotNull
    public static FileBuilder newInstance() {
        return new DepoParameterClassBuilder();
    }
}
