package com.kreig133.daogenerator;

/**
 * @author eshangareev
 * @version 1.0
 */
public class InputParameter extends Parameter {
    private String defaultValue;

    public InputParameter(String name, String type, String defaultValue, String comment) {
        super(comment, type, name);
        this.defaultValue = Utils.handleDefaultValue(defaultValue);

    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString() {

        return super.toString() +
                (
                        ( defaultValue == null || "".equals(defaultValue) )?
                        ";\n\n" :
                        " = " + defaultValue.toLowerCase() + ";\n\n"
                );
    }
}
