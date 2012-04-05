package com.kreig133.daogenerator.db.preparators;

import junit.framework.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * @author eshangareev
 * @version 1.0
 */
public class ValueModeQueryPreparatorTest extends ValueModeQueryPreparator {
    @Test
    public void columnsFromQueryTest() {
        Map<String, String> columnsFromQuery = super.getColumnsFromQuery( QueryPreparatorTest.updateQuery );
        Assert.assertEquals( columnsFromQuery.size(), 2 );
        Assert.assertTrue( columnsFromQuery.keySet().contains( "nflreport" ) );
        Assert.assertTrue( columnsFromQuery.keySet().contains( "n_DEPO_id" ) );
    }

}
