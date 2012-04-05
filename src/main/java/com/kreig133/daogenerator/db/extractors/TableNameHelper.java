package com.kreig133.daogenerator.db.extractors;

import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.SelectType;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author eshangareev
 * @version 1.0
 */
public class TableNameHelper {

    @NotNull
    @Language( "RegExp" )
    private static String tableNameExtractingPatter = "(?isu)%s.*?%s\\s+(dbo\\.)?(\\w+)\\b";

    private static Pattern getPatterForTableNameExtracting( @NotNull SelectType type) {
        return Pattern.compile( String.format( tableNameExtractingPatter, type.name(), type.keyWord() ) );
    }

    @NotNull
    public static String getTableName( @NotNull DaoMethod daoMethod ) {
        return getTableName( daoMethod.getCommon().getQuery() );
    }

    @NotNull
    public static String getTableName( @NotNull String query ) {
        Matcher matcher = getPatterForTableNameExtracting( Extractor.determineQueryType( query ) ).matcher( query );
        return matcher.find() ? matcher.group( 2 ) : "";
    }
}
