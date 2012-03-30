package com.kreig133.daogenerator.files.mybatis.model;

import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.jaxb.ParametersType;
import junit.framework.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;

/**
 * @author eshangareev
 * @version 1.0
 */
public class InModelClassGeneratorTest extends InModelClassGenerator{

    private static final ParametersType PARAMETERS_TYPE = Mockito.mock( ParametersType.class );

    static {
        Mockito.when( PARAMETERS_TYPE.isWithPaging() ).thenReturn( Boolean.TRUE );
    }

    public InModelClassGeneratorTest() {
        super( PARAMETERS_TYPE );
    }

    @Test
    public void filterTest() {
        ArrayList<ParameterType> parameter = new ArrayList<ParameterType>();
        ParameterType type = new ParameterType();
        type.setName( "i_start" );
        parameter.add( type );
        Assert.assertEquals( super.filter( parameter ).size(), 0 );
        Assert.assertEquals( type.getRenameTo(), ParametersType.WithPagingType.I_START.fieldName() );
    }

}
