package com.kreig133.daogenerator.parameter;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.jaxb.InOutType;
import com.kreig133.daogenerator.jaxb.JavaType;

/**
 * @author eshangareev
 * @version 1.0
 */
public class InputParameter extends Parameter {
    private String defaultValue;
    private InOutType inputType;
    private String rawName;

    public InputParameter(String name, String type, String defaultValue, String comment, InOutType inputType) {
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

    public InOutType getInputType() {
        return inputType;
    }

    public void setInputType( InOutType inputType ) {
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
                                            type == JavaType.LONG ?
                                                    ( "null".equals( defaultValue.toLowerCase().trim() )?  "" :  "L" ) :
                                                     ""
                                    )
                ) + ";\n\n";
    }
}
