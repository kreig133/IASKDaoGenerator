package com.kreig133.daogenerator.files.mybatis;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.parameter.Parameter;

import java.util.List;

import static com.kreig133.daogenerator.common.StringBufferUtils.insertTabs;

/**
 * @author eshangareev
 * @version 1.0
 */
public class XmlMappingGenerator {

    public static String generateXmlMapping(
        final OperationSettings operationSettings,
        final FunctionSettings functionSettings
    ){
        final List<Parameter> inputParameterList  = functionSettings.getInputParameterList();
        final List<Parameter> outputParameterList = functionSettings.getOutputParameterList();
        final String name                         = functionSettings.getName();
        final String package_                     = operationSettings.getEntityPackage();

        StringBuilder builder = new StringBuilder();
        insertTabs( builder, 1 ).append( "<" ) .append( functionSettings.getSelectType().getAnnotation()
                .toLowerCase() )
                .append( " id=\"" ).append( name ).append( "\" statementType=\"CALLABLE\"" );

        writeParameterType(  inputParameterList, name, "parameterType", "In" , package_, builder );
        writeParameterType( outputParameterList, name, "resultType"   , "Out", package_, builder );

        builder.append( ">\n\n" );

        builder.append( Utils.addTabsBeforeLine( functionSettings.getMyBatisQuery(), 2 ) ).append( "\n" );

        insertTabs(builder, 1).append( "</" ).append( functionSettings.getSelectType().getAnnotation().toLowerCase() )
                .append( ">\n\n" );

        return builder.toString();
    }

    private static void writeParameterType(
            List<Parameter> outputParameterList,
            String name,
            String type,
            String suffix,
            String package_,
            StringBuilder builder
    ) {
        if ( ! outputParameterList.isEmpty() ) {
            builder.append( "\n" );
            insertTabs( builder, 2 ).append( type ).append( "=\"" );
            if ( outputParameterList.size() > 1 ) {
                builder.append( package_ ).append( "." );
                builder.append( Utils.convertNameForClassNaming( name ) ).append( suffix );
            } else {
                builder.append( "java.lang." ).append( outputParameterList.get( 0 ).getType() );
            }
            builder.append( "\"" );
        }
    }


}
