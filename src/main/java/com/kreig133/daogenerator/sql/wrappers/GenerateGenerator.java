package com.kreig133.daogenerator.sql.wrappers;

import com.kreig133.daogenerator.common.strategy.FunctionalObjectWithoutFilter;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;

import java.util.List;

import static com.kreig133.daogenerator.common.Utils.insertTabs;
import static com.kreig133.daogenerator.common.StringBuilderUtils.iterateForParameterList;

/**
 * @author eshangareev
 * @version 1.0
 */
public class GenerateGenerator extends CommonWrapperGenerator{

    public static String generateWrapper( DaoMethod daoMethod ) {
        return generateWrapper( daoMethod, false );
    }

    public static String generateWrapper( DaoMethod daoMethod, boolean forTests ) {
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

        if( !forTests ){
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
