package com.kreig133.daogenerator.gui;

import com.kreig133.daogenerator.DaoGenerator;
import com.kreig133.daogenerator.JaxbHandler;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.db.GetOutputParametersFromResultSet;
import com.kreig133.daogenerator.db.StoreProcedureInfoExtractor;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.jaxb.*;
import com.kreig133.daogenerator.sql.SqlQueryParser;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class FirstForm {
    private static FirstForm INSTANCE;

    private JTextField storeProcedureField;
    private JPanel mainPanel;
    private JTable inputParametrs;
    private JTable outputParametrs;
    private JButton getInParamsButton;
    private JButton SPTextButton;
    private JButton generateXMLButton;
    private JButton getOutParamsButton;
    private JTabbedPane tabbedPane;
    private JTextField methodNameFieldSpTab;
    private JTextArea commentTextAreaSpTab;
    private JRadioButton GENERATERadioButton;
    private JRadioButton CALLRadioButton;
    private JRadioButton GENEROUTRadioButton;
    private JCheckBox isMultipleResultCheckBoxSpTab;
    private JCheckBox isMultipleResultCheckBoxSelectTab;
    private JTextArea queryTextArea;
    private JTextField methodNameFieldSelectTab;
    private JTextArea commentTextAreaSelectTab;
    private ButtonGroup spTypeRadioGroup;
    private JFrame windowWithSPText;
    private static final int SP_TAB_INDEX = 0;

    private final JFileChooser fileChooserForXml = GuiUtils.getFileChooser();
    
    public FirstForm() {

        spTypeRadioGroup = new ButtonGroup();
        spTypeRadioGroup.add( GENERATERadioButton );
        spTypeRadioGroup.add( CALLRadioButton );
        spTypeRadioGroup.add( GENEROUTRadioButton );

        getInParamsButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                List<ParameterType> inputParametrsForSP;
                if ( tabbedPane.getSelectedIndex() == SP_TAB_INDEX ) {
                    if ( checkSPName() ) return;

                    methodNameFieldSpTab.setText( Utils.convertPBNameToName( storeProcedureField.getText() ) );

                    inputParametrsForSP =
                            StoreProcedureInfoExtractor.getInputParametrsForSP( storeProcedureField.getText() );

                    SPTextButton.setEnabled( true );
                    getOutParamsButton.setEnabled( true );

                } else {
                    if ( queryTextArea.getText() == null || queryTextArea.getText().equals( "" ) ){
                        JOptionPane.showMessageDialog( getInParamsButton, "Введите текст запроса" );
                    }

                    inputParametrsForSP = SqlQueryParser.parseSqlQueryAndParameters( getCurrentDaoMethods() )
                                        .getInputParametrs().getParameter();

                    final boolean isSelect = getQueryType() == SelectType.SELECT;

                    getOutParamsButton.setEnabled( isSelect );
                    generateXMLButton.setEnabled( ! isSelect );
                }
                updateInputParameters( inputParametrsForSP );

                ( ( ParametrsModel ) ( outputParametrs.getModel() ) ).getParameterTypes().clear();
            }

        } );
        
        SPTextButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                if( checkSPName() ) return;

                SPTextView.setText( StoreProcedureInfoExtractor.getSPText() );

                getWindowWithSPText().setVisible( true );
            }
        } );

        getOutParamsButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                final DaoMethod daoMethod  = GetOutputParametersFromResultSet.getOutputParameters( getCurrentDaoMethods() );

                updateOutputParameters( daoMethod.getOutputParametrs().getParameter() );
                updateInputParameters( daoMethod.getInputParametrs().getParameter() );

                generateXMLButton.setEnabled( true );
            }
        } );

        generateXMLButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                if ( fileChooserForXml.showSaveDialog( mainPanel ) == JFileChooser.APPROVE_OPTION ) {
                    final File dirForSave = fileChooserForXml.getSelectedFile();
                    final DaoMethod currentDaoMethods = getCurrentDaoMethods();

                    JaxbHandler.marshallInFile(
                            new File(
                                    dirForSave.getAbsolutePath() + "/" +
                                            currentDaoMethods.getCommon().getMethodName() + ".xml"
                            ),
                            currentDaoMethods
                    );
                }
            }
        } );
    }

    private void updateOutputParameters( List<ParameterType> inputParametrsForSP ) {
        updateTable( outputParametrs, inputParametrsForSP );
    }

    private void updateTable( JTable table, List<ParameterType> inputParametrsForSP ) {
        final List<ParameterType> parameterTypes =
                ( ( ParametrsModel ) ( table.getModel() ) ).getParameterTypes();
        parameterTypes.clear();
        parameterTypes.addAll( inputParametrsForSP );
        table.updateUI();
    }


    private void updateInputParameters( List<ParameterType> inputParametrsForSP ) {
        updateTable( inputParametrs, inputParametrsForSP );
    }

    private DaoMethod getCurrentDaoMethods(){
        final DaoMethod result = new DaoMethod();

        result.setCommon( new CommonType() );
        result.getCommon().setConfiguration( new ConfigurationType() );

        if( tabbedPane.getSelectedIndex() == SP_TAB_INDEX ){
            result.getCommon().setSpName( storeProcedureField.getText() );
            result.getCommon().setMethodName( methodNameFieldSpTab.getText() );
            result.getCommon().setComment( commentTextAreaSpTab.getText() );
            result.getCommon().getConfiguration().setType( getSpType() );
            result.getCommon().getConfiguration().setMultipleResult( isMultipleResultCheckBoxSpTab.isSelected() );
        } else{
            result.getCommon().setMethodName( methodNameFieldSelectTab.getText() );
            result.getCommon().setComment( commentTextAreaSelectTab.getText() );
            result.getCommon().setQuery( queryTextArea.getText() );
            result.getCommon().getConfiguration().setType( getQueryType() );
            result.getCommon().getConfiguration().setMultipleResult( isMultipleResultCheckBoxSelectTab.isSelected() );
        }

        result.setInputParametrs( new ParametersType() );
        result.getInputParametrs().getParameter().clear();
        result.getInputParametrs().getParameter().addAll(
                ( (ParametrsModel) ( inputParametrs.getModel() ) ).getParameterTypes()
        );

        result.setOutputParametrs( new ParametersType() );
        result.getOutputParametrs().getParameter().addAll(
                ( (ParametrsModel) ( outputParametrs.getModel() ) ).getParameterTypes()
        );

        return result;
    }

    private SelectType getQueryType() {
        if ( tabbedPane.getSelectedIndex() == SP_TAB_INDEX ) {
            return SelectType.CALL;
        }

        final String firstWord = queryTextArea.getText().trim().split( "\\s" )[0];

        return SelectType.getByName( firstWord );
    }

    private SelectType getSpType() {
        for ( Enumeration e = spTypeRadioGroup.getElements(); e.hasMoreElements(); ) {
            JRadioButton b = ( JRadioButton ) e.nextElement();
            if ( b.getModel() == spTypeRadioGroup.getSelection() ) {
                return SelectType.getByName( b.getText() );
            }
        }
        return null;
    }

    private boolean checkSPName( ) {
        String text = storeProcedureField.getText();
        
        if( text == null || "".equals( text )  ){
            JOptionPane.showMessageDialog( getInParamsButton, "Введите название хранимой процедуры" );
            return true;
        }
        
        return false;
    }

    public JFrame getWindowWithSPText() {
        if ( windowWithSPText == null ) {
            windowWithSPText = new JFrame();
            windowWithSPText.setSize( 400, 700 );
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
        DaoGenerator.getCurrentOperationSettings().setType( Type.DEPO );
        EventQueue.invokeLater( new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
                JFrame frame = new JFrame( "MainForm" );
                frame.setContentPane( FirstForm.getInstance() );
                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                frame.setSize( 800, 600 );
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

class ParametrsModel extends AbstractTableModel {

    static String[] columnsName = {
            "№", "Название", "Тип", "SQL-тип", "IN/OUT", "По умолчанию",
             "Для теста", "Переименовать в", "JDBC-тип", "Комментарий"
    };

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
    public void setValueAt( Object aValue, int rowIndex, int columnIndex ) {
        switch ( columnIndex ) {
            case 1:
                parameterTypes.get( rowIndex ).setName( ( String ) aValue );
                break;
            case 2:
//                parameterTypes.get( rowIndex ).setType();
                break;
            case 3:
                parameterTypes.get( rowIndex ).setSqlType( ( String ) aValue );
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
                parameterTypes.get( rowIndex ).setRenameTo( ( String ) aValue );
                break;
            case 8:
                break;
            case 9:
                parameterTypes.get( rowIndex ).setComment( ( String ) aValue );
                break;
        }
    }

    @Override
    public boolean isCellEditable( int rowIndex, int columnIndex ) {
        return true;
    }
}

