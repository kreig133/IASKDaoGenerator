package com.kreig133.daogenerator.db.preparators;

import com.kreig133.daogenerator.jaxb.ParameterType;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author eshangareev
 * @version 1.0
 */
public class DoubleQueryPreparator extends QueryPreparator {


    public String prepareQuery( String first, String second ){
        String queryWithNames;
        String queryWithTestValues;
        final StringBuilder result = new StringBuilder();

        if( determineWorkingMode( first ) == WorkingMode.NAME ){
            queryWithNames = first;
            queryWithTestValues = second;
        } else if ( determineWorkingMode( second ) == WorkingMode.NAME ) {
            queryWithNames = second;
            queryWithTestValues = first;
        } else {
            throw new AssertionError();
        }

        List<String> paramNames = new ArrayList<String>();
        List<String> queryPiece = new ArrayList<String>();

        parseQueryWithName( queryPiece, paramNames, queryWithNames );

        for ( int i = 0; i < paramNames.size(); i++ ) {
            result.append( queryPiece.get( i ) );
            String after = StringUtils.substringAfter( queryWithTestValues, queryPiece.get( 1 ) );
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

            result.append( String.format( "${%s;%s;%s}", pType.getName(), pType.getSqlType(), pType.getTestValue() ) );
        }
        if( queryPiece.size() > paramNames.size() ) {
            result.append( queryPiece.get( paramNames.size() ) );
        }

        return result.toString();
    }

    protected void determineSqlTypeByTestValue( @NotNull ParameterType pType ) {
        if(pType.getTestValue().matches( "(-)?\\d+" )){
            pType.setSqlType( "int" );
        } else if( pType.getTestValue().matches( "(-)?[\\d\\.]+" ) ){
            pType.setSqlType( "numeric" );
        } else {
            try {
                new SimpleDateFormat( "M-d-yyyy H:m:s.SSS" ).parse(
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
                if ( StringUtils.isWhitespace( new String( new char[] { aChar } ) ) ) {
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

    private static DoubleQueryPreparator INSTANCE;
    public static DoubleQueryPreparator instance(){
        if ( INSTANCE == null ) {
            INSTANCE = new DoubleQueryPreparator();
        }
        return INSTANCE;
    }
}
