package com.kreig133.daogenerator.task.gui.field;

import com.kreig133.daogenerator.jaxb.ParameterType;

/**
 * @author kreig133
 * @version 1.0
 */
public class JTaskFieldFactory {
    public static JTaskField<?> newJTaskField( ParameterType type ) {
        switch ( type.getType() ) {
            case BYTE:
            case LONG:
                return new JTaskNumericTextField();
            case DOUBLE:
                return new JTaskDecimalTextField();
            case STRING:
                return new JTaskStringTextField();
            case DATE:
                return new JTaskDateTimeField();
            default:
                throw new RuntimeException();
        }
    }
}
