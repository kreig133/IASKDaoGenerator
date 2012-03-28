package com.kreig133.daogenerator.sql;

import com.kreig133.daogenerator.jaxb.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author eshangareev
 * @version 1.0
 */
public class SqlQueryParser {

    private static SqlQueryParser INSTANCE;

    public static SqlQueryParser instance(){
        if ( INSTANCE == null ) {
            INSTANCE = new SqlQueryParser();
        }
        return INSTANCE;
    }

    public static final String NULL = "null";

    private final Pattern pattern = Pattern.compile( "\\$\\{(.+?)\\}");

    public DaoMethod parseSqlQueryForParameters( DaoMethod daoMethod ) {
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

    private Matcher getMatcher( String query ) {
        return pattern.matcher( query );
    }
    
    public String getQueryStringWithoutMetaData( String query ){
        return query.replaceAll( pattern.pattern(), "?" ) ;
    }

    public List<String> getListOfParametrNames( String query ) {
        final ArrayList<String> names = new ArrayList<String>();

        Matcher matcher = getMatcher( query );

        while ( matcher.find() ) {
            names.add( matcher.group( 1 ).split( ";" )[ 0 ] );
        }

        return names;
    }

    public SelectType determineQueryType( String query ) {
        if ( ! stringContainsMoreThanOneWord( query ) ) {
            return SelectType.CALL;
        }

        final String firstWord = query.trim().split( "\\s" )[0];

        return SelectType.getByName( firstWord );
    }

    private boolean stringContainsMoreThanOneWord( String query ) {
        return ( query.split( "\\s+" ).length > 1 );
    }

    public boolean checkSPName( String query ) {
        if( determineQueryType( query ) != SelectType.CALL ) {
            throw new IllegalArgumentException();
        }

        return ! ( getStoreProcedureName( query ) == null || "".equals( getStoreProcedureName( query ) ) );
    }


    public void fillTestValuesByInsertedQuery( List<ParameterType> inputParametrs, String query ) {
        if( ! stringContainsMoreThanOneWord( query ) ) return;

        for ( ParameterType inputParametr : inputParametrs ) {
            inputParametr.setTestValue( getParameterValueFromQuery( inputParametr, query ) );
        }
    }
    
    private static Pattern getPatternForExtractTestValue( String parameterName ) {
        return Pattern.compile( String.format( "(?isu)@%s\\s*[^@]*?=\\s*null\\b", parameterName) );
    }
    
    Pattern storeNamePattern = Pattern.compile( "(?iu)execute\\s+(\\w+)" );
    
    public String getStoreProcedureName( String query ) {
        final String s = query.replaceAll( "(?i)dbo\\.", "" ).trim();
        
        if( stringContainsMoreThanOneWord( s ) ){
            final Matcher matcher = storeNamePattern.matcher( s );
            return matcher.find() ? matcher.group( 1 ) : null;
        }
        
        return s;
    }



    private String getDefaultValueForQuotedFromSpDefinition( ParameterType type, String query ) {
        return getDefaultValue( type, query,
                getStringPatternForParameter( type.getName() ).matcher( query ) );
    }

    private String getDefaultValueForNumberFromSpDefinition( ParameterType type, String query ) {
        return getDefaultValue( type, query,
                getNumberPatternForParameter( type.getName() ).matcher( query ) );
    }

    private String getDefaultValue( ParameterType type, String query, Matcher matcher  ){
        if ( getNullPatternForParameter( type.getName() ).matcher( query ).find() ){
            return NULL;
        }
        if( matcher.find() ){
            return matcher.group( 1 );
        } else {
            return "";
        }
    }

    private Pattern getStringPatternForParameter( String name ) {
        return Pattern.compile( String.format( "(?isu)@%s\\s*[^@]*?=\\s*['\"](.*?)['\"]", name ) );
    }

    private Pattern getNullPatternForParameter( String parameterName ) {
        return Pattern.compile( String.format( "(?isu)@%s\\s*[^@]*?=\\s*null\\b", parameterName) );
    }

    private Pattern getNumberPatternForParameter( String parameterName ) {
        return Pattern.compile( String.format( "(?isu)@%s\\s*[^@]*?=\\s*([-\\d\\.]+)\\b", parameterName ) );
    }

    public String getParameterValueFromQuery( ParameterType type, String query ) {
        switch ( type.getType() ){
            case LONG:
            case BYTE:
            case DOUBLE:
                return getDefaultValueForNumberFromSpDefinition( type, query );
            default:
                return getDefaultValueForQuotedFromSpDefinition( type, query );
        }


    }
}
