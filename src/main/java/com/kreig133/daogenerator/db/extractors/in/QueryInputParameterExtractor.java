package com.kreig133.daogenerator.db.extractors.in;

import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.SelectType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author eshangareev
 * @version 1.0
 */
abstract public class QueryInputParameterExtractor extends InputParameterExtractor{
    
    @Override
    public DaoMethod fillMethodName( DaoMethod daoMethod ) {
        SelectType type = daoMethod.getSelectType();
        daoMethod.getCommon().setMethodName(
                type.name().toLowerCase() + type.keyWord() + getTableName( daoMethod )
        );
        return daoMethod;
    }
    
    String tableNameExtractingPatter = "(?isu)%s.*?%s\\s+(dbo\\.)?(\\w+)\\b";
    
    private Pattern getPatterForTableNameExtracting( SelectType type) {
        return Pattern.compile( String.format( tableNameExtractingPatter, type.name(), type.keyWord() ) );
    }
            
    protected String getTableName( DaoMethod daoMethod ) {
        Matcher matcher = getPatterForTableNameExtracting( daoMethod.getSelectType() ).matcher(
                daoMethod.getCommon().getQuery() );
        if ( matcher.find() ) {
            return matcher.group( 2 );
        }
        return "";
    }
}
