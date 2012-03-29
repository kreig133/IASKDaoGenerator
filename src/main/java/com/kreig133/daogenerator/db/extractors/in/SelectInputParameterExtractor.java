package com.kreig133.daogenerator.db.extractors.in;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.jaxb.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author eshangareev
 * @version 1.0
 */
public class SelectInputParameterExtractor extends QueryInputParameterExtractor{

    private static SelectInputParameterExtractor INSTANCE;

    static SelectInputParameterExtractor instance(){
        if ( INSTANCE == null ) {
            INSTANCE = new SelectInputParameterExtractor();
        }
        return INSTANCE;
    }

    @Override
    public DaoMethod extractInputParams( DaoMethod daoMethod ) {
        final String query = daoMethod.getCommon().getQuery();

        Matcher matcher = getMatcher( query );

        daoMethod.getInputParametrs().getParameter().clear();

        Set<String> names = new HashSet<String>();

        while( matcher.find() ){
            final String group = matcher.group( 1 );
            final String[] split = group.split( ";" );

            if( ! names.contains( split[ 0 ] ) ){

                names.add( split[0] );

                final ParameterType parameterType = new ParameterType();

                parameterType.setName( split[ 0 ] );
                parameterType.setRenameTo( split[ 0 ] );
                parameterType.setSqlType( split[ 1 ] );
                parameterType.setType( JavaType.getBySqlType( parameterType.getSqlType() ) );
                if ( split.length > 2 ) {
                    parameterType.setTestValue( split[ 2 ] );
                }
                if( split.length ==  4 ){
                    parameterType.setComment( split[ 3 ] );
                }

                parameterType.setInOut( InOutType.IN );

                daoMethod.getInputParametrs().getParameter().add( parameterType );
            }
        }
        return daoMethod;
    }

    @Override
    public DaoMethod fillTestValuesByInsertedQuery( DaoMethod daoMethod ) {
        return daoMethod;
    }
}
