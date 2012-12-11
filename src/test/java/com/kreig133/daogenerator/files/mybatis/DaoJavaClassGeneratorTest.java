package com.kreig133.daogenerator.files.mybatis;

import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.jaxb.ParametersType;
import junit.framework.Assert;
import org.junit.Test;

import static com.kreig133.daogenerator.files.mybatis.DaoJavaClassGenerator.checkToNeedOwnInClass;

/**
 * @author eshangareev
 * @version 1.0
 */
public class DaoJavaClassGeneratorTest {
    @Test
    public void testCheckToNeedOwnInClass(){

        final DaoMethod daoMethod = new DaoMethod();
        daoMethod.setInputParametrs( new ParametersType() );
        daoMethod.getInputParametrs().getParameter().add( new ParameterType() );

        Assert.assertFalse( checkToNeedOwnInClass( daoMethod ) );

        daoMethod.getInputParametrs().getParameter().add( new ParameterType() );

        Assert.assertTrue( checkToNeedOwnInClass( daoMethod ) );
    }
}
