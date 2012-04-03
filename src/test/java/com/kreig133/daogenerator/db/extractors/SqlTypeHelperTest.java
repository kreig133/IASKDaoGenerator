package com.kreig133.daogenerator.db.extractors;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author eshangareev
 * @version 1.0
 */
public class SqlTypeHelperTest {

    ResultSet rs;

    @Before
    public void before() throws SQLException {
        rs = Mockito.mock( ResultSet.class );
        Mockito.when( rs.getString( SqlTypeHelper.DATA_TYPE_COLUMN ) ).thenReturn( "numeric" );
        Mockito.when( rs.getString( SqlTypeHelper.NUMERIC_PRECISION ) ).thenReturn( "18" );
        Mockito.when( rs.getString( SqlTypeHelper.NUMERIC_SCALE ) ).thenReturn( "6" );
    }

    @Test
    public void testGetSqlTypeFromResultSet() throws Exception {
        String sqlTypeFromResultSet = SqlTypeHelper.getSqlTypeFromResultSet( rs );
        Assert.assertEquals( sqlTypeFromResultSet, "numeric(18,6)");
    }
}
