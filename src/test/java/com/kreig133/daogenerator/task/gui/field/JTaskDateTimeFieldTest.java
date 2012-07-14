package com.kreig133.daogenerator.task.gui.field;

import com.kreig133.daogenerator.common.Utils;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * @author kreig133
 * @version 1.0
 */
public class JTaskDateTimeFieldTest {
    @Test
    public void testGetValue() throws Exception {
        Date currentDate = new Date();

        JTaskDateTimeField field = new JTaskDateTimeField();
        field.setValue( Utils.getDaoGeneratorDateFormat().format( currentDate ) );

        Assert.assertEquals( field.getValue(), currentDate );
    }
}
