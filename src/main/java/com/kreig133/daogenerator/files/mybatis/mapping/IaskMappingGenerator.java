package com.kreig133.daogenerator.files.mybatis.mapping;

import com.kreig133.daogenerator.jaxb.NamingUtils;
import com.kreig133.daogenerator.files.mybatis.intrface.InterfaceGenerator;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.settings.Settings;
import com.kreig133.daogenerator.sql.creators.QueryCreatorFabric;

import java.util.List;

/**
 * @author kreig133
 * @version 1.0
 */
public class IaskMappingGenerator extends MappingGenerator{

    @Override
    public void generateBody( DaoMethod daoMethod ) {
        builder.append( generateXmlMapping( daoMethod ) );
    }

    @Override
    public String getFileName() {
        return Settings.settings().getOperationName();
    }

//    @Override
//    public void generateFoot() throws IOException {
//        builder.append( "</mapper>" );
//    }

    @Override
    protected String getFileNameEnding() {
        return ".map.xml";
    }

    @Override
    public void generateHead() {
        builder.append( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" );
        builder.append( "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis" +
                ".org/dtd/mybatis-3-mapper.dtd\">\n" );
        builder.append( "<mapper namespace=\"" ).append( Settings.settings().getDaoPackage() )
                .append( "." ).append( InterfaceGenerator.instance().getFileName() ).append( "\">\n" );
    }

    private String generateXmlMapping(
            final DaoMethod daoMethod
    ){
        final List<ParameterType> inputParameterList  = daoMethod.getInputParametrs ().getParameter();
        final List<ParameterType> outputParameterList = daoMethod.getOutputParametrs().getParameter();
        final String name                         = daoMethod.getCommon().getMethodName();
        final String package_                     = Settings.settings().getEntityPackage();

        StringBuilder builder = new StringBuilder();
        insertTabs().append( "<" ) .append( daoMethod.getSelectType().annotation().toLowerCase() )
                .append( " id=\"" ).append( name ).append( "\" statementType=\"CALLABLE\"" );

        writeParameterType(  inputParameterList, name, "parameterType", "In" , package_, builder );
        writeParameterType( outputParameterList, name, "resultType"   , "Out", package_, builder );

        builder.append( ">\n\n" );
        increaseNestingLevel();
        insertTabs().append(
                QueryCreatorFabric.newInstance( daoMethod ).generateExecuteQuery( daoMethod, false )
        );
        insertLine();
        decreaseNestingLevel();

        insertTabs().append( "</" ).append( daoMethod.getSelectType().annotation().toLowerCase() ).append( ">" );
        insertLine();
        insertLine();

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
            insertLine();
            increaseNestingLevel();
            insertTabs().append( type ).append( "=\"" );
            if ( outputParameterList.size() > 1 ) {
                builder.append( package_ ).append( "." );
                builder.append( NamingUtils.convertNameForClassNaming( name ) ).append( suffix );
            } else {
                builder.append( "java.lang." ).append( outputParameterList.get( 0 ).getType().value() );
            }
            builder.append( "\"" );
            decreaseNestingLevel();
        }
    }
}
