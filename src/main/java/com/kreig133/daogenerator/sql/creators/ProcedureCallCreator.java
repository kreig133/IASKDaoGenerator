package com.kreig133.daogenerator.sql.creators;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.jaxb.SelectType;
import com.kreig133.daogenerator.sql.test.TestValueByStringGenerator;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * @author eshangareev
 * @version 1.0
 */
public class ProcedureCallCreator extends CommonCallCreator{

    protected static String generateProcedureCall(
            @NotNull DaoMethod daoMethod,
            Function<ParameterType, String> function
    ) {
        StringBuilder builder      = new StringBuilder();

        builder.append( "{CALL " ).append( daoMethod.getCommon().getSpName() ).append( "(" );

        if( !daoMethod.getInputParametrs().getParameter().isEmpty() ){
            builder.append( "\n\t" );
        }

        builder.append( StringUtils.join(
                Iterators.transform( daoMethod.getInputParametrs().getParameter().iterator(), function ),
                ",\n\t"
        ));

        builder.append( "\n" );
        builder.append( ")}" );

        return builder.toString();
    }

    @Override
    public String generateExecuteQuery( @NotNull final DaoMethod daoMethod, final boolean forTest ) {
        return ProcedureCallCreator.generateProcedureCall(
                daoMethod,
                new Function<ParameterType, String>() {
                    @Override
                    public String apply( @Nullable ParameterType input ) {
                        assert input != null;
                        return transformParameterName(
                                input,
                                forTest ?
                                        TestValueByStringGenerator.newInstance( input ).getTestValue( input ) :
                                        getEscapedParamName( input, daoMethod.getSelectType() == SelectType.CALL )
                        );
                    }
                }
        );
    }
}
