package com.kreig133.daogenerator.common;

import com.kreig133.daogenerator.DaoGenerator;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.files.InOutClassGenerator;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.jaxb.ParametersType;
import junit.framework.Assert;
import org.junit.Test;

/**
 * @author eshangareev
 * @version 1.0
 */
public class UtilsTest{
    @Test
    public void testConvertPBNameToName() throws Exception {
        final String fuCk_off_aLL = Utils.convertPBNameToName( "FuCk_off_aLL" );
        Assert.assertEquals( "fuCkOffALL", fuCk_off_aLL );
    }
    
    @Test
    public void testCheckToNeedOwnInClass(){
        DaoGenerator.settings().setType( Type.DEPO );

        final DaoMethod daoMethod = new DaoMethod();
        daoMethod.setInputParametrs( new ParametersType() );
        daoMethod.getInputParametrs().getParameter().add( new ParameterType() );
        
        Assert.assertFalse( InOutClassGenerator.checkToNeedOwnInClass( daoMethod ) );

        DaoGenerator.settings().setType( Type.IASK );

        Assert.assertFalse( InOutClassGenerator.checkToNeedOwnInClass( daoMethod ) );

        daoMethod.getInputParametrs().getParameter().add( new ParameterType() );

        Assert.assertTrue( InOutClassGenerator.checkToNeedOwnInClass( daoMethod ) );

        DaoGenerator.settings().setType( Type.DEPO );

        Assert.assertFalse( InOutClassGenerator.checkToNeedOwnInClass( daoMethod ) );

        daoMethod.getInputParametrs().getParameter().add( new ParameterType() );
        daoMethod.getInputParametrs().getParameter().add( new ParameterType() );

        Assert.assertTrue( InOutClassGenerator.checkToNeedOwnInClass( daoMethod ) );
    }
    
    
}
