package com.kreig133.daogenerator.sql.wrappers;

import com.kreig133.daogenerator.common.strategy.FuctionalObject;
import com.kreig133.daogenerator.common.strategy.FunctionalObjectWithoutFilter;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.InOutType;
import com.kreig133.daogenerator.jaxb.ParameterType;

import java.util.List;

import static com.kreig133.daogenerator.common.StringBuilderUtils.iterateForParameterList;

/**
 * @author eshangareev
 * @version 1.0
 */
public class GeneroutGenerator extends CommonWrapperGenerator{

    private static int index = 0;

    public static String generateWrapper( DaoMethod daoMethod ) {
        final List<ParameterType> inputParametrs    = daoMethod.getInputParametrs().getParameter();

        StringBuilder myBatisQuery      = new StringBuilder();

        //Декларация переменных
        myBatisQuery.append( "DECLARE\n" );

        iterateForParameterList( myBatisQuery, inputParametrs, new FunctionalObjectForOutParametres() {
            @Override
            public void writeString( StringBuilder builder, ParameterType p ) {
                insertExpressionWithParameter( builder, p.getSqlType() );
            }
        } );

        if( index == 0 ) throw new AssertionError();

        //Записываем в переменные дефолтные значения
        myBatisQuery.append( "SELECT" );

        index = 0;
        iterateForParameterList( myBatisQuery, inputParametrs, new FunctionalObjectForOutParametres() {
            @Override
            public void writeString( StringBuilder builder, ParameterType p ) {
                insertExpressionWithParameter( builder, "= " + defaultValue( p ) );
            }
        } );

        //Выполняем хранимую процедуру
        myBatisQuery.append( "EXECUTE DBO." ).append( daoMethod.getCommon().getSpName() ).append( "\n" );

        index = 0;
        iterateForParameterList( myBatisQuery, inputParametrs, new FunctionalObjectWithoutFilter() {
            @Override
            public void writeString( StringBuilder builder, ParameterType p ) {
                index = declareParamInProcedure( builder, p, index );
            }
        } );

        //вернуть полученные значения

        myBatisQuery    .append( "\nSELECT\n" );

        index = 0;
        iterateForParameterList( myBatisQuery, inputParametrs, new FunctionalObjectForOutParametres() {
            @Override
            public void writeString( StringBuilder builder, ParameterType p ) {
                insertExpressionWithParameter( builder, p.getName() );
            }
        } );

        return myBatisQuery.toString();
    }

    private static void insertExpressionWithParameter( StringBuilder builder, String rightPartOfExpr ) {
        index = parameterName( builder, index );
        builder.append( rightPartOfExpr );
    }

    static boolean isOutParameter( ParameterType p ) {
        return p.getInOut() == InOutType.OUT;
    }

    private static String defaultValue( ParameterType parameter ) {
        switch ( parameter.getType() ){
            case DOUBLE:
            case LONG:
                return "0";
            case DATE:
                return "NULL";
            case STRING:
                return "''";
        }
        throw new IllegalArgumentException();
    }
}

abstract class FunctionalObjectForOutParametres implements FuctionalObject{
    @Override
    public boolean filter( ParameterType p ) {
        return GeneroutGenerator.isOutParameter( p );
    }
}
