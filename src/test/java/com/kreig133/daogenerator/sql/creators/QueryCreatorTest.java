package com.kreig133.daogenerator.sql.creators;

import com.kreig133.daogenerator.TestHelper;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.jaxb.SelectType;
import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class QueryCreatorTest {
    @Test
    public void testNewInstance() throws Exception {
        DaoMethod daoMethod = TestHelper.getDaoMethodForTest();
        daoMethod.getCommon().getConfiguration().setType( SelectType.CALL );

        List<ParameterType> typeList = new ArrayList<ParameterType>();
        ParameterType parameterTypeWithEmptyName = new ParameterType();
        parameterTypeWithEmptyName.setName( "" );
        ParameterType parameterTypeWithNormalName = new ParameterType();
        parameterTypeWithNormalName.setName( "name" );

        typeList.add( parameterTypeWithEmptyName );
        typeList.add( parameterTypeWithEmptyName );

        setOutputParameters( daoMethod, typeList );

        QueryCreator queryCreator = QueryCreatorFabric.newInstance( daoMethod );
        Assert.assertTrue( queryCreator instanceof WrapperGenerator );

        typeList.clear();
        typeList.add( parameterTypeWithEmptyName );
        typeList.add( parameterTypeWithNormalName );

        setOutputParameters(daoMethod, typeList);

        queryCreator = QueryCreatorFabric.newInstance( daoMethod );
        Assert.assertTrue( queryCreator instanceof ProcedureCallCreator );
    }

    private void setOutputParameters( DaoMethod daoMethod, List<ParameterType> typeList ) {
        daoMethod.getOutputParametrs().getParameter().clear();
        daoMethod.getOutputParametrs().getParameter().addAll( typeList );
    }
}
