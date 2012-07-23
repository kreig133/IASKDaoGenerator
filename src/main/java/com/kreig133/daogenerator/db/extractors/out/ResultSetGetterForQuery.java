package com.kreig133.daogenerator.db.extractors.out;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.db.JDBCConnector;
import com.kreig133.daogenerator.db.JDBCTypeIdConverter;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.sql.creators.QueryCreator;
import com.kreig133.daogenerator.sql.test.TestValueByStringGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

/**
 * @author kreig133
 * @version 1.0
 */
public class ResultSetGetterForQuery implements  ResultSetGetter{

    @Nullable
    @Override
    public ResultSet getResultSetAndFillJdbcTypeIfNeed( @NotNull DaoMethod daoMethod ) throws SQLException {
        final String query =
                QueryCreator.getQueryStringWithoutMetaData( daoMethod.getCommon().getQuery() );

        assert query != null;

        final PreparedStatement statement = JDBCConnector.instance().connectToDB().prepareStatement( query );

        List<String> names = QueryCreator.getListOfParametrNames( daoMethod.getCommon().getQuery() );

        for ( int i = 0; i < names.size(); i++ ) {
            ParameterType paramter = daoMethod.getInputParametrs().getParameterByName( names.get( i ) );

            assert paramter != null;

            setParameter( statement, paramter, i + 1 );
        }

        return statement.execute() ? statement.getResultSet() : null ;
    }

    void setParameter( PreparedStatement statement, ParameterType parameter, int parameterIndex ) throws SQLException {
        String testValue = TestValueByStringGenerator.newInstance( parameter ).getTestValue( parameter );

        if( testValue.equals( TestValueByStringGenerator.NULL ) ){
            statement.setNull( parameterIndex, JDBCTypeIdConverter.getIdByJdbcTypeName( parameter.getJdbcType() ) );
            return;
        }

        if ( testValue.startsWith( "'" ) ) {
            testValue = testValue.substring( 1, testValue.length() - 2 );
        }

        switch ( parameter.getType() ) {
            case BYTE:
            case LONG:
                statement.setLong( parameterIndex, Long.parseLong( testValue ) );
                break;
            case DOUBLE:
                statement.setDouble( parameterIndex, Double.parseDouble( testValue ) );
                break;
            case DATE:
                try {
                    statement.setDate( parameterIndex,
                            new Date( Utils.getDaoGeneratorDateFormat().parse( testValue ).getTime() ) );
                } catch ( ParseException ex ) {
                    throw new IllegalArgumentException( parameter.getTestValue() );
                }
                break;
            case STRING:
                statement.setString( parameterIndex, testValue );
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
}
