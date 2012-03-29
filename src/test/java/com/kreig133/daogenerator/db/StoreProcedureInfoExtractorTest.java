package com.kreig133.daogenerator.db;

import com.kreig133.daogenerator.db.extractors.in.SpInputParameterExtractor;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.jaxb.*;
import com.kreig133.daogenerator.settings.Settings;
import org.junit.Before;
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
public class StoreProcedureInfoExtractorTest extends SpInputParameterExtractor {

    @Before
    public void before(){
        Settings.settings().setType( Type.TEST );
    }

    @Test
    public void test(){
        DaoMethod daoMethod = new DaoMethod();
        daoMethod.setCommon( new CommonType() );
        daoMethod.setInputParametrs( new ParametersType() );
        daoMethod.getCommon().setSpName( "sp_bilPg_GetBillMakerList" );

        final List<ParameterType> sp_bilPg_getBillMakerList =
                new SpInputParameterExtractor().extractInputParams( daoMethod  ).getInputParametrs().getParameter();

        assertEquals( 37, sp_bilPg_getBillMakerList.size() );
    }
    
    @Test
    public void test2() throws SQLException {
        ResultSet rs = Mockito.mock( ResultSet.class );

        Mockito.when( rs.getString( SpInputParameterExtractor.DATA_TYPE_COLUMN ) ).thenReturn( "int" );
        Mockito.when( rs.getString( SpInputParameterExtractor.PARAMETER_NAME_COLUMN ) ).thenReturn( "id" );
        Mockito.when( rs.getString( SpInputParameterExtractor.PARAMETER_MODE ) ).thenReturn( "IN" );

        final ParameterType parameterType = new SpInputParameterExtractor().extractDataFromResultSetRow( rs );

        assertEquals( parameterType.getName(), "id" );
        assertEquals( parameterType.getType(), JavaType.LONG );
    }

    @Test
    public void test3(){
        super.getSPText( "sp_bilPg_GetBillMakerList" );
        final String sp_bilPg_getBillMakerList =  getSPText();
        System.out.println( "sp_bilPg_getBillMakerList = " + sp_bilPg_getBillMakerList );
        assertNotNull( sp_bilPg_getBillMakerList );

    }
    
    @Test
    public void test4(){
//        StoreProcedureInfoExtractor.fillDefaultValues( daoMethod, "" );
    }

    @Override
    public DaoMethod extractInputParams( DaoMethod daoMethod ) {
        return null;
    }
}
