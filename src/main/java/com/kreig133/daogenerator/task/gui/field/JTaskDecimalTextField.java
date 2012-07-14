package com.kreig133.daogenerator.task.gui.field;

/**
 * @author kreig133
 * @version 1.0
 */
public class JTaskDecimalTextField extends JTaskFormattedTextField<Double> {

    JTaskDecimalTextField() {
        super( "-?\\d+(.\\d+)?");
    }

    @Override
    public Double getValue() {
        return Double.valueOf( jFormattedTextField.getText() );
    }
}
