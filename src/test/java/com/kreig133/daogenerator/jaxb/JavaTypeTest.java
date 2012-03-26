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
}
