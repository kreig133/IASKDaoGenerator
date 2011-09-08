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
        
        builder.append( "    @" ).append( selectType.getAnnotation() ).append( "(\n" );

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

        StringBuilder builder = null;

        String[] splitted = sqlQuery.split( "\\?" );
        if( splitted.length > 1 ){

            builder = new StringBuilder();

            int index = 0;

            for( int i = 0; i < splitted.length - 1; i++ ){
                builder.append( splitted[i] ).append( "#{" );
                builder.append( inputParameters.get( index ).getName() ).append( "}" );
                index ++ ;
            }
            builder.append( splitted[ splitted.length - 1 ] );
        }

        splitted = sqlQuery.split( ":" );
        if( splitted.length > 1 ){

            builder = new StringBuilder();

            for( int i = 0; i < splitted.length ; i++ ){
                if( i == 0 ){
                    builder.append( splitted[ 0 ] );
                } else {
                    builder.append( "#{" );
                    String[] aftefSplit = splitted[i].split( "[ =;,\\)\\n\\t\\r\\*\\-\\+/<>]" );
                    builder.append( aftefSplit[ 0 ] ).append( "} " );
                    builder.append( splitted[ i ].substring( aftefSplit[ 0 ].length() + 1 ) );
                }
            }
        }

        return builder == null? sqlQuery : builder.toString();
    }
}
