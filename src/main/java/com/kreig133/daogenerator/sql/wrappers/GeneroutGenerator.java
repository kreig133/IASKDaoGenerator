package com.kreig133.daogenerator.sql.wrappers;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.strategy.FuctionalObject;
import com.kreig133.daogenerator.common.strategy.FunctionalObjectWithoutFilter;
import com.kreig133.daogenerator.enums.InputOrOutputType;
import com.kreig133.daogenerator.parameter.InputParameter;
import com.kreig133.daogenerator.parameter.Parameter;

import java.util.List;

import static com.kreig133.daogenerator.common.StringBuilderUtils.iterateForParameterList;

/**
 * @author eshangareev
 * @version 1.0
 */
public class GeneroutGenerator extends CommonWrapperGenerator{

    private static int index = 0;

    public static void generateWrapper( FunctionSettings functionSettings ) {
        final List<Parameter> inputParametrs    = functionSettings.getInputParameterList();

        StringBuilder myBatisQuery      = new StringBuilder();
        StringBuilder queryForTesting   = new StringBuilder();

        //Декларация переменных
        myBatisQuery.append( "DECLARE\n" );

        iterateForParameterList( myBatisQuery, inputParametrs, new FunctionalObjectForOutParametres() {
            @Override
            public void writeString( StringBuilder builder, Parameter p ) {
                insertExpressionWithParameter( builder, p.getSqlType() );
            }
        } );

        if( index == 0 ) throw new AssertionError();

        //Записываем в переменные дефолтные значения
        myBatisQuery.append( "SELECT" );

        index = 0;
        iterateForParameterList( myBatisQuery, inputParametrs, new FunctionalObjectForOutParametres() {
            @Override
            public void writeString( StringBuilder builder, Parameter p ) {
                insertExpressionWithParameter( builder, "= " + defaultValue( p ) );
            }
        } );

        //Выполняем хранимую процедуру
        myBatisQuery.append( "EXECUTE DBO." ).append( functionSettings.getNameForCall() ).append( "\n" );

        queryForTesting.append( myBatisQuery.toString() );

        index = 0;
        iterateForParameterList( myBatisQuery, inputParametrs, new FunctionalObjectWithoutFilter() {
            @Override
            public void writeString( StringBuilder builder, Parameter p ) {
                index = declareParamInProcedure( builder, p, index );
            }
        } );

        index = 0;
        iterateForParameterList( queryForTesting, inputParametrs, new FunctionalObjectWithoutFilter() {
            @Override
            public void writeString( StringBuilder builder, Parameter p ) {
                index = declareParamInProcedureForTesting( builder, p, index );
            }
        } );

        //вернуть полученные значения

        myBatisQuery    .append( "\nSELECT\n" );
        queryForTesting .append( "\nSELECT\n" );

        index = 0;
        iterateForParameterList( myBatisQuery, inputParametrs, new FunctionalObjectForOutParametres() {
            @Override
            public void writeString( StringBuilder builder, Parameter p ) {
                insertExpressionWithParameter( builder, p.getName() );
            }
        } );
        index = 0;
        iterateForParameterList( queryForTesting, inputParametrs, new FunctionalObjectForOutParametres() {
            @Override
            public void writeString( StringBuilder builder, Parameter p ) {
                insertExpressionWithParameter( builder, p.getName() );
            }
        } );

        functionSettings.setMyBatisQuery         ( myBatisQuery   .toString() );
        functionSettings.appendToQueryForTesting ( queryForTesting.toString() );
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
                return "''";
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
