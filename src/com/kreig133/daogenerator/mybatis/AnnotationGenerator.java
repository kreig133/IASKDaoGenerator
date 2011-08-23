package com.kreig133.daogenerator.mybatis;

import com.kreig133.daogenerator.Settings;
import com.kreig133.daogenerator.Utils;
import com.kreig133.daogenerator.enums.SelectType;
import com.kreig133.daogenerator.parametr.Parameter;

import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class AnnotationGenerator {
    public static String generateAnnotation(
        Settings settings
    ){
        SelectType selectType               = settings.getSelectType();
        List<Parameter> inputParameterList  = settings.getInputParameterList();
        List<Parameter> outputParameterList = settings.getOutputParameterList();
        String selectQuery                  = settings.getSelectQuery().toString();
        String name                         = settings.getFunctionName();


        StringBuilder builder = new StringBuilder();
        builder.append( "    @Select(\n" );

        switch ( selectType ){

            case CALL:
                builder.append(
                        Utils.wrapWithQuotes( XmlMappingGenerator.generateProcedureCall( inputParameterList, name ) )
                );
                break;

            case GENERATE:
                builder.append(
                    Utils.wrapWithQuotes( WrapperGenerator.generateWrapperProcedure( settings ) ) );
                break;

            case SELECT:
                builder.append(
                    Utils.wrapWithQuotes( processSelectQueryString( selectQuery, inputParameterList  ) )
                );
                break;
        }

        builder.append( "    )\n" );

        return builder.toString();
    }

    private static String processSelectQueryString( final String sqlQuery, final List<Parameter> inputParameters ){

        String query = sqlQuery;

        StringBuilder result = null;

        final String[] split = query.split( "\\?" );
        if( split.length > 1 ){

            result = new StringBuilder();

            int index = 0;

            for( int i = 0; i < split.length - 1; i++ ){
                String string = split[i];
                result.append( string );
                result.append( "${" );
                result.append( inputParameters.get( index ).getName() );
                result.append( "}" );
                index ++ ;
            }
        }

        if( sqlQuery.split( ":" ).length > 1 ){
            if( result != null ){
                query = result.toString();
            }

            result = new StringBuilder();

            int index = 0;
            
            for( String string: sqlQuery.split( ":" )){
                if( index == 0 ){
                    result.append( string );
                    index ++ ;
                } else {
                    String[] aftefSplit = string.split( "[ =;,\\)\\n\\t\\r\\*\\-\\+/<>]" );
                    result.append( aftefSplit[0] );
                    result.append( "} " );
                    if( aftefSplit.length > 1 ) {
                        result.append( string.substring( aftefSplit[0].length() + 1 ) );
                    }
                }
                result.append( "${" );
            }
        }

        return result == null? sqlQuery : result.toString();
    }
}
