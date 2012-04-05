package com.kreig133.daogenerator.sql.creators;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.sql.test.TestValueByStringGenerator;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

import static com.kreig133.daogenerator.common.Utils.insertTabs;

/**
 * @author eshangareev
 * @version 1.0
 */
public class WrapperGenerator extends CommonCallCreator{

    @Override
    public String generateExecuteQuery( @NotNull DaoMethod daoMethod, final boolean forTest ) {

        final List<ParameterType> outputParametrs  = daoMethod.getOutputParametrs().getParameter();
        final List<ParameterType> inputParametrs   = daoMethod.getInputParametrs ().getParameter();

        StringBuilder builder      = new StringBuilder();

        builder.append( "create table #TempTableForNamedResultSet(\n" );

        insertTabs( builder, 1 ).append( StringUtils.join(
                Iterators.transform( outputParametrs.iterator(),
                        new Function<ParameterType, String>() {
                            @Override
                            public String apply( @Nullable ParameterType p ) {
                                assert p != null;
                                return String.format( "%s %s NULL", p.getName(), p.getSqlType() );
                            }
                        }
                ), ",\n\t"
        ) );

        builder.append( "\n);\n" );
        builder.append( "insert into #TempTableForNamedResultSet\n" );
        insertTabs( builder, 1 ).append( "exec " ).append(  daoMethod.getCommon().getSpName() ).append( "\n" );

        insertTabs( builder, 2 ).append( StringUtils.join(
                Iterators.transform( inputParametrs.iterator(),
                        new Function<ParameterType, String>() {
                            @Override
                            public String apply( @Nullable ParameterType parameter ) {
                                assert parameter != null;
                                return transformParameterName( parameter,
                                        forTest ?
                                                TestValueByStringGenerator.newInstance( parameter )
                                                        .getTestValue( parameter ) :
                                                getEscapedParamName( parameter, true )
                                );
                            }
                        }
                ), ",\n\t\t"
        ) );

        builder.append( ";\n" );
        builder.append( "SELECT * FROM #TempTableForNamedResultSet" );

        return builder.toString();
    }
}
