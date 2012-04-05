package com.kreig133.daogenerator.gui;

import com.kreig133.daogenerator.jaxb.ParameterType;
import org.jetbrains.annotations.NotNull;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

class ParametrsModel extends AbstractTableModel {

    @NotNull
    static String[] columnsName = {
            "№", "Название", "Тип", "SQL-тип", "IN/OUT", "По умолчанию",
            "Для теста", "Переименовать в", "JDBC-тип", "Комментарий"
    };

    @NotNull
    List<ParameterType> parameterTypes = new ArrayList<ParameterType>();

    @NotNull
    public List<ParameterType> getParameterTypes() {
        return parameterTypes;
    }

    @Override
    public int getRowCount() {
        return parameterTypes.size();
    }

    @Override
    public int getColumnCount() {
        return columnsName.length;
    }

    @Override
    public String getColumnName( int column ) {
        return columnsName[ column ];
    }

    @Override
    public Object getValueAt( int rowIndex, int columnIndex ) {
        switch ( columnIndex ) {
            case 0:
                return rowIndex + 1;
            case 1:
                return parameterTypes.get( rowIndex ).getName();
            case 2:
                return parameterTypes.get( rowIndex ).getType().value();
            case 3:
                return parameterTypes.get( rowIndex ).getSqlType();
            case 4:
                return parameterTypes.get( rowIndex ).getInOut();
            case 5:
                return parameterTypes.get( rowIndex ).getDefaultValue();
            case 6:
                return parameterTypes.get( rowIndex ).getTestValue();
            case 7:
                return parameterTypes.get( rowIndex ).getRenameTo();
            case 8:
                return parameterTypes.get( rowIndex ).getJdbcType();
            case 9:
                return parameterTypes.get( rowIndex ).getComment();
            default:
                throw new RuntimeException( "Ошибка при работе с таблицей" );
        }
    }

    @Override
    public void setValueAt( @NotNull Object aValue, int rowIndex, int columnIndex ) {
        switch ( columnIndex ) {
            case 1:
                parameterTypes.get( rowIndex ).setName( ( ( String ) aValue ).trim() );
                break;
            case 2:
//                parameterTypes.get( rowIndex ).setType();
                break;
            case 3:
                parameterTypes.get( rowIndex ).setSqlType( ( ( String ) aValue ).trim() );
                break;
            case 4:
//                parameterTypes.get( rowIndex ).getInOut();
                break;
            case 5:
                parameterTypes.get( rowIndex ).setDefaultValue( ( String ) aValue );
                break;
            case 6:
                parameterTypes.get( rowIndex ).setTestValue( ( String ) aValue );
                break;
            case 7:
                parameterTypes.get( rowIndex ).setRenameTo( ( ( String ) aValue ).trim() );
                break;
            case 8:
                break;
            case 9:
                parameterTypes.get( rowIndex ).setComment( ( ( String ) aValue ).trim() );
                break;
        }
    }

    @Override
    public boolean isCellEditable( int rowIndex, int columnIndex ) {
        return true;
    }
}