package com.kreig133.daogenerator.files.equalshashbuilder;

import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.jaxb.ParametersType;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * @author zildarius
 * @version 1.0
 */
public class EqualsMethodBuilderTest {

    @Test
    public void testEqualsBuilder() throws Exception {
        ArrayList<String> etalonParams = new ArrayList<String>();
        etalonParams = EtalonTestClass.getParamList();

        ParametersType params = new ParametersType();

        for (String currentEtalonParam : etalonParams){
            ParameterType param = new ParameterType();
            param.setRenameTo(currentEtalonParam);
            params.getParameter().add(param);
        }

        assertEquals(EtalonTestClass.GENERATED_EQUALS_METHOD,
                     EqualsMethodBuilder.equalsMethodBuilding(params, EtalonTestClass.getEtalonClassName())
                    );
    }
}
