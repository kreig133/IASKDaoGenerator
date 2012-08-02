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
    static String updateQuery =
            "UPDATE \n" +
            "\ttDockPack\n" +
            "SET \n" +
            "\tnDocTypeID = 12345\n" +
            "WHERE nPackID = 1 AND nDeleted = 0";

    @Test
    public void getColumnsFromDbByTableNameTest(){
        List<ParameterType> t_depo = super.getColumnsFromDbByTableName( "tDocPack" );
        Assert.assertTrue( !t_depo.isEmpty() );
        Assert.assertTrue( t_depo.get( 0 ).getSqlType() != null );
    }

    @Test
    public void prepareQueryTestUpdate() {
        Assert.assertEquals(
                super.prepareQuery( "UPDATE teSponsor SET sName = :sName, sPart=:sPart, sText=:sText WHERE nRecID = :nRecID" ),
                "UPDATE teSponsor SET sName = ${sName;varchar(255)}, sPart=${sPart;varchar(255)}, sText=${sText;varchar(255)} WHERE nRecID = ${nRecID;int identity}"
        );
    }

    @Test
    public void prepareQueryTestInsertWithoutColumnList() {
        Assert.assertEquals(
                super.prepareQuery( "INSERT INTO tLimitTypes VALUES (1, \'раздватри\')" ),
                "INSERT INTO tLimitTypes values(\n" +
                        "\t${nLimitTypeID;int;1},\n" +
                        "\t${sLimitTypeName;varchar(100);раздватри}\n" +
                ")"
        );
    }

    @Test
    public void prerareQueryTestSelect(){
        Assert.assertEquals(
            super.prepareQuery("SELECT\n" +
                    "tDocPack.nDocID\n" +
                    "FROM \n" +
                    "tDocPack ( NOLOCK )\n" +
                    "WHERE \n" +
                    "tDocPack.nDocTypeID = 59 and\n" +
                    "isnull ( tDocPack.ndeleted , 0 ) = 0 and\n" +
                    "tDocPack.nPackID = 250287 "),

                    "SELECT\n" +
                    "tDocPack.nDocID\n" +
                    "FROM \n" +
                    "tDocPack ( NOLOCK )\n" +
                    "WHERE \n" +
                    "tDocPack.nDocTypeID = ${nDocTypeID;int;59} and\n" +
                    "isnull ( tDocPack.ndeleted , 0 ) = 0 and\n" +
                    "tDocPack.nPackID = ${nPackID;int;250287} "
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
            super.prepareQuery("select fl.nPackID, sd.nLapScID /*, **/ from tFinLaps fl\n" +
                    "join tScensData sd on fl.nScenRecID = sd.nScenRecID\n" +
                    "where nPackID = 249616"),
                    "select fl.nPackID, sd.nLapScID /*, **/ from tFinLaps fl\n" +
                    "join tScensData sd on fl.nScenRecID = sd.nScenRecID\n" +
                    "where nPackID = ${nPackID;int;249616}"
        );
    }

    @Test
    public void prepareQueryTestInsert() {
        Assert.assertEquals(
                super.prepareQuery( "INSERT INTO teSponsor ( nCUID, nCreDataID, sName, sPart ) VALUES ( 5597, 267059, 'ааа', 'ппп' )" ),
                "INSERT INTO teSponsor ( nCUID, nCreDataID, sName, sPart ) values(\n" +
                        "\t${nCUID;int;5597},\n" +
                        "\t${nCreDataID;int;267059},\n" +
                        "\t${sName;varchar(255);ааа},\n" +
                        "\t${sPart;varchar(255);ппп}\n" +
                        ")"
        );
    }

    @Test
    public void determineWorkingModeTest(){
        String query = "'fasdaf:asdfas', \"fa::::::dsfasf\", [dfasd:adsf:::]";
        Assert.assertEquals( determineWorkingMode( query ), WorkingMode.VALUE );
        Assert.assertEquals( determineWorkingMode( query + ":" ), WorkingMode.NAME );
    }



}
