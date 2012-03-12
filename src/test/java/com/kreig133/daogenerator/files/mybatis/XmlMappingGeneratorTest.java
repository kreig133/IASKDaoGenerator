package com.kreig133.daogenerator.files.mybatis;

import com.kreig133.daogenerator.TestHelper;
import org.junit.Test;

/**
 * @author eshangareev
 * @version 1.0
 */
public class XmlMappingGeneratorTest {

    @Test
    public void test(){
        final String s = XmlMappingGenerator.generateXmlMapping( TestHelper.getDaoMethodForTest() );

        System.out.println(s);
    }
}
