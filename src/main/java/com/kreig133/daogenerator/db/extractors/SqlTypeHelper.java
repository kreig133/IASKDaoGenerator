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
public abstract class SqlTypeHelper {

    public static String getSqlTypeFromResultSet(
            @NotNull ResultSet resultSet, @NotNull ColumnNameHolder columnNameHolder
    ) throws SQLException {
        return getFullSqlTypeDefinition(
                resultSet.getString( columnNameHolder.getDataTypeColumnName() ),
                resultSet.getString( columnNameHolder.getCharacterMaximumLengthColumnName() ),
                resultSet.getString( columnNameHolder.getNumericPrecisionColumnName() ),
                resultSet.getString( columnNameHolder.getNumericScaleColumnName() ) );
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

    public interface ColumnNameHolder{
        String getNumericScaleColumnName();
        String getNumericPrecisionColumnName();
        String getCharacterMaximumLengthColumnName();
        String getDataTypeColumnName();
    }
}
