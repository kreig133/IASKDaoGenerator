package com.kreig133.daogenerator.sql;

import com.kreig133.daogenerator.jaxb.CommonType;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParametersType;
import org.intellij.lang.annotations.Language;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author eshangareev
 * @version 1.0
 */
public class SqlQueryParserTest {

    @Language( "SQL" )
    private String value;

    @Before
    public void setUp() throws Exception {
        value = "SELECT * FROM tTable WHERE a = ${firstParam;varchar(255);first} AND b = ${second;int;123}}";
    }

    @Test
    public void test() {
        final DaoMethod daoMethod = new DaoMethod();
        daoMethod.setCommon( new CommonType() );
        daoMethod.setInputParametrs( new ParametersType() );

        daoMethod.getCommon().setQuery( value );

        final DaoMethod daoMethod1 = SqlQueryParser.parseSqlQueryAndParameters( daoMethod );
        System.out.println( daoMethod );
        Assert.assertTrue( daoMethod.getInputParametrs().getParameter().size() == 2 );
    }
}
