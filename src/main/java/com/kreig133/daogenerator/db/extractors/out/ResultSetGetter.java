package com.kreig133.daogenerator.db.extractors.out;

import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.SelectType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kreig133
 * @version 1.0
 */
public interface ResultSetGetter {
    ResultSet getResultSetAndFillJdbcTypeIfNeed( DaoMethod daoMethod ) throws SQLException;

    class Factory{
        private static final Map<SelectType, ResultSetGetter> resultSetGetterMap = new HashMap<SelectType, ResultSetGetter>();

        static {
            resultSetGetterMap.put( SelectType.SELECT, new ResultSetGetterForQuery() );
            resultSetGetterMap.put( SelectType.CALL  , new ResultSetGetterForSp() );
        }

        public static ResultSetGetter get( SelectType type ) {
            return resultSetGetterMap.get( type );
        }
    }
}
