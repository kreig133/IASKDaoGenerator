package com.kreig133.daogenerator.task.gui.field;

import javax.swing.*;
import java.awt.*;

/**
 * @author kreig133
 * @version 1.0
 */
public abstract class JTaskField<T> extends JPanel{

    protected JTaskField() {
        setLayout( new BorderLayout() );
    }

    abstract public T getValue();

    abstract public void setValue( String value );

    protected Box wrapWithVerticalBoxLayout( JComponent comp ) {
        Box verticalBox = Box.createVerticalBox();
        verticalBox.add( Box.createVerticalGlue() );
        verticalBox.add( comp );
        verticalBox.add( Box.createVerticalGlue() );
        return verticalBox;
    }
}
