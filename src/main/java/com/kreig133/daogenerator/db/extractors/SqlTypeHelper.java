package com.kreig133.daogenerator.db.extractors;

import com.kreig133.daogenerator.jaxb.JavaType;

import java.sql.ResultSet;
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

        if( JavaType.getBySqlType( sqlType ) == JavaType.STRING && !sqlType.equalsIgnoreCase( "text" ) ) {
            return String.format( "%s(%s)",
                    sqlType, resultSet.getString( CHARACTER_MAXIMUM_LENGTH ) );
        }

        if ( JavaType.getBySqlType( sqlType ) == JavaType.DOUBLE ) {
            return String.format( "%s(%s,%s)",
                    sqlType , resultSet.getString( NUMERIC_PRECISION ), resultSet.getString( NUMERIC_SCALE ) );
        }

        return sqlType;
    }
}
