package com.kreig133.daogenerator.files;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author eshangareev
 * @version 1.0
 */
public class JavaClassGeneratorTest{

    private String javaClassName = "com.kreig133.daogenerator.files.JavaClassGenerator";
    private String javaClassName2 = "com.aplana.deposit.ledom.WhatTheModel";

    @Test
    public void testGetPackageName() throws Exception {
        Assert.assertEquals( PackageAndFileUtils.getPackage( javaClassName ), "com.kreig133.daogenerator.files" );
        Assert.assertEquals( PackageAndFileUtils.getPackage( javaClassName2 ), "com.aplana.deposit.ledom" );
    }

    @Test
    public void testGetShortName() throws Exception {
        Assert.assertEquals( PackageAndFileUtils.getShortName( javaClassName ), "JavaClassGenerator" );
        Assert.assertEquals( PackageAndFileUtils.getShortName( javaClassName2 ), "WhatTheModel" );
    }
}
