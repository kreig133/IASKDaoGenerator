package com.kreig133.daogenerator.db.preparators;

import com.kreig133.daogenerator.db.JDBCConnector;
import com.kreig133.daogenerator.db.extractors.SqlTypeHelper;
import com.kreig133.daogenerator.jaxb.JavaType;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.jaxb.ParametersType;
import org.apache.commons.lang.StringUtils;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class QueryPreparator {

    protected static final String ERROR = "!!ERROR!!";

    protected enum WorkingMode{
        VALUE, NAME
    }

    @Language( "RegExp" )
    protected static final String SQL_TYPE = "\\w+(\\s*\\(\\s*\\d+(\\s*,\\s*\\d+)?\\s*\\))?";
    @Language( "RegExp" )
    protected static final String TEST_VALUES = "(([-\\d\\.]+)|(null)|('.+?'))";
    @NotNull
    @Language("RegExp")
    protected String regex = "(?u)([\"\\[]?\\w+[\"\\]]?)?%s\\s*=\\s*" + TEST_VALUES;
    @NotNull
    @Language( "RegExp" )
    protected String castRegExp = "(?isu)(%s\\s*=.*?)\\bcast\\s*\\(\\s*"+ TEST_VALUES +"\\s*as\\s*("+ SQL_TYPE + ")\\s*\\)";
    @NotNull
    @Language("RegExp")
    protected String columnName = "\\b([@#\\w&&[\\D]][\\w\\$@#]*)\\b";
    @NotNull
    @Language("RegExp")
    protected String quotedColumnName = "\"(.+?)\"";
    @NotNull
    @Language("RegExp")
    protected String columnNameInBrackets = "\\[(.+?)\\]";
    @NotNull
    @Language( "RegExp" )
    protected String testValuesInInsert = "(?i)"+ TEST_VALUES + "\\s*[,\\)]";
    @NotNull
    @Language("RegExp")
    protected String insertRE =
            "(?isu)insert\\b\\s*\\binto\\b\\s*.+?\\s*(\\(\\s*(.+?)\\s*\\))?\\s*\\bvalues\\b\\s*\\((.+?\\))";
    @NotNull
    protected String[] subPatterns = { columnName, quotedColumnName, columnNameInBrackets };

    public String prepareQuery( String query ) {
        String result;
        if( determineWorkingMode( query ) == WorkingMode.VALUE ) {
            result = new ValueModeQueryPreparator().prepareQueryValueMode( query );
        } else {
            result = new NameModeQueryPreparator().prepareQueryNameMode ( query );
        }
        return result
                .replaceAll( "(\\$\\{.+?;)'(.+?\\})", "$1$2" )
                .replaceAll( "(\\$\\{.+?)'\\}", "$1}" )
                .replaceAll( "``", "" );
    }

    @NotNull
    protected WorkingMode determineWorkingMode( String query ) {
        int colonCountInsideQuote = 0;
        int colonCountTotal = StringUtils.countMatches( query, ":" );
        Matcher matcher = Pattern.compile( "['\"\\[].*?(:.*?)['\"\\]]" ).matcher( query );
        while ( matcher.find() ){
            colonCountInsideQuote += StringUtils.countMatches( matcher.group( 1 ), ":" );
        }

        return colonCountInsideQuote < colonCountTotal ? WorkingMode.NAME : WorkingMode.VALUE;
    }

    protected ParameterType getActualParameter( List<ParameterType> columnsFromDbByTableName, @NotNull String column ) {
        ParameterType parameterByName = ParametersType.getParameterByName( column.trim(), columnsFromDbByTableName );
        if( parameterByName == null ) {
            parameterByName = getColumnFromDbByColumnName( column.trim() );
        }

        if( parameterByName == null ) {
            parameterByName = new ParameterType();
            parameterByName.setName( column );
            parameterByName.setSqlType( ERROR );
        }
        return parameterByName;
    }

    protected ParameterType getColumnFromDbByColumnName( String columnName ) {
        Connection connection = JDBCConnector.instance().connectToDB();
        assert connection != null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT DISTINCT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE COLUMN_NAME = ?"
            );
            preparedStatement.setString( 1, columnName );
            Set<JavaType> types = new HashSet<JavaType>();

            String s = null;
            ResultSet resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
                s = resultSet.getString( 1 );
                types.add( JavaType.getBySqlType( s ) );
            }

            assert types.size() == 1;

            ParameterType parameterType = new ParameterType();
            parameterType.setSqlType( s );
            parameterType.setName( columnName );

            return parameterType;
        } catch ( SQLException e ) {
            e.printStackTrace();
            return null;
        }
    }

    @NotNull
    protected List<ParameterType> getColumnsFromDbByTableName( String tableName ) {
        try {
            Connection connection = JDBCConnector.instance().connectToDB();

            assert connection != null;

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ? ORDER BY ORDINAL_POSITION" );
            statement.setString( 1, tableName );

            ResultSet resultSet = statement.executeQuery();
            List<ParameterType> parameterTypeList = new ArrayList<ParameterType>();
            while ( resultSet.next() ) {
                ParameterType type = new ParameterType();
                type.setSqlType( SqlTypeHelper.getSqlTypeFromResultSet( resultSet ) );
                type.setName( resultSet.getString( "COLUMN_NAME" ) );
                parameterTypeList.add( type );
            }
            return parameterTypeList;
        } catch ( Exception e ) {
            e.printStackTrace();
            return new ArrayList<ParameterType>();
        }
    }

    private final static QueryPreparator INSTANCE = new QueryPreparator();

    //TODO че за ?
    protected QueryPreparator() {
    }

    public static QueryPreparator instance(){
        return INSTANCE;
    }
}