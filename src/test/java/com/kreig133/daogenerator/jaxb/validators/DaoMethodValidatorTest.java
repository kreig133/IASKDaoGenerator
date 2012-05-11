package com.kreig133.daogenerator.jaxb.validators;

import com.kreig133.daogenerator.TestHelper;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import junit.framework.Assert;
import org.junit.Test;

/**
 * @author eshangareev
 * @version 1.0
 */
public class DaoMethodValidatorTest {
    @Test
    public void testCheckRenameTos(){
        DaoMethod daoMethodForTest = TestHelper.getDaoMethodForTest();
        daoMethodForTest.getInputParametrs().getParameter().get( 0 ).setRenameTo( "" );
        Assert.assertFalse( DaoMethodValidator.checkRenameTos( daoMethodForTest ) );

        daoMethodForTest.getInputParametrs().getParameter().get( 0 ).setRenameTo(
                daoMethodForTest.getInputParametrs().getParameter().get( 1 ).getRenameTo()
        );
        Assert.assertFalse( DaoMethodValidator.checkRenameTos( daoMethodForTest ) );

        DaoMethod daoMethod = TestHelper.getDaoMethodForTest();
        daoMethod.getInputParametrs ().getParameter().clear();
        daoMethod.getOutputParametrs().getParameter().clear();
        daoMethod.getInputParametrs().getParameter().add( new ParameterType() );

        Assert.assertFalse( DaoMethodValidator.checkRenameTos( daoMethod ) );

        daoMethod.getInputParametrs().getParameter().get( 0 ).setRenameTo( "name" );

        Assert.assertTrue( DaoMethodValidator.checkRenameTos( daoMethod ) );

        daoMethod.getInputParametrs().getParameter().add( new ParameterType() );

        Assert.assertFalse( DaoMethodValidator.checkRenameTos( daoMethod ) );

        daoMethod.getInputParametrs().getParameter().get( 1 ).setRenameTo( "name" );

        Assert.assertFalse( DaoMethodValidator.checkRenameTos( daoMethod ) );

        daoMethod.getInputParametrs().getParameter().get( 1 ).setRenameTo( "name1" );

        Assert.assertTrue( DaoMethodValidator.checkRenameTos( daoMethod ) );
    }
}
