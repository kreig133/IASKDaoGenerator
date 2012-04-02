package com.kreig133.daogenerator.db.extractors;

import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.settings.Settings;
import junit.framework.Assert;
import org.intellij.lang.annotations.Language;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * @author eshangareev
 * @version 1.0
 */
public class QueryPreparatorTest extends QueryPreparator {

    @Language( "SQL" )
    String insertQuery =
                    "INSERT INTO dbo.t_DocAdmMembers \n" +
                    "( iDocInID, iGeographyID, sFullName, \n" +
                    "#@!sEngName, #@!dRegDate, sOkpoCode, \n" +
                    "sINN, sOrgan, sRegNumber, \n" +
                    "sDirLName, sDirFName, sDirFatherName, \n" +
                    "sJurPersName, sInvestorCode, idivisiontype ) \n" +
                    "VALUES\n" +
                    "( 4058335, 1, 'ru,ss', 'eng', \n" +
                    "'11-29-2011 0:0:0.000', '0002', \n" +
                    "'0003', 'omr', '0001', '000002', \n" +
                    "'000003', '000004', 'ru', 'ki', -1 )";

    @Language( "SQL" )
    String updateQuery = "UPDATE t_Depo \n" +
            "SET \"nflreport\" = 1 \n" +
            "WHERE [nflreport] = 0 AND n_DEPO_id = '614'";

    @Before
    public void before(){
        Settings.settings().setType( Type.TEST );
    }

    @Test
    public void columnsFromQueryTest() {
        Map<String, String> columnsFromQuery = super.getColumnsFromQuery( updateQuery );
        Assert.assertEquals( columnsFromQuery.size(), 2 );
        Assert.assertTrue( columnsFromQuery.keySet().contains( "nflreport" ) );
        Assert.assertTrue( columnsFromQuery.keySet().contains( "n_DEPO_id" ) );
    }

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
                        "WHERE [nflreport] = ${nflreport;int;0} AND n_DEPO_id = ${n_DEPO_id;int;614}"
        );
    }

    @Test
    public void prepareQueryTestInsert() {
        Assert.assertEquals(
                super.prepareQuery( insertQuery ),
                "INSERT INTO dbo.t_DocAdmMembers \n" +
                        "( iDocInID, iGeographyID, sFullName, \n" +
                        "sEngName, dRegDate, sOkpoCode, \n" +
                        "sINN, sOrgan, sRegNumber, \n" +
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


}
