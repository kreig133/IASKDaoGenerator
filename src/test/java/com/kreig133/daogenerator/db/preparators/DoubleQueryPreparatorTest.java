package com.kreig133.daogenerator.db.preparators;

import com.kreig133.daogenerator.jaxb.ParameterType;
import junit.framework.Assert;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class DoubleQueryPreparatorTest{

    @NotNull
    @Language( "SQL" )
    private String query = "SELECT distinct\n" +
            " tm.imembercid, \n" +
            "ltrim(tm.sShortName) as sshortname \n" +
            "FROM dbo.tabBondKind bk with (nolock) \n" +
            "JOIN dbo.tabBondTypes bt with (nolock) ON bk.iBondKindID = bt.iBondKindID \n" +
            "JOIN dbo.tabMembers tm with (nolock) ON bt.iMemberCID = tm.iMemberCID \n" +
            "JOIN ( select ibonvalueid \n" +
            "from dbo.tabsectorbondtypes with (nolock) \n" +
            "where ivalueid = coalesce(cast(:id as integer),0) \n" +
            "union select ivalueid as ibonvalueid \n" +
            "from dbo.tabbondtypes with (nolock) \n" +
            "where coalesce(cast(:id as integer),0) = 0) ts ON bt.ivalueid = ts.ibonvalueid, \n" +
            "( select streecode, ibondkindid \n" +
            "from dbo.tabBondKind with (nolock) \n" +
            "where ibondkindid=coalesce(cast(:ibondid as integer),ibondkindid)) tk \n" +
            "WHERE dbo.SUBSTRING(bk.streecode,1, dbo.DATALENGTH(tk.streecode)) = tk.streecode \n" +
            "UNION ALL\n" +
            "SELECT cast(null as int) as imembercid, \n" +
            "'' as sshortname \n" +
            "FROM dbo.v_dummy \n" +
            "ORDER BY sshortname";

    @NotNull
    @Language( "SQL" )
    private String fiiledQuery = "SELECT distinct\n" +
            " tm.imembercid, \n" +
            "ltrim(tm.sShortName) as sshortname \n" +
            "FROM dbo.tabBondKind bk with (nolock) \n" +
            "JOIN dbo.tabBondTypes bt with (nolock) ON bk.iBondKindID = bt.iBondKindID \n" +
            "JOIN dbo.tabMembers tm with (nolock) ON bt.iMemberCID = tm.iMemberCID \n" +
            "JOIN ( select ibonvalueid \n" +
            "from dbo.tabsectorbondtypes with (nolock) \n" +
            "where ivalueid = coalesce(cast(123 as integer),0) \n" +
            "union select ivalueid as ibonvalueid \n" +
            "from dbo.tabbondtypes with (nolock) \n" +
            "where coalesce(cast(123 as integer),0) = 0) ts ON bt.ivalueid = ts.ibonvalueid, \n" +
            "( select streecode, ibondkindid \n" +
            "from dbo.tabBondKind with (nolock) \n" +
            "where ibondkindid=coalesce(cast(456 as integer),ibondkindid)) tk \n" +
            "WHERE dbo.SUBSTRING(bk.streecode,1, dbo.DATALENGTH(tk.streecode)) = tk.streecode \n" +
            "UNION ALL\n" +
            "SELECT cast(null as int) as imembercid, \n" +
            "'' as sshortname \n" +
            "FROM dbo.v_dummy \n" +
            "ORDER BY sshortname";

    @Test
    public void prepareQueryTest(){
        @Language( "SQL" )
        String inputWithName =
                "select count(n_ad_id) from dbo.t_ad_rasp where not n_ad_id is null and n_adr_raspor = :irasp_id and iisdeleted = 0";
        @Language( "SQL" )
        String inputWithTestVlue =
                "select count(n_ad_id) from dbo.t_ad_rasp where not n_ad_id is null and n_adr_raspor = 376682 and iisdeleted = 0";
        String s = DoubleQueryPreparator.instance().prepareQuery( inputWithName, inputWithTestVlue );
        Assert.assertEquals( s, "select count(n_ad_id) from dbo.t_ad_rasp where not n_ad_id is null and n_adr_raspor = ${irasp_id;int;376682} and iisdeleted = 0" );
    }

    @Test
    public void testParse(){
        String s = DoubleQueryPreparator.instance().prepareQuery( query, fiiledQuery );
        System.out.println(s);
    }

    @Test
    public void testParseQueryWithName() {
        List<String> queryPiece = new ArrayList<String>();
        List<String> paramNames = new ArrayList<String>();

        DoubleQueryPreparator.instance().parseQueryWithName( queryPiece, paramNames, query );

        Assert.assertEquals( paramNames.size(), 3 );
    }

    @Test
    public void testDetermineSqlTypeByTestValue() {
        testDetermineSqlTypeByTestValue( "'3-22-2000 0:0:0.000'", "datetime" );
        testDetermineSqlTypeByTestValue( "-199239818328939", "int" );
        testDetermineSqlTypeByTestValue( "99239818328939", "int" );
        testDetermineSqlTypeByTestValue( "-99239818328939.000", "numeric" );
        testDetermineSqlTypeByTestValue( "99239818328939.000", "numeric" );
        testDetermineSqlTypeByTestValue( "''", "varchar" );
        testDetermineSqlTypeByTestValue( "''", "varchar" );
    }

    @Test
    public void testPrepareQueryWithNonEqualQueries(){
        String s = DoubleQueryPreparator.instance().prepareQuery(
                "SELECT * FROM      table \t\t\nWHERE id   \t\n   \r   =:id AND date=\t\n\r\t\n\r                  :date",
                "       SELECT \t\r\n* FROM table WHERE id=1 AND date='1.1.1 1'" );
        Assert.assertTrue( s.contains( "${id;int;1}" ) );
        Assert.assertTrue( s.contains( "${date;varchar;'1.1.1 1'}" ) );
    }

    @Test
    public void prepareQueryBeforeParseTest(){
        Assert.assertEquals( DoubleQueryPreparator.instance().prepareQueryBeforeParse( ") \n, " ), ")," );
        Assert.assertEquals( DoubleQueryPreparator.instance().prepareQueryBeforeParse( "p    \t\n\r = \n123 " ),
                "p=123" );
        Assert.assertEquals( DoubleQueryPreparator.instance().prepareQueryBeforeParse( "SELECT\nINTO" ),
                "SELECT INTO" );
    }

    private void testDetermineSqlTypeByTestValue( String testValue, String sqlType ) {
        ParameterType parameterType = new ParameterType();
        parameterType.setTestValue( testValue );
        DoubleQueryPreparator.instance().determineSqlTypeByTestValue( parameterType );

        Assert.assertEquals( parameterType.getSqlType(), sqlType );
    }
}
