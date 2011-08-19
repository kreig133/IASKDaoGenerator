package com.kreig133.daogenerator.mybatis;

import com.kreig133.daogenerator.Settings;
import com.kreig133.daogenerator.Utils;
import com.kreig133.daogenerator.enums.SelectType;
import com.kreig133.daogenerator.enums.Type;
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
        String name                         = settings.getName();


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
                    Utils.wrapWithQuotes( selectQuery )
                );
                break;
        }

        builder.append( "    )\n" );

        return builder.toString();
    }
}
