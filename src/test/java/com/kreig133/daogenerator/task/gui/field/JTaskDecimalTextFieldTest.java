package com.kreig133.daogenerator.task.gui.field;

import junit.framework.Assert;
import org.junit.Test;

/**
 * @author kreig133
 * @version 1.0
 */
public class JTaskDecimalTextFieldTest {
    @Test
    public void testGetValue() throws Exception {
        JTaskDecimalTextField jTaskDecimalTextField = new JTaskDecimalTextField();

        getValueShoulBeEqualSetValue( jTaskDecimalTextField, 1.0 );
        getValueShoulBeEqualSetValue( jTaskDecimalTextField, -1.0 );
    }

    private void getValueShoulBeEqualSetValue( JTaskDecimalTextField jTaskDecimalTextField, Double d ) {
        jTaskDecimalTextField.setValue( d.toString() );
        Assert.assertEquals( d, jTaskDecimalTextField.getValue() );
    }

    @Test(expected = NumberFormatException.class )
    public void testRegexFormatter(){
        JTaskDecimalTextField jTaskDecimalTextField = new JTaskDecimalTextField();
        jTaskDecimalTextField.setValue( "avac" );
        jTaskDecimalTextField.getValue();
    }



}
