package com.kreig133.daogenerator.db;

import com.kreig133.daogenerator.TestHelper;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.JavaType;
import com.kreig133.daogenerator.jaxb.ParameterType;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class StoreProcedureInfoExtractorTest extends StoreProcedureInfoExtractor{

    @Test
    public void test(){
        final List<ParameterType> sp_bilPg_getBillMakerList =
                StoreProcedureInfoExtractor.getInputParametrsForSP( "sp_bilPg_GetBillMakerList" );

        assertEquals( 37, sp_bilPg_getBillMakerList.size() );
    }
    
    @Test
    public void test2() throws SQLException {
        ResultSet rs = Mockito.mock( ResultSet.class );

        Mockito.when( rs.getString( StoreProcedureInfoExtractor.DATA_TYPE_COLUMN ) ).thenReturn( "int" );
        Mockito.when( rs.getString( StoreProcedureInfoExtractor.PARAMETER_NAME_COLUMN ) ).thenReturn( "id" );

        final ParameterType parameterType = StoreProcedureInfoExtractor.extractDataFromResultSetRow( rs );

        assertEquals( parameterType.getName(), "id" );
        assertEquals( parameterType.getType(), JavaType.LONG );
    }

    @Test
    public void test3(){
        StoreProcedureInfoExtractor.getSPText( "sp_bilPg_GetBillMakerList" );
        final String sp_bilPg_getBillMakerList =  getSPText();
        System.out.println( "sp_bilPg_getBillMakerList = " + sp_bilPg_getBillMakerList );
        assertNotNull( sp_bilPg_getBillMakerList );

    }
    
    @Test
    public void test4(){
//        StoreProcedureInfoExtractor.fillDefaultValues( daoMethod, "" );
    }
}
