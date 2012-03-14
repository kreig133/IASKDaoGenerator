package com.kreig133.daogenerator.sql;

import com.kreig133.daogenerator.jaxb.DaoMethod;

import static com.kreig133.daogenerator.common.StringBuilderUtils.*;
import static com.kreig133.daogenerator.sql.SqlQueryParser.getQueryStringWithoutMetaData;

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

        String sqlQuery = getQueryStringWithoutMetaData( daoMethod.getCommon().getQuery() );

        StringBuilder builder = null;

        String[] splitted = sqlQuery.split( "\\?" );
        if( splitted.length > 1 ){

            builder = new StringBuilder();

            int index = 0;

            for( int i = 0; i < splitted.length - 1; i++ ){
                builder.append( splitted[i] );
                if ( ! forTest ){
                    insertEscapedParamName( builder,
                            daoMethod.getInputParametrs().getParameter().get( index ) );
                } else {
                    builder.append(
                            SqlUtils.getTestValue( daoMethod.getInputParametrs().getParameter().get( index ) ) );
                }
                index++;
            }
            builder.append( splitted[ splitted.length - 1 ] );
        }

        return builder == null ? sqlQuery : builder.toString();
    }
}
