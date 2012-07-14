package com.kreig133.daogenerator.task.gui.field;

import com.kreig133.daogenerator.task.gui.lib.RegexFormatter;

import javax.swing.*;
import java.awt.*;

/**
 * @author kreig133
 * @version 1.0
 */
public abstract class JTaskFormattedTextField<T> extends JTaskField<T> {

    protected final JFormattedTextField jFormattedTextField;

    public JTaskFormattedTextField( String pattern ) {


        jFormattedTextField = new JFormattedTextField( new RegexFormatter( pattern ) );
        jFormattedTextField.setPreferredSize( new Dimension( 500, 30 ) );
        jFormattedTextField.setMaximumSize( new Dimension( 2000, 100 ) );
        add( wrapWithVerticalBoxLayout( jFormattedTextField ), BorderLayout.CENTER );
    }

    @Override
    public final void setValue( String value ) {
        jFormattedTextField.setText( value );
    }
}
