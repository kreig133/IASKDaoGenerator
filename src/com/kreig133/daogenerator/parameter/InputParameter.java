package com.kreig133.daogenerator.parameter;

import com.kreig133.daogenerator.Utils;
import com.kreig133.daogenerator.enums.InputParameterType;

/**
 * @author eshangareev
 * @version 1.0
 */
public class InputParameter extends Parameter {
    private String defaultValue;
    private InputParameterType inputType;

    public InputParameter(String name, String type, String defaultValue, String comment, InputParameterType inputType) {
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

    public InputParameterType getInputType() {
        return inputType;
    }

    public void setInputType( InputParameterType inputType ) {
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
                                            "Long".equals( type ) ?
                                                    ( "null".equals( defaultValue.toLowerCase() )?  "" :  "L" ) :
                                                     ""
                                    )
                ) + ";\n\n";
    }
}
