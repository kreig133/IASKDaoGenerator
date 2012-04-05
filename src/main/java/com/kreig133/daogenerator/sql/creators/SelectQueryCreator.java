package com.kreig133.daogenerator.sql.creators;

import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.sql.test.TestValueByStringGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class SelectQueryCreator extends QueryCreator{
    @Override
    public String generateExecuteQuery( @NotNull DaoMethod daoMethod, boolean forTest ) {

        String sqlQuery = daoMethod.getCommon().getQuery();

        StringBuilder builder = null;

        String[] splitted = sqlQuery.split( "\\$\\{.+?\\}" );

        if( splitted.length > 1 ){

            builder = new StringBuilder();

            final List<String> listOfParametrNames =
                    getListOfParametrNames( daoMethod.getCommon().getQuery() );

            for( int i = 0; i < splitted.length - 1; i++ ){
                builder.append( splitted[i] );

                ParameterType parameter = daoMethod.getInputParametrs().getParameterByName( listOfParametrNames.get( i ) );

                assert parameter != null;
                builder.append( forTest ?
                        TestValueByStringGenerator.newInstance( parameter ).getTestValue( parameter ) :
                        getEscapedParamName( parameter, false )
                );
            }
            builder.append( splitted[ splitted.length - 1 ] );
        }

        return builder == null ? sqlQuery : builder.toString();
    }


}
