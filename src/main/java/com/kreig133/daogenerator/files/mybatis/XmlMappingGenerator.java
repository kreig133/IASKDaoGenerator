package com.kreig133.daogenerator.files.mybatis;

import com.kreig133.daogenerator.DaoGenerator;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.sql.SqlQueryCreator;

import java.util.List;

import static com.kreig133.daogenerator.common.StringBuilderUtils.insertTabs;

/**
 * @author eshangareev
 * @version 1.0
 */
public class XmlMappingGenerator {

    public static String generateXmlMapping(
        final DaoMethod daoMethod
    ){
        final List<ParameterType> inputParameterList  = daoMethod.getInputParametrs ().getParameter();
        final List<ParameterType> outputParameterList = daoMethod.getOutputParametrs().getParameter();
        final String name                         = daoMethod.getCommon().getMethodName();
        final String package_                     = DaoGenerator.getCurrentOperationSettings().getEntityPackage();

        StringBuilder builder = new StringBuilder();
        insertTabs( builder, 1 ).append( "<" ) .append( daoMethod.getCommon().getConfiguration().getType().getAnnotation()
                .toLowerCase() )
                .append( " id=\"" ).append( name ).append( "\" statementType=\"CALLABLE\"" );

        writeParameterType(  inputParameterList, name, "parameterType", "In" , package_, builder );
        writeParameterType( outputParameterList, name, "resultType"   , "Out", package_, builder );

        builder.append( ">\n\n" );
        builder.append( Utils.addTabsBeforeLine( SqlQueryCreator.createQueries( daoMethod, false ), 2 ) ).append( "\n" );

        insertTabs(builder, 1).append( "</" ).append( daoMethod.getCommon().getConfiguration().getType()
                .getAnnotation().toLowerCase() ).append( ">\n\n" );

        return builder.toString();
    }

    private static void writeParameterType(
            List<ParameterType> outputParameterList,
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
