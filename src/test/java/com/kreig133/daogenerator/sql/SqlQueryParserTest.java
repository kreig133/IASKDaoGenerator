package com.kreig133.daogenerator.sql;

import com.kreig133.daogenerator.TestHelper;
import com.kreig133.daogenerator.db.extractors.Extractor;
import com.kreig133.daogenerator.db.extractors.in.SelectInputParameterExtractor;
import com.kreig133.daogenerator.db.extractors.in.SpInputParameterExtractor;
import com.kreig133.daogenerator.jaxb.*;
import org.intellij.lang.annotations.Language;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author eshangareev
 * @version 1.0
 */
public class SqlQueryParserTest {

    @Language( "SQL" )
    private String value;

    @Before
    public void setUp() throws Exception {
        value = "SELECT * FROM tTable WHERE a = ${firstParam;varchar(255);first} AND b = ${second;int;123}}";
    }



    String spName2 = " dbo.sp_ValueSearcher18 ";
    String query2 = "     eXecUte    dbo.sp_ValueSearcher18\n @id_mode";

    @Test
    public void test() {
        final DaoMethod daoMethod = new DaoMethod();
        daoMethod.setCommon( new CommonType() );
        daoMethod.setInputParametrs( new ParametersType() );

        daoMethod.getCommon().setQuery( value );

        final DaoMethod daoMethod1 = new SelectInputParameterExtractor().extractInputParams( daoMethod );
        System.out.println( daoMethod );
        Assert.assertTrue( daoMethod.getInputParametrs().getParameter().size() == 2 );
    }

    @Test
    public void getStoreProcedureName() {
        Assert.assertEquals( "sp_ValueSearcher18", Extractor.getStoreProcedureName( TestHelper.spCall ) );
        Assert.assertEquals( "sp_ValueSearcher18", Extractor.getStoreProcedureName( query2 ) );
        Assert.assertEquals( "sp_ValueSearcher18", Extractor.getStoreProcedureName( spName2 ) );
    }
    
    @Test
    public void fillTestValuesByInsertedQuery() {
        final ArrayList<ParameterType> inputParametrs = new ArrayList<ParameterType>();
        inputParametrs.add( getParameterType( "d_realdate", JavaType.STRING ) );
        inputParametrs.add( getParameterType( "d2_realdate", JavaType.DATE ) );
        inputParametrs.add( getParameterType( "id_mode", JavaType.DOUBLE ) );
        inputParametrs.add( getParameterType( "b_resident", JavaType.BYTE ) );
        inputParametrs.add( getParameterType( "id_user", JavaType.STRING ) );
        inputParametrs.add( getParameterType( "iexternal", JavaType.LONG ) );

        DaoMethod daoMethodForTest = TestHelper.getDaoMethodForTest();
        daoMethodForTest.getInputParametrs().getParameter().clear();
        daoMethodForTest.getInputParametrs().getParameter().addAll( inputParametrs );
        daoMethodForTest.getCommon().setQuery( TestHelper.spCall );

        new SpInputParameterExtractor().fillTestValuesByInsertedQuery( daoMethodForTest );

        Assert.assertEquals( inputParametrs.get( 0 ).getTestValue(), "3-22-1990 0:0:0.000" );
        Assert.assertEquals( inputParametrs.get( 1 ).getTestValue(), "3-22-2000 0:0:0.000" );
        Assert.assertEquals( inputParametrs.get( 2 ).getTestValue(), "2" );
        Assert.assertEquals( inputParametrs.get( 3 ).getTestValue(), "-1" );
        Assert.assertEquals( inputParametrs.get( 4 ).getTestValue(), "null" );
        Assert.assertEquals( inputParametrs.get( 5 ).getTestValue(), "-1" );
    }

    private ParameterType getParameterType( String name, JavaType type ) {
        ParameterType parameterType = new ParameterType();
        parameterType.setName( name );
        parameterType.setType( type );
        return parameterType;
    }
}
