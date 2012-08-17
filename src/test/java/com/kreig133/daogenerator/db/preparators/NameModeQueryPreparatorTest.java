package com.kreig133.daogenerator.db.preparators;

import junit.framework.Assert;
import org.intellij.lang.annotations.Language;
import org.junit.Test;

/**
 * @author eshangareev
 * @version 1.0
 */
public class NameModeQueryPreparatorTest  extends NameModeQueryPreparator{
    @Test
    public void replaceCastNameModeTest(){
        Assert.assertEquals(
                replaceCastNameMode(
                        "from dbo.t_depo \n" +
                                "where n_depo_id = CAST(:id AS INT)" ),

                "from dbo.t_depo \n" +
                        "where n_depo_id = CAST(${id;INT} AS INT)"
        );
        Assert.assertEquals(
                replaceCastNameMode(
                        "from dbo.t_depo \n" +
                                "where n_depo_id = CAST(:id        AS varchar  ( 255  ) )" ),

                "from dbo.t_depo \n" +
                        "where n_depo_id = CAST(${id;varchar  ( 255  )}        AS varchar  ( 255  ) )"
        );
        Assert.assertEquals(
                replaceCastNameMode(
                        "from dbo.t_depo \n" +
                                "where n_depo_id = CAST(:id   AS numeric  ( 255  , 10 ) )" ),

                "from dbo.t_depo \n" +
                        "where n_depo_id = CAST(${id;numeric  ( 255  , 10 )}   AS numeric  ( 255  , 10 ) )"
        );
    }

    @Language( "SQL" )
    String preparedQueryWithDate = "SELECT mi.ID,\n" +
            "mi.iIncReceiveType,\n" +
            "mi.iInfoSendType,\n" +
            "mi.iOrdReceiveType,\n" +
            "mi.iMemberCID,\n" +
            "mi.iUserID,\n" +
            "mi.dtCreateDateTime,\n" +
            "mi.iIsDeleted,\n" +
            "i.sName,\n" +
            "o.sName,\n" +
            "r.sName,\n" +
            "mi.iOrdReceiveSumPwr\n" +
            "FROM dbo.t_AccDepo ad\n" +
            "LEFT JOIN dbo.t_MemberInteraction mi ON mi.iMemberCID = ad.n_ad_owner AND mi.iIsDeleted = 0\n" +
            "LEFT JOIN dbo.t_InfoSendType i ON i.ID = mi.iInfoSendType\n" +
            "LEFT JOIN dbo.t_OrdReceiveType o ON o.ID = mi.iOrdReceiveType\n" +
            "LEFT JOIN dbo.t_IncReceiveType r ON r.ID = mi.iIncReceiveType\n" +
            "WHERE ad.n_ad_id = CAST(${id;INT} AS INT)\n" +
            "UNION\n" +
            "SELECT 0,\n" +
            "0,\n" +
            "0,\n" +
            "0,\n" +
            "0,\n" +
            "0,\n" +
            "dbo.timestamp(cast('2002-01-01 00:00:00' as varchar(30))),\n" +
            "0,\n" +
            "'',\n" +
            "'',\n" +
            "'',\n" +
            "0 FROM DBO.V_DUMMY";

    @Test
    public void testReplaceOthersNameMode(){
        String result = super.replaceOthersNameMode( preparedQueryWithDate );
        Assert.assertEquals( result, preparedQueryWithDate );
    }
}
