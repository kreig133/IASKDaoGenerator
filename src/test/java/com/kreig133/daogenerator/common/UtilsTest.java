package com.kreig133.daogenerator.common;

import junit.framework.TestCase;

/**
 * @author eshangareev
 * @version 1.0
 */
public class UtilsTest extends TestCase {
    public void testConvertPBNameToName() throws Exception {
        final String fuCk_off_aLL = Utils.convertPBNameToName( "FuCk_off_aLL" );
        assertEquals( "fuCkOffALL", fuCk_off_aLL );
    }
}
