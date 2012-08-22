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
public class DoubleQueryPreparatorTest {

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
    public void prepareQueryTest() {
        @Language( "SQL" )
        String inputWithName =
                "select count(n_ad_id) from dbo.t_ad_rasp where not n_ad_id is null and n_adr_raspor = :irasp_id and iisdeleted = 0";
        @Language( "SQL" )
        String inputWithTestVlue =
                "select count(n_ad_id) from dbo.t_ad_rasp where not n_ad_id is null and n_adr_raspor = 376682 and iisdeleted = 0";
        String s = DoubleQueryPreparator.instance().prepareQuery( inputWithName, inputWithTestVlue );
        Assert.assertEquals( s,
                "select count(n_ad_id) from dbo.t_ad_rasp where not n_ad_id is null and n_adr_raspor = ${irasp_id;int;376682} and iisdeleted = 0" );
    }

    @Test
    public void testParse() {
        String s = DoubleQueryPreparator.instance().prepareQuery( query, fiiledQuery );
        Assert.assertEquals( s, "SELECT distinct\n" +
                " tm.imembercid, \n" +
                "ltrim(tm.sShortName) as sshortname \n" +
                "FROM dbo.tabBondKind bk with (nolock) \n" +
                "JOIN dbo.tabBondTypes bt with (nolock) ON bk.iBondKindID = bt.iBondKindID \n" +
                "JOIN dbo.tabMembers tm with (nolock) ON bt.iMemberCID = tm.iMemberCID \n" +
                "JOIN ( select ibonvalueid \n" +
                "from dbo.tabsectorbondtypes with (nolock) \n" +
                "where ivalueid = coalesce(cast(${id;integer;123} as integer),0) \n" +
                "union select ivalueid as ibonvalueid \n" +
                "from dbo.tabbondtypes with (nolock) \n" +
                "where coalesce(cast(${id;integer;123} as integer),0) = 0) ts ON bt.ivalueid = ts.ibonvalueid, \n" +
                "( select streecode, ibondkindid \n" +
                "from dbo.tabBondKind with (nolock) \n" +
                "where ibondkindid=coalesce(cast(${ibondid;integer;456} as integer),ibondkindid)) tk \n" +
                "WHERE dbo.SUBSTRING(bk.streecode,1, dbo.DATALENGTH(tk.streecode)) = tk.streecode \n" +
                "UNION ALL\n" +
                "SELECT cast(null as int) as imembercid, \n" +
                "'' as sshortname \n" +
                "FROM dbo.v_dummy \n" +
                "ORDER BY sshortname" );
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
    public void testPrepareQueryWithNonEqualQueries() {
        String s = DoubleQueryPreparator.instance().prepareQuery(
                "SELECT * FROM      table \t\t\nWHERE id   \t\n   \r   =:id AND date=\t\n\r\t\n\r                  :date",
                "       SELECT \t\r\n* FROM table WHERE id=1 AND date='1.1.1 1'" );
        Assert.assertTrue( s.contains( "${id;int;1}" ) );
        Assert.assertTrue( s.contains( "${date;varchar;'1.1.1 1'}" ) );
    }

    @Test
    public void prepareQueryBeforeParseTest() {
        Assert.assertEquals( DoubleQueryPreparator.instance().prepareQueryBeforeParse( ") \n, " ), " ) , " );
        Assert.assertEquals( DoubleQueryPreparator.instance().prepareQueryBeforeParse( "p    \t\n\r = \n123 " ),
                "p = 123 " );
        Assert.assertEquals( DoubleQueryPreparator.instance().prepareQueryBeforeParse( "SELECT\nINTO" ),
                "SELECT INTO" );
        Assert.assertEquals( DoubleQueryPreparator.instance().prepareQueryBeforeParse( "FROM dbo.tabPersons p\nWHERE" ),
                "FROM dbo.tabPersons p WHERE" );
        Assert.assertEquals( DoubleQueryPreparator.instance().prepareQueryBeforeParse( ")id=" ), " ) id = " );
        Assert.assertEquals( DoubleQueryPreparator.instance().prepareQueryBeforeParse( ">=" ), ">=" );
        Assert.assertEquals( DoubleQueryPreparator.instance().prepareQueryBeforeParse( "<=" ), "<=" );
        Assert.assertEquals( DoubleQueryPreparator.instance().prepareQueryBeforeParse( "!=" ), "!=" );
    }

    private void testDetermineSqlTypeByTestValue( String testValue, String sqlType ) {
        ParameterType parameterType = new ParameterType();
        parameterType.setTestValue( testValue );
        DoubleQueryPreparator.instance().determineSqlTypeByTestValue( parameterType );

        Assert.assertEquals( parameterType.getSqlType(), sqlType );
    }

    @Language( "SQL" )
    String inQueryWithParams = "SELECT \n" +
            "         id,   \n" +
            "         dt,   \n" +
            "         nday,   \n" +
            "         fl,\n" +
            "         dbo.datepartday(dt),\n" +
            "         case when dbo.datepartmonth(dt) = CAST(:n AS INT) then 1 else 0 end,   \n" +
            "         (case when CAST(:fl AS INT)=1 and not t.dtdate is null then 1 \n" +
            "                     when CAST(:fl AS INT)=2 and not v.ddatefrom is null then 1 \n" +
            "                     else 0 end) AS flquot  \n" +
            "FROM dbo.t_calendar_workday\n" +
            "     Left join (select dtdate,sum(nratebond) AS nres \n" +
            "                  from dbo.tabquotation \n" +
            "                where dtdate between :dt1 and :dt2 and \n" +
            "                      iisdeleted = 0 \n" +
            "                group by dtdate\n" +
            "                having sum(nratebond) > 0 ) t on t.dtdate = dt \n" +
            "     Left join (select ddatefrom,sum(nrate) AS nres \n" +
            "                  from dbo.tabbondsmarketrate\n" +
            "                where ddatefrom between :dt1 and :dt2 and \n" +
            "                      ivalueid = 2 \n" +
            "                group by ddatefrom\n" +
            "                having sum(nrate) > 0) v on dt = ddatefrom\n" +
            "where dt between :dt1 and :dt2  \n" +
            "order by dt";

    @Language( "SQL" )
    String inQueryTestParams = "SELECT \n" +
            "         id,   \n" +
            "         dt,   \n" +
            "         nday,   \n" +
            "         fl,\n" +
            "         dbo.datepartday(dt),\n" +
            "         case when dbo.datepartmonth(dt) = CAST(7 AS INT) then 1 else 0 end,   \n" +
            "         (case when CAST(1 AS INT)=1 and not t.dtdate is null then 1 \n" +
            "                     when CAST(1 AS INT)=2 and not v.ddatefrom is null then 1 \n" +
            "                     else 0 end) AS flquot  \n" +
            "FROM dbo.t_calendar_workday\n" +
            "     Left join (select dtdate,sum(nratebond) AS nres \n" +
            "                  from dbo.tabquotation \n" +
            "                where dtdate between '6-25-2012 0:0:0.000' and '7-31-2012 0:0:0.000' and \n" +
            "                      iisdeleted = 0 \n" +
            "                group by dtdate\n" +
            "                having sum(nratebond) > 0 ) t on t.dtdate = dt \n" +
            "     Left join (select ddatefrom,sum(nrate) AS nres \n" +
            "                  from dbo.tabbondsmarketrate\n" +
            "                where ddatefrom between '6-25-2012 0:0:0.000' and '7-31-2012 0:0:0.000' and \n" +
            "                      ivalueid = 2 \n" +
            "                group by ddatefrom\n" +
            "                having sum(nrate) > 0) v on dt = ddatefrom\n" +
            "where dt between '6-25-2012 0:0:0.000' and '7-31-2012 0:0:0.000'  \n" +
            "order by dt";

    @Test
    public void prepareQueryTest2() {
        Assert.assertEquals( DoubleQueryPreparator.instance().prepareQuery( inQueryTestParams, inQueryWithParams ),
        "SELECT \n" +
                "         id,   \n" +
                "         dt,   \n" +
                "         nday,   \n" +
                "         fl,\n" +
                "         dbo.datepartday(dt),\n" +
                "         case when dbo.datepartmonth(dt) = CAST(${n;INT;7} AS INT) then 1 else 0 end,   \n" +
                "         (case when CAST(${fl;INT;1} AS INT)=1 and not t.dtdate is null then 1 \n" +
                "                     when CAST(${fl;INT;1} AS INT)=2 and not v.ddatefrom is null then 1 \n" +
                "                     else 0 end) AS flquot  \n" +
                "FROM dbo.t_calendar_workday\n" +
                "     Left join (select dtdate,sum(nratebond) AS nres \n" +
                "                  from dbo.tabquotation \n" +
                "                where dtdate between ${dt1;datetime;'6-25-2012 0:0:0.000'} and ${dt2;datetime;'7-31-2012 0:0:0.000'} and \n" +
                "                      iisdeleted = 0 \n" +
                "                group by dtdate\n" +
                "                having sum(nratebond) > 0 ) t on t.dtdate = dt \n" +
                "     Left join (select ddatefrom,sum(nrate) AS nres \n" +
                "                  from dbo.tabbondsmarketrate\n" +
                "                where ddatefrom between ${dt1;datetime;'6-25-2012 0:0:0.000'} and ${dt2;datetime;'7-31-2012 0:0:0.000'} and \n" +
                "                      ivalueid = 2 \n" +
                "                group by ddatefrom\n" +
                "                having sum(nrate) > 0) v on dt = ddatefrom\n" +
                "where dt between ${dt1;datetime;'6-25-2012 0:0:0.000'} and ${dt2;datetime;'7-31-2012 0:0:0.000'}  \n" +
                "order by dt");

    }

    @Language( "SQL" )
    String inputQueryWithTestValues =
            "select count ( n_depo_id ) from dbo.t_depo where n_depo_id <> CAST ( 128 AS INT ) and dbo" +
            ".concat3 ( s_depo_bank , n_depo_divnr , COALESCE ( s_depo_branch , '' ) ) =dbo.concat3 " +
            "( CAST ( '11' AS VARCHAR ( 255 ) ) , CAST ( '22' AS VARCHAR ( 255 ) ) , " +
            "COALESCE ( CAST ( '00023' AS VARCHAR ( 255 ) ) , '' ) )";

    @Language( "SQL" )
    String inputQueryWithParams =
            "select count(n_depo_id)\nfrom dbo.t_depo \nwhere n_depo_id <> CAST(:id AS INT) and \n" +
            "      dbo.concat3(s_depo_bank,n_depo_divnr,COALESCE(s_depo_branch,'')) = dbo.concat3" +
                    "(CAST(:scode1 AS VARCHAR(255)),CAST(:scode2 AS VARCHAR(255))," +
                    "COALESCE(CAST(:scode3 AS VARCHAR(255)),''))";

    @Test
    public void prepareNotShouldRemoveTestValues(){
        Assert.assertEquals(
                "select count(n_depo_id)\n" +
                "from dbo.t_depo \n" +
                "where n_depo_id <> CAST(${id;INT;128} AS INT) and \n" +
                "      dbo.concat3(s_depo_bank,n_depo_divnr,COALESCE(s_depo_branch,'')) = dbo.concat3(CAST(${scode1;VARCHAR ( 255 );'11'} AS VARCHAR(255)),CAST(${scode2;VARCHAR ( 255 );'22'} AS VARCHAR(255)),COALESCE(CAST(${scode3;VARCHAR ( 255 );'00023'} AS VARCHAR(255)),''))"
        , DoubleQueryPreparator.instance().prepareQuery( inputQueryWithParams, inputQueryWithTestValues ));
    }

    @Test
    public void problemsWithPlusTest(){
        @Language("SQL")
        String first = "SELECT tTempTables.sTblName,   \n" +
                "       tTempTables.sCreateScript  \n" +
                "FROM   tTempTables  \n" +
                "WHERE  tTempTables.sTblName = '#table1'    \n" +
                "And IsNull(object_ID('tempdb..'+'#table1'),0 ) = 0";
        @Language("SQL")
        String second = "SELECT tTempTables.sTblName,   \n" +
                "       tTempTables.sCreateScript  \n" +
                "FROM   tTempTables  \n" +
                "WHERE  tTempTables.sTblName = :as_temp_table_name    \n" +
                "And IsNull(object_ID('tempdb..' + :as_temp_table_name),0 ) = 0";

        String result = DoubleQueryPreparator.instance().prepareQuery( first, second );

        Assert.assertEquals( result, "SELECT tTempTables.sTblName,   \n" +
                "       tTempTables.sCreateScript  \n" +
                "FROM   tTempTables  \n" +
                "WHERE  tTempTables.sTblName = ${as_temp_table_name;varchar;'#table1'}    \n" +
                "And IsNull(object_ID('tempdb..' + ${as_temp_table_name;varchar;'#table1'}),0 ) = 0" );
    }

}
