package com.kreig133.daogenerator.db.extractors.in;

import com.kreig133.daogenerator.TestHelper;
import com.kreig133.daogenerator.db.extractors.QueryPreparator;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.settings.Settings;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * @author eshangareev
 * @version 1.0
 */
public class QueryPreparatorTest extends QueryPreparator {

    String query = "UPDATE t_Depo \n" +
            "SET \"nflreport\" = 1 \n" +
            "WHERE [nflreport] = 0 AND n_DEPO_id = '614'";

    @Before
    public void before(){
        Settings.settings().setType( Type.TEST );
    }

    @Test
    public void columnsFromQueryTest() {
        Map<String, String> columnsFromQuery = super.getColumnsFromQuery( query );
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
    public void prepareQueryTest() {
        Assert.assertEquals(
                super.prepareQuery( query ),
                "UPDATE t_Depo \n" +
                        "SET \"nflreport\" = ${nflreport;int;1} \n" +
                        "WHERE [nflreport] = ${nflreport;int;0} AND n_DEPO_id = ${n_DEPO_id;int;614}"
        );
    }
}
