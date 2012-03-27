package com.kreig133.daogenerator.files.mybatis.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author eshangareev
 * @version 1.0
 */
public class ModelClassGeneratorTest extends ModelClassGenerator{



    public ModelClassGeneratorTest() {
        super( null );
    }

    @Test
    public void testConvertForEnum() throws Exception {
        Assert.assertEquals( convertForEnum( "userGroupId" ), "USER_GROUP_ID" );
        Assert.assertEquals( convertForEnum( "GroupId" ), "GROUP_ID" );
        Assert.assertEquals( convertForEnum( "UGroupId" ), "UGROUP_ID" );
        Assert.assertEquals( convertForEnum( "UGroupIdS" ), "UGROUP_ID_S" );
    }


}
