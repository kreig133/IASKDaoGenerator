package com.kreig133.daogenerator.task.gui.field;

/**
 * @author kreig133
 * @version 1.0
 */
public class JTaskStringTextField extends JTaskFormattedTextField<String> {

    JTaskStringTextField() {
        super( ".*" );
    }

    @Override
    public String getValue() {
        return jFormattedTextField.getText();
    }
}
