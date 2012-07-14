package com.kreig133.daogenerator.db.preparators;

import com.kreig133.daogenerator.jaxb.ParameterType;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.kreig133.daogenerator.common.Utils.getDaoGeneratorDateFormat;

/**
 * @author eshangareev
 * @version 1.0
 */
public class DoubleQueryPreparator extends QueryPreparator {

    public String prepareQuery( String first, String second ){
        String queryWithNames;
        String queryWithTestValues;

        if( determineWorkingMode( first ) == WorkingMode.NAME ){
            queryWithNames = first;
            queryWithTestValues = second;
        } else if ( determineWorkingMode( second ) == WorkingMode.NAME ) {
            queryWithNames = second;
            queryWithTestValues = first;
        } else {
            throw new AssertionError();
        }

        String rawQuery = queryWithNames;

        queryWithNames      = prepareQueryBeforeParse( queryWithNames );
        queryWithTestValues = prepareQueryBeforeParse( queryWithTestValues );

        List<String> paramNames = new ArrayList<String>();
        List<String> queryPiece = new ArrayList<String>();

        parseQueryWithName( queryPiece, paramNames, queryWithNames );

        for ( int i = 0; i < paramNames.size(); i++ ) {
            String after = StringUtils.substringAfter( queryWithTestValues, queryPiece.get( i ) );
            ParameterType pType = new ParameterType();
            {
                Matcher matcher = Pattern.compile( "(?iu)" + TEST_VALUES ).matcher( after );

                pType.setName( paramNames.get( i ) );
                if( matcher.find() ){
                    pType.setTestValue( matcher.group( 1 ) );
                }
            }
            if( queryPiece.get( i ).matches( "(?isu).*cast\\s*\\(\\s*" ) ){
                Matcher matcher = Pattern.compile( "(?iu)\\s*as\\s*(" + SQL_TYPE + ")" ).matcher( queryPiece.get( i + 1 ) );
                if( matcher.find() ) {
                    pType.setSqlType( matcher.group( 1 ) );
                }
            } else {
                determineSqlTypeByTestValue( pType );
            }

            rawQuery = rawQuery.replaceAll( ":" + pType.getName(),
                    String.format( "\\${%s;%s;%s}", pType.getName(), pType.getSqlType(), pType.getTestValue() ) );
        }
        return rawQuery;
    }

    protected void determineSqlTypeByTestValue( @NotNull ParameterType pType ) {
        if(pType.getTestValue().matches( "(-)?\\d+" )){
            pType.setSqlType( "int" );
        } else if( pType.getTestValue().matches( "(-)?[\\d\\.]+" ) ){
            pType.setSqlType( "numeric" );
        } else {
            try {
                getDaoGeneratorDateFormat().parse(
                        pType.getTestValue().substring( 1, pType.getTestValue().length() - 1 )
                );
                pType.setSqlType( "datetime" );
            } catch ( Exception e ){
                if( pType.getTestValue().startsWith( "'" )){
                    pType.setSqlType( "varchar" );
                } else {
                    pType.setSqlType( ERROR );
                }
            }
        }
    }



    protected void parseQueryWithName(
            @NotNull List<String> queryPiece, @NotNull List<String> paramNames, @NotNull String queryWithNames
    ) {
        char[] chars = queryWithNames.toCharArray();

        StringBuilder queryPieceBuilder = new StringBuilder();
        StringBuilder paramNameBuilder = new StringBuilder();
        Character quote = null;
        boolean quotedMode = false;
        boolean paramNameMode = false;

        for ( char aChar : chars ) {
            if ( paramNameMode ) {
                if ( new String( new char[]{ aChar } ).matches( "[\\W&&[^@$#]]" ) ) {
                    paramNameMode = false;
                    paramNames.add( paramNameBuilder.toString() );
                    paramNameBuilder = new StringBuilder();
                    queryPieceBuilder.append( aChar );
                } else {
                    paramNameBuilder.append( aChar );
                }
            } else {
                if ( aChar == ':' ) {
                    if ( ! quotedMode ) {
                        paramNameMode = true;
                        queryPiece.add( queryPieceBuilder.toString() );
                        queryPieceBuilder = new StringBuilder();
                    }
                } else {
                    if ( aChar == '\'' || aChar == '[' || aChar == '\"' || aChar == ']' ) {
                        if ( quotedMode ) {
                            if ( getCloseChar( quote ) == aChar ) {
                                quotedMode = false;
                                quote = null;
                            } else if ( aChar != ']' ) {
                                quote = aChar;
                                quotedMode = true;
                            }
                        }
                    }
                    queryPieceBuilder.append( aChar );
                }
            }
        }

        if( paramNameMode ) {
            paramNames.add( paramNameBuilder.toString() );
        } else {
            queryPiece.add( queryPieceBuilder.toString() );
        }
    }

    protected String prepareQueryBeforeParse( String query ){
        return query.replaceAll( "(\\b)\\s+(\\b)", "$1 $2" )
                    .replaceAll( "(\\B)\\s+", "$1" )
                    .replaceAll( "\\s+(\\B)", "$1" );
    }

    private char getCloseChar( @NotNull Character quote ) {
        switch ( quote ){
            case '\'':
                return '\'';
            case '\"':
                return '\"';
            case '[':
                return ']';
            default:
                throw new RuntimeException();
        }
    }

    //<editor-fold desc="Singleton">
    private final static DoubleQueryPreparator INSTANCE = new DoubleQueryPreparator();

    private DoubleQueryPreparator() {
    }

    public static DoubleQueryPreparator instance(){
        return INSTANCE;
    }
    //</editor-fold>
}
