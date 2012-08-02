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

    public static final String DATA_TYPE_COLUMN = "dtc";
    public static final String NUMERIC_PRECISION = "np";
    public static final String NUMERIC_SCALE = "ns";

    @Before
    public void before() throws SQLException {
        rs = Mockito.mock( ResultSet.class );
        Mockito.when( rs.getString( DATA_TYPE_COLUMN ) ).thenReturn( "numeric" );
        Mockito.when( rs.getString( NUMERIC_PRECISION ) ).thenReturn( "18" );
        Mockito.when( rs.getString( NUMERIC_SCALE ) ).thenReturn( "6" );
    }

    @Test
    public void testGetSqlTypeFromResultSet() throws Exception {
        String sqlTypeFromResultSet = SqlTypeHelper.getSqlTypeFromResultSet( rs, new SqlTypeHelper.ColumnNameHolder() {
            @Override
            public String getNumericScaleColumnName() {
                return NUMERIC_SCALE;
            }

            @Override
            public String getNumericPrecisionColumnName() {
                return NUMERIC_PRECISION;
            }

            @Override
            public String getCharacterMaximumLengthColumnName() {
                return null;
            }

            @Override
            public String getDataTypeColumnName() {
                return DATA_TYPE_COLUMN;
            }
        } );
        Assert.assertEquals( sqlTypeFromResultSet, "numeric(18,6)");
    }
}
