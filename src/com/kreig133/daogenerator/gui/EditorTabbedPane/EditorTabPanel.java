package com.kreig133.daogenerator.gui.EditorTabbedPane;

import com.kreig133.daogenerator.InOutClass;
import com.kreig133.daogenerator.parametr.Parameter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class EditorTabPanel extends JTabbedPane {

    JTable table = new JTable(  );

    public EditorTabPanel() {
       init();
    }

    protected void init(){
        this.setModel( new DefaultSingleSelectionModel() );
        this.addTab( "Text", new EditorTextArea() );
        this.addTab( "Parsed", null  ); //TODO
    }
}

class EditorTextArea extends JPanel{
    JTextArea textArea = new JTextArea(  );

    EditorTextArea() {
        super( new GridLayout(  ) );
        this.add( textArea );
    }

    public String getText(){
        return textArea.getText();
    }

    public void setText( String text ){
        textArea.setText( text );
    }
}

class EditorParsedTable extends JPanel{

    JTable table = new JTable();

    EditorParsedTable() {
        super( new GridLayout(), true );
        init();
    }

    private void init(){
    }

    class InOutClassDataModel extends AbstractTableModel {
        String[] headers = { "Name", "Type", "Default", "SQL-type", "Comment" };
        List<Parameter> paramets = new ArrayList<Parameter>();

        InOutClassDataModel() {
        }

        public int getRowCount() {
            return paramets.size();
        }

        public int getColumnCount() {
            return headers.length;
        }

        public Object getValueAt( int rowIndex, int columnIndex ) {
            Object result = null;

            final Parameter parameter = paramets.get( rowIndex );

            switch ( columnIndex ){
                case 0:
                    result = parameter.getName();
                    break;
                case 1:
                    result = parameter.getType();
                    break;
                case 2:
                    result = parameter.getName();//TODO
                    break;
                case 3:
                    break;
                case 4:
                    break;
            }
            
            return result;
        }

        @Override
        public boolean isCellEditable( int rowIndex, int columnIndex ) {
            return true;
        }

        @Override
        public void setValueAt( Object aValue, int rowIndex, int columnIndex ) {

        }
    }
}
