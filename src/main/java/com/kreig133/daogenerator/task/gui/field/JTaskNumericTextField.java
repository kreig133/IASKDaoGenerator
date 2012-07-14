package com.kreig133.daogenerator.task.gui.field;

/**
 * @author kreig133
 * @version 1.0
 */
public class JTaskNumericTextField extends JTaskFormattedTextField<Long> {

    JTaskNumericTextField() {
        super( "-?\\d+" );
    }

    @Override
    public Long getValue() {
        return Long.valueOf( jFormattedTextField.getText() );
    }
}
