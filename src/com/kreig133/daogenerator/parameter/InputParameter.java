package com.kreig133.daogenerator.parameter;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.enums.InputOrOutputType;
import com.kreig133.daogenerator.enums.JavaType;

/**
 * @author eshangareev
 * @version 1.0
 */
public class InputParameter extends Parameter {
    private String defaultValue;
    private InputOrOutputType inputType;

    public InputParameter(String name, String type, String defaultValue, String comment, InputOrOutputType inputType) {
        super(comment, type, name);
        this.defaultValue = Utils.handleDefaultValue( defaultValue );
        this.inputType = inputType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public InputOrOutputType getInputType() {
        return inputType;
    }

    public void setInputType( InputOrOutputType inputType ) {
        this.inputType = inputType;
    }

    @Override
    public String toString() {

        return super.toString() +
                (
                        ( defaultValue == null || "".equals(defaultValue) )?
                            "" :
                            " = " + defaultValue.toLowerCase() +
                                    (
                                            type == JavaType.Long ?
                                                    ( "null".equals( defaultValue.toLowerCase() )?  "" :  "L" ) :
                                                     ""
                                    )
                ) + ";\n\n";
    }
}
