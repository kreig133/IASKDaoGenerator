package com.kreig133.daogenerator.sql.creators;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.kreig133.daogenerator.common.FunctionalObjectWithoutFilter;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.sql.test.TestValueByStringGenerator;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.List;

import static com.kreig133.daogenerator.common.Utils.insertTabs;
import static com.kreig133.daogenerator.common.Utils.iterateForParameterList;

/**
 * @author eshangareev
 * @version 1.0
 */
public class GenerateGenerator extends CommonCallCreator{

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
    public String generateExecuteQuery( DaoMethod daoMethod, final boolean forTest ) {

        final List<ParameterType> outputParametrs  = daoMethod.getOutputParametrs().getParameter();
        final List<ParameterType> inputParametrs   = daoMethod.getInputParametrs ().getParameter();

        StringBuilder builder      = new StringBuilder();

        builder.append( "create table #TempTableForNamedResultSet(\n" );

        insertTabs( builder, 1 ).append( StringUtils.join(
                Iterators.transform( outputParametrs.iterator(),
                        new Function<ParameterType, String>() {
                            @Override
                            public String apply( @Nullable ParameterType p ) {
                                return String.format( "%s %s NULL", p.getName(), p.getSqlType() );
                            }
                        }
                ), ",\n\t"
        ) );

        builder.append( "\n);\n" );
        builder.append( "insert into #TempTableForNamedResultSet\n" );
        insertTabs( builder, 1 ).append( "exec " ).append(  daoMethod.getCommon().getSpName() ).append( "\n" );

        insertTabs( builder, 1 ).append( StringUtils.join(
                Iterators.transform( inputParametrs.iterator(),
                        new Function<ParameterType, String>() {
                            @Override
                            public String apply( @Nullable ParameterType parameter ) {
                                return transformParameterName( parameter,
                                        forTest ?
                                                TestValueByStringGenerator.newInstance( parameter )
                                                        .getTestValue( parameter ) :
                                                getEscapedParamName( parameter, false )
                                );
                            }
                        }
                ), ",\n\t"
        ) );

        builder.append( ";\n" );
        builder.append( "SELECT * FROM #TempTableForNamedResultSet" );

        return builder.toString();
    }
}
