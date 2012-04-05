package com.kreig133.daogenerator.files.mybatis.model;

import com.kreig133.daogenerator.jaxb.NamingUtils;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.jaxb.ParametersType;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class ModelClassGeneratorTest extends ModelClassGenerator{

    @Nullable
    @Override
    protected List<ParameterType> filter( List<ParameterType> parameter ) {
        return null;
    }

    public ModelClassGeneratorTest() {
        super( new ParametersType() );
    }

    @Test
    public void testConvertForEnum() throws Exception {
        Assert.assertEquals( NamingUtils.convertNameForEnum( "userGroupId" ), "USER_GROUP_ID" );
        Assert.assertEquals( NamingUtils.convertNameForEnum( "GroupId" ), "GROUP_ID" );
        Assert.assertEquals( NamingUtils.convertNameForEnum( "UGroupId" ), "UGROUP_ID" );
        Assert.assertEquals( NamingUtils.convertNameForEnum( "UGroupIdS" ), "UGROUP_ID_S" );
    }
}
