package com.kreig133.daogenerator.db.preparators;

import com.kreig133.daogenerator.jaxb.ParameterType;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.kreig133.daogenerator.db.extractors.TableNameHelper.getTableName;

/**
 * @author eshangareev
 * @version 1.0
 */
public class NameModeQueryPreparator extends  QueryPreparator{

    public String prepareQueryNameMode( @NotNull String query ) {
        @Language("RegExp")
        String regExp = "(?s)((%s)\\s*=\\s*):(\\w+)\\b";

        List<ParameterType> columnsFromDbByTableName = getColumnsFromDbByTableName( getTableName( query ) );

        for ( String subPattern : subPatterns ) {
            Matcher matcher = Pattern.compile( String.format( regExp, subPattern ) ).matcher( query );
            while ( matcher.find() ) {
                ParameterType actualParameter = getActualParameter( columnsFromDbByTableName, matcher.group( 3 ) );
                query = query.replaceAll(
                        String.format( regExp, matcher.group( 2 ) ),
                        String.format( "$1\\${%s;%s}", actualParameter.getName(), actualParameter.getSqlType() )
                );
            }
        }
        query = replaceCastNameMode( query );

        return replaceOthersNameMode( query );
    }

    protected String replaceCastNameMode( String query ) {
        String castNameModeRegex = "(?i)(\\bcast\\s*\\(\\s*):(\\w+)(\\s*as\\s*("
                + SQL_TYPE +")\\s*\\))";
        Matcher matcher = Pattern.compile( castNameModeRegex ).matcher( query );
        while ( matcher.find() ){
            query = query.replaceAll( castNameModeRegex, "$1\\${$2;$4}$3" );
        }
        return query;
    }

    String replaceOthersNameMode( @NotNull String query ) {
        return query.replaceAll( "(?<=\\W):(\\w+)", "\\${$1;"+ERROR+"}" );
    }
}