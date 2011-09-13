package com.kreig133.daogenerator.sql;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.parameter.Parameter;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class SelectQueryConverter {

    public static void processSelectQueryString(
            final FunctionSettings functionSettings
    ){

        String sqlQuery = functionSettings.getSelectQuery().toString();
        StringBuilder myBatisString = null;
        StringBuilder queryForTesting = null;

        String[] splitted = sqlQuery.split( "\\?" );
        if( splitted.length > 1 ){

            myBatisString = new StringBuilder();

            int index = 0;

            for( int i = 0; i < splitted.length - 1; i++ ){
                myBatisString.append( splitted[i] ).append( "#{" );
                myBatisString.append( functionSettings.getInputParameterList().get( index ).getName() ).append( "}" );
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
                    myBatisString  .append( "#{" );

                    String[] aftefSplit = splitted[i].split( "[ =;,\\)\\n\\t\\r\\*\\-\\+/<>]" );
                    myBatisString.append( aftefSplit[ 0 ] ).append( "} " );

                    final String stringAfterParamName = splitted[ i ].substring( aftefSplit[ 0 ].length() + 1 );
                    queryForTesting.append( stringAfterParamName );
                    myBatisString  .append( stringAfterParamName );
                }
            }
        }

        functionSettings.setMyBatisQuery(
                myBatisString == null ? sqlQuery : myBatisString.toString()
        );

        functionSettings.setQueryForTesting(
                queryForTesting == null? sqlQuery : queryForTesting.toString()
        );
    }
}
