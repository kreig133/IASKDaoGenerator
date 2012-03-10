package com.kreig133.daogenerator.sql;

import com.kreig133.daogenerator.jaxb.DaoMethod;

import static com.kreig133.daogenerator.common.StringBuilderUtils.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class SelectQueryConverter {

    //TODO rename
    public static String processSelectQueryString(
            final DaoMethod daoMethod
    ){

        String sqlQuery = daoMethod.getCommon().getQuery();

        StringBuilder myBatisString = null;
        StringBuilder queryForTesting = null;

        String[] splitted = sqlQuery.split( "\\?" );
        if( splitted.length > 1 ){

            myBatisString = new StringBuilder();

            int index = 0;

            for( int i = 0; i < splitted.length - 1; i++ ){
                myBatisString.append( splitted[i] );
                insertEscapedParamName( myBatisString,
                        daoMethod.getInputParametrs().getParameter().get( index ).getName() );
                index ++ ;
            }
            myBatisString.append( splitted[ splitted.length - 1 ] );
        }

        splitted = sqlQuery.split( ":" );
        if( splitted.length > 1 ){

            myBatisString   = new StringBuilder();
            queryForTesting = new StringBuilder(  );

            for( int i = 0; i < splitted.length ; i++ ){
                if( i == 0 ){
                    myBatisString   .append( splitted[ 0 ] );
                    queryForTesting .append( splitted[ 0 ] );
                } else {
                    queryForTesting.append( "?" );

                    String[] aftefSplit = splitted[i].split( "[ =;,\\)\\n\\t\\r\\*\\-\\+/<>]" );

                    insertEscapedParamName( myBatisString, aftefSplit[ 0 ] );

                    final String stringAfterParamName = splitted[ i ].substring( aftefSplit[ 0 ].length() );
                    queryForTesting.append( stringAfterParamName );
                    myBatisString  .append( stringAfterParamName );
                }
            }
        }


        return myBatisString == null ? sqlQuery : myBatisString.toString();
    }
}
