package com.kreig133.daogenerator.sql;

import com.kreig133.daogenerator.TestHelper;
import com.kreig133.daogenerator.db.extractors.Extractor;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author eshangareev
 * @version 1.0
 */
public class SqlQueryParserTest {

    @NotNull
    String spName2 = " dbo.sp_ValueSearcher18 ";
    @NotNull
    String query2 = "     eXecUte    dbo.sp_ValueSearcher18\n @id_mode";

    @Test
    public void getStoreProcedureName() {
        Assert.assertEquals( "sp_ValueSearcher18", Extractor.getStoreProcedureName( TestHelper.spCall ) );
        Assert.assertEquals( "sp_ValueSearcher18", Extractor.getStoreProcedureName( query2 ) );
        Assert.assertEquals( "sp_ValueSearcher18", Extractor.getStoreProcedureName( spName2 ) );
    }
}
