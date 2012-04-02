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

    public static String getSqlTypeFromResultSet( ResultSet resultSet ) throws SQLException {
        String sqlType = resultSet.getString( DATA_TYPE_COLUMN );

        if( JavaType.getBySqlType( sqlType ) == JavaType.STRING ){
            return sqlType + "(" + resultSet.getString( CHARACTER_MAXIMUM_LENGTH ) + ")";
        }
        return sqlType;
        //TODO добавить для Double
//        if( JavaType.getBySqlType( sqlType ))
    }
}
