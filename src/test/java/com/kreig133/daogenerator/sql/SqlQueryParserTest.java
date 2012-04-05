package com.kreig133.daogenerator.sql;

import com.kreig133.daogenerator.TestHelper;
import com.kreig133.daogenerator.db.extractors.Extractor;
import com.kreig133.daogenerator.db.extractors.in.SelectInputParameterExtractor;
import com.kreig133.daogenerator.jaxb.CommonType;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParametersType;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
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



    @NotNull
    String spName2 = " dbo.sp_ValueSearcher18 ";
    @NotNull
    String query2 = "     eXecUte    dbo.sp_ValueSearcher18\n @id_mode";

    @Test
    public void test() {
        final DaoMethod daoMethod = new DaoMethod();
        daoMethod.setCommon( new CommonType() );
        daoMethod.setInputParametrs( new ParametersType() );

        daoMethod.getCommon().setQuery( value );

        final DaoMethod daoMethod1 = new SelectInputParameterExtractor().extractInputParams( daoMethod );
        System.out.println( daoMethod1 );
        Assert.assertTrue( daoMethod1.getInputParametrs().getParameter().size() == 2 );
    }

    @Test
    public void getStoreProcedureName() {
        Assert.assertEquals( "sp_ValueSearcher18", Extractor.getStoreProcedureName( TestHelper.spCall ) );
        Assert.assertEquals( "sp_ValueSearcher18", Extractor.getStoreProcedureName( query2 ) );
        Assert.assertEquals( "sp_ValueSearcher18", Extractor.getStoreProcedureName( spName2 ) );
    }
    



}
