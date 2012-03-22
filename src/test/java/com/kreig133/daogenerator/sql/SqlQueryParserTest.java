package com.kreig133.daogenerator.sql;

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

    @Language( "SQL" )
    String query1 = "execute dbo.sp_ValueSearcher18 @id_mode = 2, @b_resident = -1, @id_emitent = NULL, " +
            "@id_bondkind = NULL, @id_cbbondkind = NULL, @s_shortname = NULL, @s_name = NULL, @s_insidecode = NULL, " +
            "@s_regnum = NULL, @i_num = NULL, @i2_num = NULL, @d_realdate = '3-22-1990 0:0:0.000', " +
            "@d2_realdate = '3-22-2000 0:0:0.000', @d_create = NULL, @d2_create = NULL, @s_isonum = NULL," +
            " @i_rest = NULL, @d_rest = NULL, @id_membauth = NULL, @id_membstore = NULL, @id_status = NULL, " +
            "@id_user = NULL, @iexternal= -1, @dt_modifiedfrom = NULL, @dt_modifiedto = NULL";

    String spName2 = " dbo.sp_ValueSearcher18 ";
    String query2 = "     eXecUte    dbo.sp_ValueSearcher18\n @id_mode";

    @Test
    public void test() {
        final DaoMethod daoMethod = new DaoMethod();
        daoMethod.setCommon( new CommonType() );
        daoMethod.setInputParametrs( new ParametersType() );

        daoMethod.getCommon().setQuery( value );

        final DaoMethod daoMethod1 = SqlQueryParser.instance().parseSqlQueryForParameters( daoMethod );
        System.out.println( daoMethod );
        Assert.assertTrue( daoMethod.getInputParametrs().getParameter().size() == 2 );
    }

    @Test
    public void getStoreProcedureName() {
        Assert.assertEquals( "sp_ValueSearcher18", SqlQueryParser.instance().getStoreProcedureName( query1 ) );
        Assert.assertEquals( "sp_ValueSearcher18", SqlQueryParser.instance().getStoreProcedureName( query2 ) );
        Assert.assertEquals( "sp_ValueSearcher18", SqlQueryParser.instance().getStoreProcedureName( spName2 ) );
    }
    
    @Test
    public void fillTestValuesByInsertedQuery() {
        final ArrayList<ParameterType> inputParametrs = new ArrayList<ParameterType>();
        inputParametrs.add( getParameterType( "d_realdate", JavaType.STRING ) );
        inputParametrs.add( getParameterType( "d2_realdate", JavaType.DATE ) );
        inputParametrs.add( getParameterType( "id_mode", JavaType.DOUBLE ) );
        inputParametrs.add( getParameterType( "b_resident", JavaType.LONG ) );
        inputParametrs.add( getParameterType( "id_user", JavaType.BYTE ) );

        SqlQueryParser.instance().fillTestValuesByInsertedQuery( inputParametrs, query1 );

        Assert.assertEquals( inputParametrs.get( 0 ).getTestValue(), "3-22-1990 0:0:0.000" );
        Assert.assertEquals( inputParametrs.get( 1 ).getTestValue(), "3-22-2000 0:0:0.000" );
        Assert.assertEquals( inputParametrs.get( 2 ).getTestValue(), "2" );
        Assert.assertEquals( inputParametrs.get( 3 ).getTestValue(), "-1" );
        Assert.assertEquals( inputParametrs.get( 4 ).getTestValue(), "null" );
    }

    private ParameterType getParameterType( String name, JavaType type ) {
        ParameterType parameterType = new ParameterType();
        parameterType.setName( name );
        parameterType.setType( type );
        return parameterType;
    }
}
