package com.kreig133.daogenerator.sql;

import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.JavaType;
import com.kreig133.daogenerator.jaxb.ParameterType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author eshangareev
 * @version 1.0
 */
public class SqlQueryParser {

    private static final Pattern pattern = Pattern.compile( "\\$\\{(.+?)\\}");

    public static DaoMethod parseSqlQueryAndParameters( DaoMethod daoMethod ) {
        final String query = daoMethod.getCommon().getQuery();

        Matcher matcher = getMatcher( query );


        while( matcher.find() ){

            final String group = matcher.group( 1 );
            final String[] split = group.split( ";" );
            final ParameterType parameterType = new ParameterType();

            parameterType.setName( split[ 0 ] );
            parameterType.setRenameTo( split[ 0 ] );
            parameterType.setSqlType( split[ 1 ] );
            parameterType.setType( JavaType.getBySqlType( parameterType.getSqlType() ) );
            parameterType.setTestValue( split[ 2 ] );
            if( split.length ==  4 ){
                parameterType.setComment( split[ 3 ] );
            }

            daoMethod.getInputParametrs().getParameter().add( parameterType );
        }

        daoMethod.getCommon().setQuery( query.replaceAll( pattern.pattern(), "?" ) );

        return daoMethod;
    }

    private static Matcher getMatcher( String query ) {
        return pattern.matcher( query );
    }
}
