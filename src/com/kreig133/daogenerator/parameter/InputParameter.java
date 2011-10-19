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
    private String rawName;

    public InputParameter(String name, String type, String defaultValue, String comment, InputOrOutputType inputType) {
        super(comment, type, Utils.convertPBNameToName( name ) );
        this.defaultValue = Utils.handleDefaultValue( defaultValue );
        this.inputType = inputType;
        this.rawName = name;
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

    public String getRawName() {
        return rawName;
    }

    @Override
    public String toString() {

        return super.toString() +
                (
                        ( defaultValue == null || "".equals(defaultValue.trim()) )?
                            "" :
                            " = " + defaultValue.toLowerCase().trim() +
                                    (
                                            type == JavaType.Long ?
                                                    ( "null".equals( defaultValue.toLowerCase().trim() )?  "" :  "L" ) :
                                                     ""
                                    )
                ) + ";\n\n";
    }
}
