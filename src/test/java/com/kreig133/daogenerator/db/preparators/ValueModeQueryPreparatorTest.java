package com.kreig133.daogenerator.db.preparators;

import junit.framework.Assert;
import org.intellij.lang.annotations.Language;
import org.junit.Test;

import java.util.Map;

/**
 * @author eshangareev
 * @version 1.0
 */
public class ValueModeQueryPreparatorTest extends ValueModeQueryPreparator {

    String delMe = "select count ( *) as retn\n" +
            "from dbo.t_division\n" +
            "where iisdeleted = ${iisdeleted;int;0} and\n" +
            "ntypesource = ${ntypesource;int;2} and\n" +
            "iownerid = ${iownerid;int;0} and\n" +
            "irelationtypeid =CAST ( 1 AS INT ) and\n" +
            "isourceid =CAST ( 8368 AS INT )";

    private static final String QUERY = "SELECT\n" +
            "\ttDocPack.nDocID\n" +
            "FROM \n" +
            "\ttDocPack ( NOLOCK )\n" +
            "WHERE \n" +
            "\t``tDocPack.nDocTypeID = 59 and\n" +
            "\t``isnull ( tDocPack.ndeleted , 0 ) = 0 and\n" +
            "\ttDocPack.nPackID = 250287 ";

    @Test
    public void columnsFromQueryTest() {
        Map<String, String> columnsFromQuery = super.getColumnsFromQuery( QueryPreparatorTest.updateQuery );
        Assert.assertEquals( columnsFromQuery.size(), 2 );
        Assert.assertTrue( columnsFromQuery.keySet().contains( "nflreport" ) );
        Assert.assertTrue( columnsFromQuery.keySet().contains( "n_DEPO_id" ) );
    }

    @Test
    public void columnsFromQueryTestWithEscapedColumns(){
        Map<String, String> columnsFromQuery = super.getColumnsFromQuery( QUERY );
        Assert.assertEquals( columnsFromQuery.size(), 1 );
    }

    @Language( "SQL" )
    public static final String IN_QUERY = "select count ( *) as retn\n" +
            "from dbo.t_division\n" +
            "where iisdeleted =0 and\n" +
            "ntypesource =2 and\n" +
            "iownerid =0 and\n" +
            "irelationtypeid =CAST ( 1 AS INT ) and\n" +
            "isourceid =CAST ( 8368 AS INT )";

    @Test
    public void inputParamsCountShouldBeEqual3() {
        int size = super.getColumnsFromQuery( IN_QUERY ).size();
        Assert.assertEquals( size, 3 );
    }

    @Test
    public void testCastPrepare() {
        super.prepareQueryValueMode( IN_QUERY );
    }


}
