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
        Map<String, String> columnsFromQuery = super.getColumnsFromQuery( "UPDATE teSponsor SET sName = 'лжщл' WHERE nRecID = 97" );
        Assert.assertEquals( columnsFromQuery.size(), 2 );
        Assert.assertTrue( columnsFromQuery.keySet().contains( "sName" ) );
        Assert.assertTrue( columnsFromQuery.keySet().contains( "nRecID" ) );
    }

    @Test
    public void columnsFromQueryTestWithEscapedColumns(){
        Map<String, String> columnsFromQuery = super.getColumnsFromQuery( QUERY );
        Assert.assertEquals( columnsFromQuery.size(), 1 );
    }

    @Test
    public void test(){
        String s = super.prepareQuery( QUERY );
        Assert.assertEquals( s, "SELECT\n" +
                "\ttDocPack.nDocID\n" +
                "FROM \n" +
                "\ttDocPack ( NOLOCK )\n" +
                "WHERE \n" +
                "\ttDocPack.nDocTypeID = 59 and\n" +
                "\tisnull ( tDocPack.ndeleted , 0 ) = 0 and\n" +
                "\ttDocPack.nPackID = ${nPackID;int;250287} " );
    }

}
