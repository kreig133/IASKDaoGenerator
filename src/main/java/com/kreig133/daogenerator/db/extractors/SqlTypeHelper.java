package com.kreig133.daogenerator.db.extractors;

import com.kreig133.daogenerator.jaxb.JavaType;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author eshangareev
 * @version 1.0
 */
public class SqlTypeHelper {

    public static final String DATA_TYPE_COLUMN         = "DATA_TYPE";
    public static final String CHARACTER_MAXIMUM_LENGTH = "CHARACTER_MAXIMUM_LENGTH";
    public static final String NUMERIC_PRECISION = "NUMERIC_PRECISION";
    public static final String NUMERIC_SCALE = "NUMERIC_SCALE";


    public static String getSqlTypeFromResultSet( ResultSet resultSet ) throws SQLException {
        String sqlType = resultSet.getString( DATA_TYPE_COLUMN );

        if( isStringWithPrecision( sqlType ) ) {
            return String.format( "%s(%s)",
                    sqlType, resultSet.getString( CHARACTER_MAXIMUM_LENGTH ) );
        }

        if ( isDouble( sqlType ) ) {
            return String.format( "%s(%s,%s)",
                    sqlType , resultSet.getString( NUMERIC_PRECISION ), resultSet.getString( NUMERIC_SCALE ) );
        }

        return sqlType;
    }

    private static boolean isStringWithPrecision( String sqlType ) {
        return JavaType.getBySqlType( sqlType ) == JavaType.STRING && !sqlType.equalsIgnoreCase( "text" );
    }

    private static boolean isDouble( String sqlType ) {
        return JavaType.getBySqlType( sqlType ) == JavaType.DOUBLE;
    }

    public static String getSqlTypeFromResultSet( ResultSetMetaData metaData, int index )  {
        String sqlType = null;
        try {
            sqlType = metaData.getColumnTypeName( index );


            if( isStringWithPrecision( sqlType ) ) {
                return String.format( "%s(%s)",
                        sqlType, metaData.getPrecision( index ) );
            }

            if ( isDouble( sqlType ) ) {
                return String.format( "%s(%s,%s)",
                        sqlType , metaData.getPrecision( index ), metaData.getScale( index ) );
            }
        } catch ( SQLException e ) {
            e.printStackTrace();
        }

        return sqlType;
    }
}
