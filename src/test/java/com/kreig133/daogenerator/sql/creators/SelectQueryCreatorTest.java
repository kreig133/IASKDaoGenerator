package com.kreig133.daogenerator.sql.creators;

import com.kreig133.daogenerator.TestHelper;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import junit.framework.Assert;
import org.intellij.lang.annotations.Language;
import org.junit.Test;

/**
 * @author eshangareev
 * @version 1.0
 */
public class SelectQueryCreatorTest {

    @Language( "SQL" )
    private static String inputQuery =
            "SELECT MIN(inct.iIncomeTransferID) AS MINiIncomeTransferID, COUNT(*) AS COUNTIncomeTransfer \n" +
            "FROM dbo.t_IncomeTransfer inct WITH (NOLOCK)  \n" +
            "WHERE inct.iIsDeleted = 0 " +
                    "and inct.iIncomeStatusID &lt;&gt; 4 " +
                    "and inct.iIncomeCalcID = ${iIncomeCalcID;int;2876}";

    @Test
    public void testGenerateExecuteQuery() throws Exception {
        SelectQueryCreator selectQueryCreator = new SelectQueryCreator();

        ParameterType parameterType = new ParameterType();
        parameterType.setName( "iIncomeCalcID" );
        parameterType.setRenameTo( parameterType.getName() );
        parameterType.setSqlType( "int" );

        DaoMethod daoMethodForTest = TestHelper.getDaoMethodForTest();
        daoMethodForTest.getInputParametrs().getParameter().clear();
        daoMethodForTest.getInputParametrs().getParameter().add( parameterType );
        daoMethodForTest.getCommon().setQuery( inputQuery );


        Assert.assertEquals( selectQueryCreator.generateExecuteQuery( daoMethodForTest, false ),
                "SELECT MIN(inct.iIncomeTransferID) AS MINiIncomeTransferID, COUNT(*) AS COUNTIncomeTransfer \n" +
                        "FROM dbo.t_IncomeTransfer inct WITH (NOLOCK)  \n" +
                        "WHERE inct.iIsDeleted = 0 and inct.iIncomeStatusID &lt;&gt; 4 and inct.iIncomeCalcID = #{iIncomeCalcID}" );
    }
}
