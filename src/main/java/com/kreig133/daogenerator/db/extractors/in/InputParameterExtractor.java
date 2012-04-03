package com.kreig133.daogenerator.db.extractors.in;

import com.kreig133.daogenerator.db.extractors.Extractor;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author eshangareev
 * @version 1.0
 */
abstract public class InputParameterExtractor extends Extractor{

    public static final String NULL = "null";
    public static final String NOT_FOUNDED = "~`12!@";

    public abstract DaoMethod extractInputParams( DaoMethod daoMethod );

    public abstract DaoMethod fillTestValuesByInsertedQuery( DaoMethod daoMethod );

    public abstract DaoMethod fillMethodName( DaoMethod daoMethod );

    public static InputParameterExtractor getInstance( DaoMethod daoMethod ){
        switch ( daoMethod.getSelectType() ){
            case CALL:
                return SpInputParameterExtractor.instance();
            default:
                return SelectInputParameterExtractor.instance();
        }
    }

    public DaoMethod extractAndFillInputParams( DaoMethod daoMethod ){
        return fillTestValuesByInsertedQuery( extractInputParams( daoMethod ) );
    }

    protected String getParameterValueFromQuery( ParameterType type, String query ) {
        switch ( type.getType() ){
            case LONG:
            case BYTE:
            case DOUBLE:
                return getDefaultValueForNumberFromSpDefinition( type, query );
            default:
                return getDefaultValueForQuotedFromSpDefinition( type, query );
        }
    }

    private static Pattern getPatternForExtractTestValue( String parameterName ) {
        return Pattern.compile( String.format( "(?isu)@%s\\s*[^@]*?=\\s*null\\b", parameterName) );
    }

    private String getDefaultValueForQuotedFromSpDefinition( ParameterType type, String query ) {
        return getDefaultValue( type, query,
                getStringPatternForParameter( type.getName() ).matcher( query ) );
    }

    protected String getDefaultValueForNumberFromSpDefinition( ParameterType type, String query ) {
        return getDefaultValue( type, query,
                getNumberPatternForParameter( type.getName() ).matcher( query ) );
    }

    private String getDefaultValue( ParameterType type, String query, Matcher matcher  ){
        if ( getNullPatternForParameter( type.getName() ).matcher( query ).find() ){
            return NULL;
        }
        return matcher.find() ? matcher.group( 1 ) : NOT_FOUNDED;
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
}
