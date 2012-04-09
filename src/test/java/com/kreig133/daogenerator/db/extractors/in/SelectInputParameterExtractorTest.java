package com.kreig133.daogenerator.db.extractors.in;

import com.kreig133.daogenerator.TestHelper;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import org.intellij.lang.annotations.Language;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author kreig133
 * @version 1.0
 */
public class SelectInputParameterExtractorTest {
    @Language( "SQL" )
    private String  value = "SELECT * FROM tTable WHERE a = ${firstParam;varchar(255);first} AND b = ${second;int;123}}";

    @Test
    public void test() {
        final DaoMethod daoMethod = TestHelper.getDaoMethodForTest();
        daoMethod.getCommon().setSpName( "" );

        daoMethod.getCommon().setQuery( value );

        final DaoMethod daoMethod1 = SelectInputParameterExtractor.instance().extractInputParams(
                daoMethod );
        System.out.println( daoMethod1 );
        Assert.assertTrue( daoMethod1.getInputParametrs().getParameter().size() == 2 );
    }
}
