package com.kreig133.daogenerator.files.mybatis.model;

import com.kreig133.daogenerator.files.NamingUtils;
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
        Assert.assertEquals( NamingUtils.convertNameForEnum( "userGroupId" ), "USER_GROUP_ID" );
        Assert.assertEquals( NamingUtils.convertNameForEnum( "GroupId" ), "GROUP_ID" );
        Assert.assertEquals( NamingUtils.convertNameForEnum( "UGroupId" ), "UGROUP_ID" );
        Assert.assertEquals( NamingUtils.convertNameForEnum( "UGroupIdS" ), "UGROUP_ID_S" );
    }


}
