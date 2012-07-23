package com.kreig133.daogenerator.db.extractors.out;

import com.kreig133.daogenerator.jaxb.JavaType;
import com.kreig133.daogenerator.jaxb.ParameterType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author eshangareev
 * @version 1.0
 */
public class ResultSetGetterForQueryTest {

    PreparedStatement statement;

    @Before
    public void before() throws SQLException {
        statement = Mockito.mock( PreparedStatement.class, new Answer() {
            @Override
            public Object answer( InvocationOnMock invocationOnMock ) throws Throwable {
                for ( int i = 0; i < invocationOnMock.getArguments().length; i++ ) {
                    System.out.println( "i = " + invocationOnMock.getArguments()[ i ] );
                }
                return null;
            }
        } );
    }

    @Test
    public void test() throws SQLException {
        ResultSetGetterForQuery resultSetGetterForQuery = new ResultSetGetterForQuery();
        ParameterType parameterType = new ParameterType();
        parameterType.setTestValue( "'1231245'" );
        parameterType.setType( JavaType.STRING );
        resultSetGetterForQuery.setParameter( statement, parameterType, 1 );

        parameterType.setTestValue( "" );
        parameterType.setType( JavaType.DOUBLE );
        parameterType.setJdbcType( "INTEGER" );
        resultSetGetterForQuery.setParameter( statement, parameterType, 2 );
    }
}
