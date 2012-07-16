package com.kreig133.daogenerator.jaxb;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author eshangareev
 * @version 1.0
 */
public class JavaTypeTest {
    @Test
    public void testGetBySqlType() throws Exception {
        Assert.assertEquals( JavaType.getBySqlType( "VARCHAR(5)" ), JavaType.STRING );
        Assert.assertEquals( JavaType.getBySqlType( "INT" ), JavaType.LONG );
    }

    @Test
    public void testIsNameAccordHungarianNotation() throws Exception {
        Assert.assertTrue( JavaType.STRING.isNameAccordHungarianNotation( "sJLKJLKJ" ) );
        Assert.assertFalse( JavaType.STRING.isNameAccordHungarianNotation( "nJLKJLKJ" ) );
        Assert.assertTrue( JavaType.STRING.isNameAccordHungarianNotation( "KJLKJLKJ" ) );

        Assert.assertFalse( JavaType.DATE.isNameAccordHungarianNotation( "SKLJLJJL" ) );
        Assert.assertFalse( JavaType.DATE.isNameAccordHungarianNotation( "sKLJLJJL" ) );
        Assert.assertFalse( JavaType.DATE.isNameAccordHungarianNotation( "nKLJLJJL" ) );
        Assert.assertTrue( JavaType.DATE.isNameAccordHungarianNotation( "dKLJLJJL" ) );
    }
}
