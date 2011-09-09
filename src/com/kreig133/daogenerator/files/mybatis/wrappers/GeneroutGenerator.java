package com.kreig133.daogenerator.files.mybatis.wrappers;

import com.kreig133.daogenerator.common.Settings;
import com.kreig133.daogenerator.common.strategy.FunctionalObjectWithoutFilter;
import com.kreig133.daogenerator.enums.InputOrOutputType;
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

        iterateForParameterList( builder, inputParametrs, new FunctionalObjectForOutParametres() {
            @Override
            public void writeString( StringBuilder builder, Parameter p ) {
                insertExpressionWithParameter( builder, p.getSqlType() );
            }
        } );

        if( index == 0 ) throw new AssertionError();

        //Записываем в переменные дефолтные значения
        builder.append( "SELECT" );

        index = 0;
        iterateForParameterList( builder, inputParametrs, new FunctionalObjectForOutParametres() {
            @Override
            public void writeString( StringBuilder builder, Parameter p ) {
                insertExpressionWithParameter( builder, "= " + defaultValue( p ) );
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

        iterateForParameterList( builder, inputParametrs, new FunctionalObjectForOutParametres() {
            @Override
            public void writeString( StringBuilder builder, Parameter p ) {
                insertExpressionWithParameter( builder, p.getName() );
            }
        } );

        return builder.toString();
    }

    private static void insertExpressionWithParameter( StringBuilder builder, String rightPartOfExpr ) {
        index = parameterName( builder, index );
        builder.append( rightPartOfExpr );
    }

    static boolean isOutParameter( InputParameter p ) {
        return p.getInputType() == InputOrOutputType.OUT;
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

abstract class FunctionalObjectForOutParametres implements FuctionalObject{
    @Override
    public boolean filter( Parameter p ) {
        return GeneroutGenerator.isOutParameter( ( InputParameter ) p );
    }
}
