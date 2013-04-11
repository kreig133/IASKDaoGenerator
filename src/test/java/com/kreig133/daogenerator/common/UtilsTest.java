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
        final String fuCk_off_aLL = Utils.convertPBNameToName( "LuCk_ocl_aLL" );
        Assert.assertEquals( "luCkOclALL", fuCk_off_aLL );
        String s = "nstate";
        Assert.assertEquals(Utils.convertPBNameToName(s), "nstate");
    }

    @Test
    public void testConvertPBNameToNameWhenAllCharIsUpperCase() throws Exception {
        final String fuCk_off_aLL = Utils.convertPBNameToName( "LOVE_YOU_ALL" );
        Assert.assertEquals( "loveYouAll", fuCk_off_aLL );
        final String ID = Utils.convertPBNameToName( "ID" );
        Assert.assertEquals( "id", ID );
        String s = "NSTATE";
        Assert.assertEquals(Utils.convertPBNameToName(s), "nstate");
    }

    @Test
    public void testConvertPBNameToNameWithHungarianNotation() throws Exception {
        String s = "nState";
        Assert.assertEquals(Utils.convertPBNameToName(s), "state");
        s = "n_State";
        Assert.assertEquals(Utils.convertPBNameToName(s), "state");
        s = "nSTATE";
        Assert.assertEquals(Utils.convertPBNameToName(s), "state");
    }
}
