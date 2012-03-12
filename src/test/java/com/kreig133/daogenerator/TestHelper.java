package com.kreig133.daogenerator;

import com.kreig133.daogenerator.jaxb.*;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class TestHelper {

    public static DaoMethod getDaoMethodForTest(){
        final DaoMethod daoMethod = new DaoMethod();

        daoMethod.setCommon( new CommonType() );
        daoMethod.getCommon().setMethodName( "methodName" );

        daoMethod.getCommon().setConfiguration( new ConfigurationType () );
        daoMethod.getCommon().getConfiguration().setType( SelectType.GENEROUT );


        daoMethod.setInputParametrs( new ParametersType() );
        final List<ParameterType> parameter = daoMethod.getInputParametrs().getParameter();

        ParameterType parameterType = new ParameterType();

        parameterType.setType           ( JavaType.LONG );
        parameterType.setComment        ( "Тут был Вася" );
        parameterType.setInOut          ( InOutType.OUT );
        parameterType.setDefaultValue   ( "null" );
        parameterType.setRenameTo       ( "newParameterName" );
        parameterType.setName           ( "parameterName" );

        parameter.add( parameterType );

        parameterType = new ParameterType();

        parameterType.setName( "параметр" );
        parameterType.setRenameTo( "параметрКакПараметр" );
        parameterType.setType( JavaType.LONG );
        parameterType.setDefaultValue( "123" );
        parameterType.setInOut( InOutType.IN );

        parameterType = new ParameterType();

        parameterType.setName( "321" );
        parameterType.setRenameTo( "одавыолдавылодавыф" );
        parameterType.setInOut( InOutType.IN );
        parameterType.setDefaultValue( "" );
        parameterType.setType( JavaType.STRING );

        parameter.add( parameterType );

        daoMethod.setOutputParametrs( daoMethod.getInputParametrs() );

        return daoMethod;
    }
}
