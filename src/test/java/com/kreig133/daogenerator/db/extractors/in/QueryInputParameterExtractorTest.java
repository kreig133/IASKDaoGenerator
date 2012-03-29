package com.kreig133.daogenerator.db.extractors.in;

import com.kreig133.daogenerator.TestHelper;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.SelectType;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author eshangareev
 * @version 1.0
 */
public class QueryInputParameterExtractorTest extends QueryInputParameterExtractor{
    @Override
    public DaoMethod extractInputParams( DaoMethod daoMethod ) {
        return null;
    }

    @Override
    public DaoMethod fillTestValuesByInsertedQuery( DaoMethod daoMethod ) {
        return null;
    }

    @Test
    public void getTableNameTestUpdate(){
        DaoMethod daoMethodForTest = TestHelper.getDaoMethodForTest();
        daoMethodForTest.getCommon().setQuery(
            "UPDATE   \n\tteSponsor SET sName = :sName, sPart=:sPart, sText=:sText WHERE nRecID = :nRecID"
        );
        daoMethodForTest.getCommon().getConfiguration().setType( SelectType.UPDATE );
        Assert.assertEquals( getTableName( daoMethodForTest ), "teSponsor" ) ;
    }
    @Test
    public void getTableNameTestSelect(){
        DaoMethod daoMethodForTest = TestHelper.getDaoMethodForTest();
        daoMethodForTest.getCommon().setQuery(
                    "SELECT dbo.tabstatebonds.ivalueid,\n" +
                    "         dbo.tabvalue.sshortname\n" +
                    "    FROM dbo.tabstatebonds\n" +
                    "          INNER JOIN dbo.tabvalue ON ( dbo.tabvalue.ivalueid = dbo.tabstatebonds.ivalueid )"
        );
        daoMethodForTest.getCommon().getConfiguration().setType( SelectType.SELECT );
        Assert.assertEquals( getTableName( daoMethodForTest ), "tabstatebonds" ) ;
    }
    @Test
    public void getTableNameTestDelete(){
        DaoMethod daoMethodForTest = TestHelper.getDaoMethodForTest();
        daoMethodForTest.getCommon().setQuery(
                "DELETE \tFROM \n   dbo.teSponsor WHERE nRecID = :nRecID"
        );
        daoMethodForTest.getCommon().getConfiguration().setType( SelectType.DELETE );
        Assert.assertEquals( getTableName( daoMethodForTest ), "teSponsor" ) ;
    }
    @Test
    public void getTableNameTestInsert(){
        DaoMethod daoMethodForTest = TestHelper.getDaoMethodForTest();
        daoMethodForTest.getCommon().setQuery(
                " INSERT INTO tePackRiskChanged ( dDateBegin , nPrizn , nPackId , nCUID , sComment ) VALUES ( ? , ? , ? , ? , ? ) "
        );
        daoMethodForTest.getCommon().getConfiguration().setType( SelectType.INSERT );
        Assert.assertEquals( getTableName( daoMethodForTest ), "tePackRiskChanged" ) ;
    }

}
