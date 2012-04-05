package com.kreig133.daogenerator.db.extractors;

import com.kreig133.daogenerator.jaxb.JavaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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


    public static String getSqlTypeFromResultSet( @NotNull ResultSet resultSet ) throws SQLException {
        return getFullSqlTypeDefinition(
                resultSet.getString( DATA_TYPE_COLUMN ),
                resultSet.getString( CHARACTER_MAXIMUM_LENGTH ),
                resultSet.getString( NUMERIC_PRECISION ),
                resultSet.getString( NUMERIC_SCALE ) );
    }



    private static boolean isStringWithPrecision( @NotNull String sqlType ) {
        return JavaType.getBySqlType( sqlType ) == JavaType.STRING && !sqlType.equalsIgnoreCase( "text" );
    }

    private static boolean isDouble( @NotNull String sqlType ) {
        return JavaType.getBySqlType( sqlType ) == JavaType.DOUBLE && !sqlType.equalsIgnoreCase( "money" );
    }

    @Nullable
    public static String getSqlTypeFromResultSet( @NotNull ResultSetMetaData metaData, int index )  {
        String sqlType = null;
        try {
            sqlType = getFullSqlTypeDefinition(
                    metaData.getColumnTypeName( index ),
                    metaData.getPrecision( index ),
                    metaData.getPrecision( index ),
                    metaData.getScale( index )
            );
        } catch ( SQLException e ) {
            e.printStackTrace();
        }

        return sqlType;
    }

    private static String getFullSqlTypeDefinition(
            String sqlType, Object stringPrecision, Object numberPrecision, Object numberScale
    ){
        if( isStringWithPrecision( sqlType ) ) {
            sqlType =  getLiteralSqlTypeDefinition( sqlType, stringPrecision ) ;
        }

        if ( isDouble( sqlType ) ) {
            sqlType =  getNumericSqlTypeDefinition( sqlType, numberPrecision, numberScale );
        }
        return sqlType;
    }

    private static String getNumericSqlTypeDefinition(
            String sqlType, Object precision, Object scale
    ){
        return String.format( "%s(%s,%s)", sqlType, precision, scale );
    }

    private static String getLiteralSqlTypeDefinition(
            String sqlType, Object precision
    ){
        return String.format( "%s(%s)", sqlType, precision );
    }
}
