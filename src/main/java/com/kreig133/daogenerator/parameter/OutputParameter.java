package com.kreig133.daogenerator.parameter;

/**
 * @author eshangareev
 * @version 1.0
 */
public class OutputParameter extends Parameter{

    public OutputParameter(String comment, String type, String name) {
        super(comment, type, name);
    }

    @Override
    public String toString() {
        return super.toString() + ";\n\n";
    }
}
