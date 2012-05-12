package com.kreig133.daogenerator.common;

import junit.framework.Assert;
import org.junit.Test;

/**
 * @author eshangareev
 * @version 1.0
 */
public class UtilsTest{
    @Test
    public void testConvertPBNameToName() throws Exception {
        final String fuCk_off_aLL = Utils.convertPBNameToName( "FuCk_off_aLL" );
        Assert.assertEquals( "fuCkOffALL", fuCk_off_aLL );
    }

    @Test
    public void testConvertPBNameToNameWhenAllCharIsUpperCase() throws Exception {
        final String fuCk_off_aLL = Utils.convertPBNameToName( "FUCK_OFF_ALL" );
        Assert.assertEquals( "fuckOffAll", fuCk_off_aLL );
        final String ID = Utils.convertPBNameToName( "ID" );
        Assert.assertEquals( "id", ID );
    }


}
