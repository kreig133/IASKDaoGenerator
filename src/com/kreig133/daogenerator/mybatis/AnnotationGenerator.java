package com.kreig133.daogenerator.mybatis;

import com.kreig133.daogenerator.common.Settings;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.enums.SelectType;
import com.kreig133.daogenerator.parameter.Parameter;
import com.kreig133.daogenerator.mybatis.wrappers.WrapperGenerators;

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
        String selectQuery                  = settings.getSelectQuery().toString();
        String name                         = settings.getFunctionName();


        StringBuilder builder = new StringBuilder();

        assert selectType != null ;
        
        builder.append( "    @" );
        builder.append( selectType.getAnnotation() );
        builder.append( "(\n" );

        switch ( selectType ){

            case CALL:
                builder.append(
                    Utils.wrapWithQuotes( XmlMappingGenerator.generateProcedureCall( inputParameterList, name ) )
                );
                break;

            case GENERATE:
            case GENEROUT:
                builder.append(
                    Utils.wrapWithQuotes( WrapperGenerators.generateWrapperProcedure( settings ) ) );
                break;

            default:
                builder.append(
                    Utils.wrapWithQuotes( processSelectQueryString( selectQuery, inputParameterList ) )
                );
                break;
        }

        builder.append( "    )\n" );

        return builder.toString();
    }

    private static String processSelectQueryString( final String sqlQuery, final List<Parameter> inputParameters ){

        StringBuilder result = null;

        String[] splitted = sqlQuery.split( "\\?" );
        if( splitted.length > 1 ){

            result = new StringBuilder();

            int index = 0;

            for( int i = 0; i < splitted.length - 1; i++ ){
                result.append( splitted[i] );
                result.append( "#{" );
                result.append( inputParameters.get( index ).getName() );
                result.append( "}" );
                index ++ ;
            }
            result.append( splitted[splitted.length - 1 ] );
        }

        splitted = sqlQuery.split( ":" );
        if( splitted.length > 1 ){

            result = new StringBuilder();

            for( int i = 0; i < splitted.length ; i++ ){
                if( i == 0 ){
                    result.append( splitted[0] );
                } else {
                    result.append( "#{" );
                    String[] aftefSplit = splitted[i].split( "[ =;,\\)\\n\\t\\r\\*\\-\\+/<>]" );
                    result.append( aftefSplit[0] );
                    result.append( "} " );
                    result.append( splitted[i].substring( aftefSplit[0].length() + 1 ) );
                }
            }
        }

        return result == null? sqlQuery : result.toString();
    }
}
