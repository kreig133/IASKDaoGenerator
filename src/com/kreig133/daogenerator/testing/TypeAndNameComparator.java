package com.kreig133.daogenerator.testing;

import com.kreig133.daogenerator.common.settings.FunctionSettings;
import com.kreig133.daogenerator.enums.JavaType;
import com.kreig133.daogenerator.parameter.Parameter;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class TypeAndNameComparator {

    public static List<String> compare(
            ResultSetMetaData metaData,
            FunctionSettings  functionSettings
    ) throws SQLException {
        List<String> errorList = new ArrayList<String>(  );

        //TODO выкосить потом
        int ii = 1 ;
        System.out.println("FunctionSettings parametres");
        for ( Parameter outputParameterList :functionSettings.getOutputParameterList() ){
            System.out.println(
                    ( ii++ ) +
                            "  -  " +
                            outputParameterList.getName()+
                            "  -  " +
                            outputParameterList.getType() );
        }
        ii = 0;
        System.out.println("MetaData parametres");
        for ( int i = 1; i <= metaData.getColumnCount(); i++ ){
            System.out.println(
                    i +
                            "  -  " +
                            metaData.getColumnName( i )+
                            "  -  " +
                            metaData.getColumnTypeName( i ) );
        }

        for( int i = 1; i <= metaData.getColumnCount(); i++ ){
            boolean exist = false;
            String columnName = metaData.getColumnName( i );
            JavaType javaType = JavaType.getBySqlType( metaData.getColumnTypeName( i ) );
            for( Parameter parameter: functionSettings.getOutputParameterList() ){
                if( columnName.equals( parameter.getName() )){
                    if( javaType != parameter.getType() ){
                        errorList.add(
                                "Не совпадают типы данных для колонки "
                                        + columnName
                                        + ": указан в описании "
                                        + parameter.getType()
                                        + ", а в ResultSet имеем "
                                        +javaType
                                        + " ( "
                                        + metaData.getColumnTypeName( i )
                                        +")"
                        );
                    }
                    exist = true;
                    break;
                }
            }

            if( !exist ) {
                errorList.add( "В ResultSet'e есть \"неописанная\" переменная : " + columnName );
            }
        }

        return errorList;
    }
}
