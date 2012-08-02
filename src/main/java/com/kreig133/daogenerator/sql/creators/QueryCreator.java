package com.kreig133.daogenerator.sql.creators;

import com.kreig133.daogenerator.jaxb.*;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author kreig133
 * @version 1.0
 */
public abstract class QueryCreator {

    public abstract String generateExecuteQuery (
            DaoMethod daoMethod,
            boolean forTest
    );

    protected String getEscapedParamName( @NotNull ParameterType parameterType, boolean fullFormat ){
        return fullFormat ?
                String.format(
                        "#{%s, mode=%s, jdbcType=%s}",
                        parameterType.getRenameTo(),
                        parameterType.getInOut(),
                        parameterType.getJdbcType()
                ):
                String.format( "#{%s}", parameterType.getRenameTo() ) ;
    }

    public static String getQueryStringForTesting( @NotNull String query ) {
        return
                optimizeQuery(
                    query.replaceAll( daoGeneratorEscapedParamInfo.pattern(), "?" )
                );
    }

    @Language( "RegExp" )
    static String replace = "(?i)^\\s*(select\\s+(distinct\\s+)?+(?!top))";

    public static String optimizeQuery( String query ) {
        return query.replaceFirst( replace, "$1TOP 1 " );
    }

    protected static final Pattern daoGeneratorEscapedParamInfo = Pattern.compile( "\\$\\{(.+?)\\}");

    @NotNull
    public static List<String> getListOfParametrNames( String query ) {
        final List<String> names = new ArrayList<String>();

        Matcher matcher = getMatcher( query );

        while ( matcher.find() ) {
            names.add( matcher.group( 1 ).split( ";" )[ 0 ] );
        }

        return names;
    }

    protected static Matcher getMatcher( String query ) {
        return daoGeneratorEscapedParamInfo.matcher( query );
    }

    @NotNull
    public static List<ParameterType> extractInputParams( String query ) {
        Set<String> names = new HashSet<String>();
        Matcher matcher = getMatcher( query );

        List<ParameterType> result = new ArrayList<ParameterType>();

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
                result.add( parameterType );

            }
        }
        return result;
    }
}
