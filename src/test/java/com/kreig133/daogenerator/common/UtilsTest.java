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
        Assert.assertEquals( "luCkOclALL", Utils.convertPBNameToName( "LuCk_ocl_aLL" ));
        Assert.assertEquals(Utils.convertPBNameToName("nstate"), "nstate");
        Assert.assertEquals(Utils.convertPBNameToName("nStateState_State"), "stateStateState");
        Assert.assertEquals(Utils.convertPBNameToName("stateStateState"), "stateStateState");
        Assert.assertEquals(Utils.convertPBNameToName("NSTate"), "NSTate");
    }

    @Test
    public void testConvertPBNameToNameWhenAllCharIsUpperCase() throws Exception {
        Assert.assertEquals("loveYouAll", Utils.convertPBNameToName("LOVE_YOU_ALL"));
        Assert.assertEquals("id", Utils.convertPBNameToName("ID"));
        Assert.assertEquals("nstate", Utils.convertPBNameToName("NSTATE"));
    }

    @Test
    public void testConvertPBNameToNameWithHungarianNotation() throws Exception {
        Assert.assertEquals(Utils.convertPBNameToName("nState"), "state");
        Assert.assertEquals(Utils.convertPBNameToName("n_State"), "state");
        Assert.assertEquals(Utils.convertPBNameToName("nSTATE"), "state");
    }
}
