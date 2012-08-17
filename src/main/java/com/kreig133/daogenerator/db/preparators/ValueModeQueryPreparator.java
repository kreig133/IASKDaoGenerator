package com.kreig133.daogenerator.db.preparators;

import com.google.common.base.Preconditions;
import com.kreig133.daogenerator.db.extractors.Extractor;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.jaxb.ParametersType;
import com.kreig133.daogenerator.jaxb.SelectType;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.kreig133.daogenerator.db.extractors.TableNameHelper.getTableName;

/**
 * @author eshangareev
 * @version 1.0
 */
public class ValueModeQueryPreparator extends QueryPreparator {
    public String prepareQueryValueMode( @NotNull String query ) {
        List<ParameterType> result = getActualParameterList(
                getColumnsFromQuery( query ).keySet().iterator(),
                getColumnsFromDbByTableName( getTableName( query ) )
        );
        query = replaceTestVaulesByDaoGeneratorFormatedInfoString( query, result );

        return prepareCast( prepareAsInsertQueryIfNeed( query ) );
    }

    @NotNull
    private List<ParameterType> getActualParameterList(
            @NotNull Iterator<String> columnsFromQuery,
            List<ParameterType> columnsFromDbByTableName
    ) {
        List<ParameterType> result = new ArrayList<ParameterType>();
        while ( columnsFromQuery.hasNext() ) {
            result.add( getActualParameter( columnsFromDbByTableName, columnsFromQuery.next() ) );
        }
        return result;
    }

    @NotNull
    protected Map<String, String> getColumnsFromQuery( @NotNull DaoMethod daoMethod ) {
        return getColumnsFromQuery( daoMethod.getCommon().getQuery() );
    }

    @NotNull
    protected Map<String, String> getColumnsFromQuery( String query ) {
        Map<String, String> columnsWithValue = new HashMap<String, String>();
        for ( String subPattern : subPatterns ) {
            Matcher matcher = Pattern.compile( String.format( regex, subPattern ) ).matcher( query );
            while ( matcher.find() ) {
                columnsWithValue.put( matcher.group( 2 ),
                        matcher.group( 5 ) == null ? matcher.group( 6 ) : matcher.group( 5 ) );
            }
        }
        return columnsWithValue;
    }

    protected String prepareCast( String s ) {
        String newQuery = s;
        for ( String subPattern : subPatterns ) {
            String format = String.format( castRegExp, subPattern );
            Matcher matcher = Pattern.compile( format ).matcher( s );
            while ( matcher.find() ) {
                newQuery = newQuery.replaceAll(
                        format,
                        String.format("$1\\${$2;$8;$4}" ) );
            }
        }
        return newQuery;
    }

    protected String replaceTestVaulesByDaoGeneratorFormatedInfoString( String query, @NotNull List<ParameterType> result ) {
        for ( ParameterType parameterType : result ) {
            String pattern = String.format( "(?i)(%s.*?)=\\s*"+ TEST_VALUES, parameterType.getName() );
            String replacement = String.format(
                    "$1= \\${%s;%s;$2}",
                    parameterType.getName(),
                    parameterType.getSqlType()
            );
            query = query.replaceAll( pattern, replacement );
        }
        return query;
    }

    protected String prepareAsInsertQueryIfNeed( @NotNull String query ) {

        if( Extractor.determineQueryType( query ) != SelectType.INSERT ) return query;

        Matcher matcher = Pattern.compile( insertRE ).matcher( query );

        Preconditions.checkState( matcher.find(), "Ошибка в запросе и/или в генераторе!" );

        String columnNamesString = matcher.group( 2 );
        String[] testValues = getTestValues( matcher.group( 3 ) );
        if ( columnNamesString != null ) {
            String[] columnNames = columnNamesString.split( "\\s*,\\s*" );

            assert columnNames.length == testValues.length;

            List<String> columns = new ArrayList<String>();
            for ( int i = 0; i < columnNames.length; i++ ) {
                String input = columnNames[ i ];

                if ( input.matches( quotedColumnName ) || input.matches( columnNameInBrackets ) ) {
                    columnNames[ i ] = input.substring( 1, input.length() - 1 );
                    columns.add( columnNames[ i ] );
                }
                if ( input.matches( columnName ) ) {
                    columns.add( input );
                }
            }
            List<ParameterType> actualParamterList = getActualParameterList(
                    columns.iterator(),
                    getColumnsFromDbByTableName( getTableName( query ) )
            );

            assert actualParamterList.size() <= columnNames.length;

            for ( int i = 0; i < columnNames.length; i++ ) {
                String columnName = columnNames[ i ];
                ParameterType parameterByName =
                        ParametersType.getParameterByName( columnName, actualParamterList );
                if ( parameterByName != null ) {
                    testValues[ i ] = formatTestValue( testValues[ i ], parameterByName );
                }
            }
        } else {
            List<ParameterType> columnsFromDbByTableName = getColumnsFromDbByTableName( getTableName( query ) );

            assert testValues.length == columnsFromDbByTableName.size();

            for ( int i = 0; i < testValues.length; i++ ) {
                testValues[ i ] = formatTestValue( testValues[ i ], columnsFromDbByTableName.get( i ) );
            }
        }
        String join = StringUtils.join( testValues, ",\n\t" );

        return query.replaceAll( "(?isu)\\bvalues\\b\\s*\\((.+?)\\)", String.format( "values(\n\t%s\n)", join ) );
    }

    @NotNull
    private String[] getTestValues( String string ) {
        List<String> testValueList = new ArrayList<String>();
        Matcher testValuesMatcher = Pattern.compile( this.testValuesInInsert ).matcher( string );
        while ( testValuesMatcher.find() ) {
            testValueList.add( testValuesMatcher.group( 1 ) );
        }
        String[] testValues = new String[ testValueList.size() ];
        testValueList.toArray( testValues );

        return testValues;
    }

    private String formatTestValue( String testValue, @NotNull ParameterType parameterByName ) {
        return String.format( "\\${%s;%s;%s}",
                parameterByName.getName(),
                parameterByName.getSqlType(),
                testValue
        );
    }

}
