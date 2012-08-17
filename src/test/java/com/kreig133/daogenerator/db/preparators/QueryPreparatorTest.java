package com.kreig133.daogenerator.db.preparators;

import com.kreig133.daogenerator.db.JDBCConnector;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.settings.Settings;
import junit.framework.Assert;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import static com.kreig133.daogenerator.TestHelper.chanePathToPropertiesForTests;

/**
 * @author eshangareev
 * @version 1.0
 */
public class QueryPreparatorTest extends QueryPreparator {

    @NotNull
    @Language( "SQL" )
    String insertQuery =
                    "INSERT INTO dbo.t_DocAdmMembers \n" +
                    "( iDocInID, iGeographyID, sFullName, \n" +
                    "``sEngName, ``dRegDate, \"sOkpoCode\", \n" +
                    "sINN, [sOrgan], sRegNumber, \n" +
                    "sDirLName, sDirFName, sDirFatherName, \n" +
                    "sJurPersName, sInvestorCode, idivisiontype ) \n" +
                    "VALUES\n" +
                    "( 4058335, 1, 'ru,ss', 'eng', \n" +
                    "'11-29-2011 0:0:0.000', '0002', \n" +
                    "'0003', 'omr', '0001', '000002', \n" +
                    "'000003', '000004', 'ru', 'ki', -1 )";

    @Before
    public void before(){
        chanePathToPropertiesForTests();
    }

    @NotNull
    @Language( "SQL" )
    static String updateQuery = "UPDATE t_Depo \n" +
            "SET \"nflreport\" = 1 \n" +
            "WHERE [nflreport] = 0 AND n_DEPO_id = null";

    @Test
    public void getColumnsFromDbByTableNameTest(){
        List<ParameterType> t_depo = super.getColumnsFromDbByTableName( "t_Depo" );
        Assert.assertTrue( !t_depo.isEmpty() );
        Assert.assertTrue( t_depo.get( 0 ).getSqlType() != null );
    }

    @Test
    public void prepareQueryTestUpdate() {
        Assert.assertEquals(
                super.prepareQuery( updateQuery ),
                "UPDATE t_Depo \n" +
                        "SET \"nflreport\" = ${nflreport;int;1} \n" +
                        "WHERE [nflreport] = ${nflreport;int;0} AND n_DEPO_id = ${n_DEPO_id;int;null}"
        );
    }

    @Test
    public void prepareQueryTestInsertWithoutColumnList() {
        Assert.assertEquals(
                super.prepareQuery( "INSERT INTO OpsJourn VALUES ( 1, 1 )" ),
                "INSERT INTO OpsJourn values(\n" +
                        "\t${iInstrOpsID;int;1},\n" +
                        "\t${iOrderID;int;1}\n" +
                        ")"
        );
    }

    @Test
    public void prerareQueryTestSelect(){
        Assert.assertEquals(
            super.prepareQuery("SELECT CONVERT ( int , isNull ( svalue , '0' ) ) FROM dbo.t_Depo_setup d " +
                    "WHERE d.n_Depo_ID is NULL AND d.sContext ='Setup' AND d.sDeclare ='ModeSign'"),
            "SELECT CONVERT ( int , isNull ( svalue , '0' ) ) FROM dbo.t_Depo_setup d " +
                    "WHERE d.n_Depo_ID is NULL AND d.sContext = ${scontext;varchar(40);Setup} AND d.sDeclare = ${sdeclare;varchar(40);ModeSign}"
        );
    }

    @Test
    public void prerareQueryTestWithCast(){
        Assert.assertEquals(
                super.prepareQuery( "select s_depo_name, \n" +
                        "s_depo_divname \n" +
                        "from dbo.t_depo \n" +
                        "where n_depo_id = CAST(0 AS INT)" ),
                "select s_depo_name, \n" +
                        "s_depo_divname \n" +
                        "from dbo.t_depo \n" +
                        "where n_depo_id = ${n_depo_id;INT;0}"
        );
    }
    @Test
    public void prerareQueryTestSelectWithConstant(){
        Assert.assertEquals(
            super.prepareQuery("SELECT dbo.tabValue.sshortname, \n" +
                    "dbo.tabStateBonds.sRegNumber \n" +
                    "FROM dbo.tabStateBonds, \n" +
                    "dbo.tabValue \n" +
                    "WHERE dbo.tabvalue.ivalueid = dbo.tabStateBonds.iValueId and \n" +
                    "dbo.tabStateBonds.iValueId = 6295"),
            "SELECT dbo.tabValue.sshortname, \n" +
                    "dbo.tabStateBonds.sRegNumber \n" +
                    "FROM dbo.tabStateBonds, \n" +
                    "dbo.tabValue \n" +
                    "WHERE dbo.tabvalue.ivalueid = dbo.tabStateBonds.iValueId and \n" +
                    "dbo.tabStateBonds.iValueId = ${iValueID;int;6295}"
        );
    }

    @Test
    public void prepareQueryTestInsert() {
        Assert.assertEquals(
                super.prepareQuery( insertQuery ),
                "INSERT INTO dbo.t_DocAdmMembers \n" +
                        "( iDocInID, iGeographyID, sFullName, \n" +
                        "sEngName, dRegDate, \"sOkpoCode\", \n" +
                        "sINN, [sOrgan], sRegNumber, \n" +
                        "sDirLName, sDirFName, sDirFatherName, \n" +
                        "sJurPersName, sInvestorCode, idivisiontype ) \n" +
                        "values(\n" +
                        "\t${iDocInID;int;4058335},\n" +
                        "\t${iGeographyID;int;1},\n" +
                        "\t${sFullName;varchar(255);ru,ss},\n" +
                        "\t'eng',\n" +
                        "\t'11-29-2011 0:0:0.000',\n" +
                        "\t${sOkpoCode;varchar(16);0002},\n" +
                        "\t${sINN;varchar(16);0003},\n" +
                        "\t${sOrgan;varchar(255);omr},\n" +
                        "\t${sRegNumber;varchar(30);0001},\n" +
                        "\t${sDirLName;varchar(40);000002},\n" +
                        "\t${sDirFName;varchar(24);000003},\n" +
                        "\t${sDirFatherName;varchar(24);000004},\n" +
                        "\t${sJurPersName;varchar(255);ru},\n" +
                        "\t${sInvestorCode;varchar(40);ki},\n" +
                        "\t${iDivisionType;int;-1}\n" +
                        ")"
        );
    }

    @Test
    public void determineWorkingModeTest(){
        String query = "'fasdaf:asdfas', \"fa::::::dsfasf\", [dfasd:adsf:::]";
        Assert.assertEquals( determineWorkingMode( query ), WorkingMode.VALUE );
        Assert.assertEquals( determineWorkingMode( query + ":" ), WorkingMode.NAME );
    }

    @Language( "SQL" )
    String queryWithWrappedCast = "SELECT dbo.tabJurPersons.sFullName \n" +
            "FROM dbo.tabMembers, \n" +
            "dbo.tabJurPersons \n" +
            "WHERE ( dbo.tabJurPersons.iMemberCID = dbo.tabMembers.iMemberCID ) and \n" +
            "( dbo.tabMembers.iMemberCID = coalesce(cast(0 as integer),-1) ) and \n" +
            "( coalesce(cast(10 as integer),0) > 0 )";

    @Test
    public void testPrepareQueryWithWrappedCast() {
        String result = QueryPreparator.instance().prepareQuery( queryWithWrappedCast );
        Assert.assertEquals( result,
            "SELECT dbo.tabJurPersons.sFullName \n" +
                    "FROM dbo.tabMembers, \n" +
                    "dbo.tabJurPersons \n" +
                    "WHERE ( dbo.tabJurPersons.iMemberCID = dbo.tabMembers.iMemberCID ) and \n" +
                    "( dbo.tabMembers.iMemberCID = coalesce(${iMemberCID;integer;0},-1) ) and \n" +
                    "( coalesce(cast(10 as integer),0) > 0 )"

                );
    }

}
