package com.kreig133.daogenerator.sql;

import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;

import java.util.List;

import static com.kreig133.daogenerator.common.StringBuilderUtils.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class SelectQueryConverter {

    //TODO rename
    public static String getSelectQueryString(
            final DaoMethod daoMethod,
            boolean forTest
    ){

        String sqlQuery = daoMethod.getCommon().getQuery();

        StringBuilder builder = null;

        String[] splitted = sqlQuery.split( "\\$\\{.+?\\}" );
        if( splitted.length > 1 ){

            builder = new StringBuilder();

            int index = 0;

            final List<String> listOfParametrNames =
                    SqlQueryParser.getListOfParametrNames( daoMethod.getCommon().getQuery() );

            for( int i = 0; i < splitted.length - 1; i++ ){
                builder.append( splitted[i] );

                ParameterType parameterType = null;

                for ( ParameterType type : daoMethod.getInputParametrs().getParameter() ) {
                        if ( type.getName().equals( listOfParametrNames.get( i ) ) ) {
                            parameterType = type;
                            break;
                        }
                }

                if ( ! forTest ){
                    insertEscapedParamName( builder,
                            parameterType, false );
                } else {
                    builder.append( parameterType.getType().testValueGenerator().getTestValue( parameterType ) );
                }
                index++;
            }
            builder.append( splitted[ splitted.length - 1 ] );
        }

        return builder == null ? sqlQuery : builder.toString();
    }
}
