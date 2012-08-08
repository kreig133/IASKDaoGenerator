package com.kreig133.daogenerator.db;

import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import org.jetbrains.annotations.NotNull;

import java.sql.ParameterMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author eshangareev
 * @version 1.0
 */
public class JDBCTypeIdConverter {
    static Map<Integer, String> map;

    static {
        map = new HashMap<Integer, String>();
        map.put( - 7, "BIT" );
        map.put( - 6, "TINYINT" );
        map.put( 5  , "SMALLINT" );
        map.put( 4  , "INTEGER" );
        map.put( -5 , "BIGINT" );
        map.put( 6  , "FLOAT" );
        map.put( 7  , "REAL" );
        map.put( 8  , "DOUBLE" );
        map.put( 2  , "NUMERIC" );
        map.put( 3  , "DECIMAL" );
        map.put( 1  , "CHAR" );
        map.put( 12 , "VARCHAR" );
        map.put( - 1, "ONGVARCHAR" );
        map.put( 91 , "DATE" );
        map.put( 92 , "TIME" );
        map.put( 93 , "TIMESTAMP" );
        map.put( - 2, "BINARY" );
        map.put( - 3, "VARBINARY" );
        map.put( - 4, "LONGVARBINARY" );
        map.put( 0, "NULL" );
        map.put( 1111, "OTHER" );
        map.put( 2000, "JAVA_OBJECT" );
        map.put( 2001, "DISTINCT" );
        map.put( 2002, "STRUCT" );
        map.put( 2003, "ARRAY" );
        map.put( 2004, "BLOB" );
        map.put( 2005, "CLOB" );
        map.put( 2006, "REF" );
        map.put( 70, "DATALINK" );
        map.put( 16, "BOOLEAN" );
        map.put( -8, "ROWID" );
        map.put( -15, "NCHAR" );
        map.put( -9, "NVARCHAR" );
        map.put( -16, "LONGNVARCHAR" );
        map.put( -2011, "NCLOB" );
        map.put( -2009, "SQLXML" );
    }

    public static String getJdbcTypeNameById( int id ) {
        return map.get( id );
    }
}
