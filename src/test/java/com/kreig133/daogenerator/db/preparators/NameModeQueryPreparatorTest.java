package com.kreig133.daogenerator.db.preparators;

import junit.framework.Assert;
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
}
