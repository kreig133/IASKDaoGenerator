package com.kreig133.daogenerator.sql.creators;

import com.kreig133.daogenerator.common.FunctionalObjectWithoutFilter;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;

import java.util.List;

import static com.kreig133.daogenerator.common.Utils.insertTabs;
import static com.kreig133.daogenerator.common.Utils.iterateForParameterList;

/**
 * @author eshangareev
 * @version 1.0
 */
public class GenerateGenerator extends CommonCallCreator{

    protected void declareParamInProcedure( StringBuilder builder, ParameterType p ) {
        declareInTypeParamInProcedure( builder, p );
    }

    protected void declareInTypeParamInProcedure( StringBuilder builder, ParameterType p ) {
        insertParameterName( builder, p );
        insertEscapedParamName( builder, p, false ); //TODO че за код ваще?
    }

    protected int declareParamInProcedure( StringBuilder builder, ParameterType p, int index) {
        switch ( p.getInOut() ){
            case IN:
                declareInTypeParamInProcedure( builder, p );
                return index;
            case OUT:
                declareOutTypeParamInProcedure( builder, p, index );
                return ++index;
        }
        throw new AssertionError();
    }

    protected static int declareParamInProcedureForTesting( StringBuilder builder, ParameterType p, int index ){
        switch (  p.getInOut() ) {
            case IN:
                insertParameterName( builder, p );
                builder.append( " ?" );
                return index;
            case OUT:
                declareOutTypeParamInProcedure( builder, p, index );
                return ++index;
        }
        throw new AssertionError();
    }

    protected static int parameterName( StringBuilder builder, int index ){
        builder.append( "@P" ).append( index ).append( " " );

        return ++index;
    }

    private static void declareOutTypeParamInProcedure( StringBuilder builder, ParameterType p, int index ) {
        insertParameterName( builder, p );
        parameterName( builder, index );
        builder.append( " output" );
    }



    @Override
    public String generateExecuteQuery( DaoMethod daoMethod, boolean forTest ) {
        final List<ParameterType> outputParametrs  = daoMethod.getOutputParametrs().getParameter();
        final List<ParameterType> inputParametrs   = daoMethod.getInputParametrs ().getParameter();


        StringBuilder builder      = new StringBuilder();

        builder.append( "create table #TempTableForNamedResultSet(\n" );

        iterateForParameterList( builder, outputParametrs, new FunctionalObjectWithoutFilter() {
            @Override
            public void writeString( StringBuilder builder, ParameterType p ) {
                builder.append( p.getName() ).append( " " ).append( p.getSqlType() ).append( " " ).append( "NULL" );
            }
        } );

        builder.append( ");\n" );
        builder.append( "insert into #TempTableForNamedResultSet\n" );
        insertTabs( builder, 1 ).append( "exec " ).append(  daoMethod.getCommon().getSpName() ).append( "\n" );

        if( !forTest ){
            iterateForParameterList( builder, inputParametrs, 2, new FunctionalObjectWithoutFilter() {
                @Override
                public void writeString( StringBuilder builder, ParameterType p ) {
                    declareInTypeParamInProcedure( builder, p );
                }
            } );
        } else {
            iterateForParameterList( builder, inputParametrs, 2, new FunctionalObjectWithoutFilter() {
                @Override
                public void writeString( StringBuilder builder, ParameterType p ) {
                    builder.append( " ?" );
                }
            } );
        }

        builder.append( ";\n" );
        builder.append( "SELECT * FROM #TempTableForNamedResultSet" );

        return builder.toString();
    }
}
