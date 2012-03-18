package com.kreig133.daogenerator.files.mybatis.mapping;

import com.kreig133.daogenerator.DaoGenerator;
import com.kreig133.daogenerator.files.mybatis.intrface.InterfaceGenerator;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.sql.creators.QueryCreator;

import java.io.IOException;
import java.util.List;

import static com.kreig133.daogenerator.common.Utils.insertTabs;

/**
 * @author kreig133
 * @version 1.0
 */
public class IaskMappingGenerator extends MappingGenerator{

    @Override
    public void generateBody( DaoMethod daoMethod ) throws IOException {
        builder.append( generateXmlMapping( daoMethod ) );
    }

    @Override
    public void generateFoot() throws IOException {
        builder.append( "</mapper>" );
    }

    @Override
    protected String getFileNameEnding() {
        return ".map.xml";
    }

    @Override
    public void generateHead() throws IOException {
        builder.append( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" );
        builder.append( "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis" +
                ".org/dtd/mybatis-3-mapper.dtd\">\n" );
        builder.append( "<mapper namespace=\"" ).append( DaoGenerator.settings().getDaoPackage() )
                .append( "." ).append( InterfaceGenerator.interfaceFileName() ).append( "\">\n" );
    }

    private String generateXmlMapping(
            final DaoMethod daoMethod
    ){
        final List<ParameterType> inputParameterList  = daoMethod.getInputParametrs ().getParameter();
        final List<ParameterType> outputParameterList = daoMethod.getOutputParametrs().getParameter();
        final String name                         = daoMethod.getCommon().getMethodName();
        final String package_                     = DaoGenerator.settings().getEntityPackage();

        StringBuilder builder = new StringBuilder();
        insertTabs( builder, 1 ).append( "<" ) .append( daoMethod.getSelectType().getAnnotation()
                .toLowerCase() )
                .append( " id=\"" ).append( name ).append( "\" statementType=\"CALLABLE\"" );

        writeParameterType(  inputParameterList, name, "parameterType", "In" , package_, builder );
        writeParameterType( outputParameterList, name, "resultType"   , "Out", package_, builder );

        builder.append( ">\n\n" );
        insertTabs( builder, 2 ).append(
                QueryCreator.newInstance( daoMethod ).generateExecuteQuery( daoMethod, false )
        ).append( "\n" );

        insertTabs(builder, 1).append( "</" ).append( daoMethod.getSelectType().getAnnotation().toLowerCase() ).append( ">\n\n" );

        return builder.toString();
    }

    private void writeParameterType(
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
                builder.append( convertNameForClassNaming( name ) ).append( suffix );
            } else {
                builder.append( "java.lang." ).append( outputParameterList.get( 0 ).getType().value() );
            }
            builder.append( "\"" );
        }
    }
}
