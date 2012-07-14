package com.kreig133.daogenerator.task.gui;

import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.task.gui.field.JTaskField;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kreig133
 * @version 1.0
 */
public class JTaskPanel extends JPanel {
    Map<ParameterType, JTaskField> map = new HashMap<ParameterType, JTaskField>();

    public List<ParameterType> getParameterTypes() {
        List<ParameterType> result = new ArrayList<ParameterType>( map.size() );
        for ( Map.Entry<ParameterType, JTaskField> parameterTypeJTextFieldEntry : map.entrySet() ) {
            ParameterType key = parameterTypeJTextFieldEntry.getKey();
            key.setTestValue( parameterTypeJTextFieldEntry.getValue().getValue().toString() );
            result.add( key );
        }
        return result;
    }

    @Override
    public Component add( Component comp ) {
        if ( comp instanceof JTaskField ) {
            throw new RuntimeException( "Низзя было вызывать этот метод, другой вызывай!" );
        }
        return super.add( comp );
    }

    public void add( JTaskField field, ParameterType parameterType ) {
        super.add( field );
        map.put( parameterType, field );
    }
}
