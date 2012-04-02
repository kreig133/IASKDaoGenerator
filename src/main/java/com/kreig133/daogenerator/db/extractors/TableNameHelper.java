package com.kreig133.daogenerator.db.extractors;

import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.SelectType;
import org.intellij.lang.annotations.Language;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author eshangareev
 * @version 1.0
 */
public class TableNameHelper {

    @Language( "RegExp" )
    private static String tableNameExtractingPatter = "(?isu)%s.*?%s\\s+(dbo\\.)?(\\w+)\\b";

    private static Pattern getPatterForTableNameExtracting( SelectType type) {
        return Pattern.compile( String.format( tableNameExtractingPatter, type.name(), type.keyWord() ) );
    }

    public static String getTableName( DaoMethod daoMethod ) {
        return getTableName( daoMethod.getCommon().getQuery() );
    }

    public static String getTableName( String query ) {
        Matcher matcher = getPatterForTableNameExtracting( Extractor.determineQueryType( query ) ).matcher( query );
        return matcher.find() ? matcher.group( 2 ) : "";
    }
}
