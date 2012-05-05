package com.kreig133.daogenerator.db.extractors;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.jaxb.SelectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author eshangareev
 * @version 1.0
 */
public class Extractor {

    protected static final Pattern storeNamePattern = Pattern.compile( "(?iu)exec\\w*\\s+(\\w+)" );

    public static SelectType determineQueryType( @NotNull String query ) {
        return Utils.stringContainsMoreThanOneWord( query ) ?
                    SelectType.getByName( query.trim().split( "\\s" )[0] ):
                    SelectType.CALL;
    }

    @Nullable
    public static String getStoreProcedureName( @NotNull String query ) {
        final String s = query.replaceAll( "(?i)dbo\\.", "" ).trim();

        if( Utils.stringContainsMoreThanOneWord( s ) ){
            final Matcher matcher = storeNamePattern.matcher( s );
            return matcher.find() ? matcher.group( 1 ) : null;
        }

        return s;
    }
}
