package com.kreig133.daogenerator.sql.creators;

import com.kreig133.daogenerator.TestHelper;
import org.junit.Test;

/**
 * @author eshangareev
 * @version 1.0
 */
public class WrapperGeneratorTest {
    @Test
    public void testGenerateExecuteQuery() throws Exception {
        System.out.println( new WrapperGenerator().generateExecuteQuery( TestHelper.getDaoMethodForTest(), false ) );
        System.out.println();
        System.out.println( new WrapperGenerator().generateExecuteQuery( TestHelper.getDaoMethodForTest(), true ) );
    }
}
