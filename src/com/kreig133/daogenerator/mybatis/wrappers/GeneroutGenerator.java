package com.kreig133.daogenerator.mybatis.wrappers;

import com.kreig133.daogenerator.common.Settings;
import com.kreig133.daogenerator.common.strategy.FunctionalObjectWithoutFilter;
import com.kreig133.daogenerator.enums.InputParameterType;
import com.kreig133.daogenerator.common.strategy.FuctionalObject;
import com.kreig133.daogenerator.parameter.InputParameter;
import com.kreig133.daogenerator.parameter.Parameter;

import java.util.List;

import static com.kreig133.daogenerator.common.Utils.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class GeneroutGenerator extends CommonWrapperGenerator{

    private static int index = 0;

    public static String generateWrapper( Settings settings ) {
        final List<Parameter> inputParametrs    = settings.getInputParameterList();
        final String          name              = settings.getFunctionName();

        StringBuilder builder = new StringBuilder();
        //Декларация переменных

        builder.append( "DECLARE\n" );

        iterateForParameterList( builder, inputParametrs, new FuctionalObject() {
            @Override
            public void writeString( StringBuilder builder, Parameter p ) {
                index = parameterName( builder, index );
                builder.append( p.getSqlType() );
            }
            @Override
            public boolean filter( Parameter p ) {
                return isOutParameter( ( InputParameter ) p );
            }
        } );

        if( index == 0 ) throw new AssertionError();

        //Записываем в переменные дефолтные значения
        builder.append( "SELECT" );

        index = 0;
        iterateForParameterList( builder, inputParametrs, new FuctionalObject() {
            @Override
            public void writeString( StringBuilder builder, Parameter p ) {
                index = parameterName( builder, index );
                builder.append( "= " ).append( defaultValue( p ) );
            }
            @Override
            public boolean filter( Parameter p ) {
                return isOutParameter( ( InputParameter ) p );
            }
        } );

        //Выполняем хранимую процедуру
        builder.append( "EXECUTE DBO." ).append( name ).append( "\n" );

        index = 0;
        iterateForParameterList( builder, inputParametrs, new FunctionalObjectWithoutFilter() {
            @Override
            public void writeString( StringBuilder builder, Parameter p ) {
                index = declareParamInProcedure( builder, p, index );
            }
        } );

        //вернуть полученные значения
        index = 0;
        builder.append( "\nSELECT\n" );
        iterateForParameterList( builder, inputParametrs, new FuctionalObject() {
            @Override
            public void writeString( StringBuilder builder, Parameter p ) {
                index = parameterName( builder, index );
                builder.append( p.getName() );
            }
            @Override
            public boolean filter( Parameter p ) {
                return isOutParameter( ( InputParameter ) p );
            }
        } );

        return builder.toString();
    }

    private static boolean isOutParameter( InputParameter p ) {
        return p.getInputType() == InputParameterType.OUT;
    }

    private static String defaultValue( Parameter parameter ) {
        switch ( parameter.getType() ){
            case Double:
            case Long:
                return "0";
            case Date:
                return "NULL";
            case String:
                return "\"\"";
        }
        throw new IllegalArgumentException();
    }
}
