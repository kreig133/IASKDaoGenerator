package com.kreig133.daogenerator.files.builder;

import com.kreig133.daogenerator.TestHelper;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import junit.framework.Assert;
import org.junit.Test;

/**
 * @author eshangareev
 * @version 1.0
 */
public class FileBuilderTest {
    @Test
    public void testCheckJavaClassNames() {
        DaoMethod daoMethodForTest = TestHelper.getDaoMethodForTest();
        daoMethodForTest.getOutputParametrs().setJavaClassName( "" );

        Assert.assertFalse( FileBuilder.checkJavaClassNames( daoMethodForTest ) );

        daoMethodForTest.getOutputParametrs().setJavaClassName( "123" );

        Assert.assertTrue( FileBuilder.checkJavaClassNames( daoMethodForTest ) );
    }

    @Test
    public void test(){
        DaoMethod daoMethodForTest = TestHelper.getDaoMethodForTest();
        daoMethodForTest.getInputParametrs().getParameter().get( 0 ).setRenameTo( "" );
        Assert.assertFalse( FileBuilder.checkRenameTos( daoMethodForTest ) );

        daoMethodForTest.getInputParametrs().getParameter().get( 0 ).setRenameTo(
                daoMethodForTest.getInputParametrs().getParameter().get( 1 ).getRenameTo()
        );
        Assert.assertFalse( FileBuilder.checkRenameTos( daoMethodForTest ) );

        DaoMethod daoMethod = TestHelper.getDaoMethodForTest();
        daoMethod.getInputParametrs ().getParameter().clear();
        daoMethod.getOutputParametrs().getParameter().clear();
        daoMethod.getInputParametrs().getParameter().add( new ParameterType() );

        Assert.assertFalse( FileBuilder.checkRenameTos( daoMethod ) );

        daoMethod.getInputParametrs().getParameter().get( 0 ).setRenameTo( "name" );

        Assert.assertTrue( FileBuilder.checkRenameTos( daoMethod ) );

        daoMethod.getInputParametrs().getParameter().add( new ParameterType() );

        Assert.assertFalse( FileBuilder.checkRenameTos( daoMethod ) );

        daoMethod.getInputParametrs().getParameter().get( 1 ).setRenameTo( "name" );

        Assert.assertFalse( FileBuilder.checkRenameTos( daoMethod ) );

        daoMethod.getInputParametrs().getParameter().get( 1 ).setRenameTo( "name1" );

        Assert.assertTrue( FileBuilder.checkRenameTos( daoMethod ) );
    }
}
