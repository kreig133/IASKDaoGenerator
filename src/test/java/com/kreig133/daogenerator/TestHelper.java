package com.kreig133.daogenerator;

import com.kreig133.daogenerator.jaxb.*;
import org.intellij.lang.annotations.Language;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class TestHelper {
    @Language( "SQL" )
    public static String spCall = "execute dbo.sp_ValueSearcher18 @id_mode = 2, @b_resident = -1, @id_emitent = NULL, " +
            "@id_bondkind = NULL, @id_cbbondkind = NULL, @s_shortname = NULL, @s_name = NULL, @s_insidecode = NULL, " +
            "@s_regnum = NULL, @i_num = NULL, @i2_num = NULL, @d_realdate = '3-22-1990 0:0:0.000', " +
            "@d2_realdate = '3-22-2000 0:0:0.000', @d_create = NULL, @d2_create = NULL, @s_isonum = NULL," +
            " @i_rest = NULL, @d_rest = NULL, @id_membauth = NULL, @id_membstore = NULL, @id_status = NULL, " +
            "@id_user = NULL, @iexternal= -1, @dt_modifiedfrom = NULL, @dt_modifiedto = NULL";

    public static DaoMethod getDaoMethodForTest(){
        final DaoMethod daoMethod = new DaoMethod();

        daoMethod.setCommon( new CommonType() );
        daoMethod.getCommon().setMethodName( "methodName" );

        daoMethod.getCommon().setConfiguration( new ConfigurationType () );
        daoMethod.getCommon().getConfiguration().setType( SelectType.CALL );


        daoMethod.setInputParametrs( new ParametersType() );
        final List<ParameterType> parameter = daoMethod.getInputParametrs().getParameter();

        ParameterType parameterType = new ParameterType();

        parameterType.setType           ( JavaType.LONG );
        parameterType.setComment        ( "Тут был Вася" );
        parameterType.setInOut          ( InOutType.OUT );
        parameterType.setSqlType        ( "numeric(12,2)" );
        parameterType.setDefaultValue   ( "null" );
        parameterType.setRenameTo       ( "newParameterName" );
        parameterType.setName           ( "parameterName" );

        parameter.add( parameterType );

        parameterType = new ParameterType();

        parameterType.setName( "параметр" );
        parameterType.setRenameTo( "параметрКакПараметр" );
        parameterType.setSqlType( "nchar(10)" );
        parameterType.setType( JavaType.LONG );
        parameterType.setDefaultValue( "123" );
        parameterType.setInOut( InOutType.IN );

        parameterType = new ParameterType();

        parameterType.setName( "321" );
        parameterType.setRenameTo( "одавыолдавылодавыф" );
        parameterType.setInOut( InOutType.IN );
        parameterType.setDefaultValue( "" );
        parameterType.setSqlType( "nchar(10)" );
        parameterType.setType( JavaType.STRING );

        parameter.add( parameterType );

        daoMethod.setOutputParametrs( daoMethod.getInputParametrs() );

        return daoMethod;
    }
}
