package com.kreig133.daogenerator.gui;

import com.kreig133.daogenerator.db.StoreProcedureInfoExtractor;
import com.kreig133.daogenerator.jaxb.ParameterType;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class FirstForm {
    private static FirstForm INSTANCE;

    private JTextField storeProcedure;
    private JPanel mainPanel;
    private JTable inputParametrs;
    private JTable outputParametrs;
    private JButton getInParamsButton;
    private JButton SPTextButton;

    private JFrame windowWithSPText;
    
    public FirstForm() {
        getInParamsButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                if ( checkSPName() ) return;

                ( (ParametrsModel) ( inputParametrs.getModel() ) ).getParameterTypes().addAll(
                        StoreProcedureInfoExtractor.getInputParametrsForSP( storeProcedure.getText() )
                );

                inputParametrs.updateUI();
            }
        } );
        
        SPTextButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                if( checkSPName() ) return;

                SPTextView.setText( StoreProcedureInfoExtractor.getSPText( storeProcedure.getText() ) );

                getWindowWithSPText().setVisible( true );
            }
        } );

    }

    private boolean checkSPName( ) {
        String text = storeProcedure.getText();
        
        if( text == null || "".equals( text )  ){
            JOptionPane.showMessageDialog( getInParamsButton, "Введите название хранимой процедуры" );
            return true;
        }
        
        return false;
    }

    public JFrame getWindowWithSPText() {
        if ( windowWithSPText == null ) {
            windowWithSPText = new JFrame();
            windowWithSPText.setDefaultCloseOperation( WindowConstants.HIDE_ON_CLOSE );
            windowWithSPText.add( SPTextView.getInstance() );
        }

        return windowWithSPText;
    }

    private void createUIComponents() {
        inputParametrs  = new JTable( new ParametrsModel() );
        outputParametrs = new JTable( new ParametrsModel() );
    }



    public static void main( String[] args ) {
        EventQueue.invokeLater( new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame( "MainForm" );
                frame.setContentPane( FirstForm.getInstance() );
                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                frame.setSize( FirstForm.getInstance().getSize() );
                frame.setVisible( true );
            }
        } );
    }

    public static JPanel getInstance(){
        if ( INSTANCE == null ) {
            INSTANCE = new FirstForm();
        }

        return INSTANCE.mainPanel;
    }
}

class ParametrsModel extends AbstractTableModel{

    String[] columnsName = { "Название", "Тип", "SQL-тип", "IN/OUT", "По умолчанию", "Для теста", "Переименовать в"};

    List<ParameterType> parameterTypes = new ArrayList<ParameterType>();

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
        switch ( columnIndex ){
            case 0:
                return parameterTypes.get( rowIndex ).getName();
            case 1:
                return parameterTypes.get( rowIndex ).getType();
            case 2:
                return parameterTypes.get( rowIndex ).getSqlType();
            case 3:
                return parameterTypes.get( rowIndex ).getInOut();
            case 4:
                return parameterTypes.get( rowIndex ).getDefaultValue();
            case 5:
                return parameterTypes.get( rowIndex ).getTestValue();
            case 6:
                return parameterTypes.get( rowIndex ).getRenameTo();
            default:
                throw new RuntimeException( "Ошибка при работе с таблицей" );
        }
    }

    @Override
    public void setValueAt( Object aValue, int rowIndex, int columnIndex ) {
        switch ( columnIndex ){
            case 0:
                parameterTypes.get( rowIndex ).setName( ( String ) aValue );
                break;
            case 1:
//                parameterTypes.get( rowIndex ).setType();
                break;
            case 2:
                parameterTypes.get( rowIndex ).setSqlType( ( String ) aValue );
                break;
            case 3:
//                parameterTypes.get( rowIndex ).getInOut();
                break;
            case 4:
                parameterTypes.get( rowIndex ).setDefaultValue( ( String ) aValue );
                break;
            case 5:
                parameterTypes.get( rowIndex ).setTestValue( ( String ) aValue );
                break;
            case 6:
                parameterTypes.get( rowIndex ).setRenameTo( ( String ) aValue );
            default:
                throw new RuntimeException( "Ошибка при работе с таблицей" );
        }
    }
}