package com.kreig133.daogenerator.gui;

import com.kreig133.daogenerator.DaoGenerator;
import com.kreig133.daogenerator.JaxbHandler;
import com.kreig133.daogenerator.WikiGenerator;
import com.kreig133.daogenerator.common.SourcePathChangeListener;
import com.kreig133.daogenerator.common.TypeChangeListener;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.settings.OperationSettings;
import com.kreig133.daogenerator.db.StoreProcedureInfoExtractor;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.jaxb.*;
import com.kreig133.daogenerator.settings.Settings;
import com.kreig133.daogenerator.sql.SqlQueryParser;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author eshangareev
 * @version 1.0
 */
public class Form  implements TypeChangeListener, SourcePathChangeListener{
    private static Form INSTANCE;

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
    private JCheckBox isMultipleResultCheckBoxSpTab;
    private JCheckBox isMultipleResultCheckBoxSelectTab;
    private JEditorPane queryTextArea;
    private JTextField methodNameFieldSelectTab;
    private JTextArea commentTextAreaSelectTab;
    private JTabbedPane tabbedPane1;
    private JPanel developer;
    private JButton generateWikiButton;
    private JTextPane log;
    private JTextField sourceDirTextField;
    private JButton setSourceDirButton;
    private JTextField destDirTextField;
    private JButton setDestDirButton;
    private JTextField entityPackageTextField;
    private JTextField daoPackageTextField;
    private JTextField mappingPackageTextField;
    private JButton startButton;
    private JRadioButton IASKRadioButton;
    private JRadioButton DEPORadioButton;
    private JTextField modelPackageTextField;
    private JLabel modelPackageLabel;
    private JLabel daoPackageLabel;
    private JFrame windowWithSPText;
    private static final int SP_TAB_INDEX = 0;

    private boolean start = true;

    private String tempOperationName;

    private final JFileChooser fileChooser = GuiUtils.getFileChooser();
    
    public Form() {
        redirectOutAndErrOutputToGui();

        initializingAnalyticTab();
        initializingDeveloperTab();

        loadSettings();

        Settings.settings().addTypeChangeListener( this );
        Settings.settings().addSourcePathChangeListener( this );
    }

    private void initializingAnalyticTab() {
        queryTextArea.setContentType( "text/sql" );

        getInParamsButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                List<ParameterType> inputParametrs;

                if ( tabbedPane.getSelectedIndex() == SP_TAB_INDEX ) {
                    if ( checkSPName() ) return;

                    methodNameFieldSpTab.setText( Utils.convertPBNameToName( storeProcedureField.getText() ) );

                    inputParametrs =
                            StoreProcedureInfoExtractor.getInputParametrsForSP( storeProcedureField.getText() );

                    SPTextButton.setEnabled( true );
                    getOutParamsButton.setEnabled( true );

                } else {
                    if ( queryTextArea.getText() == null || queryTextArea.getText().equals( "" ) ){
                        JOptionPane.showMessageDialog( getInParamsButton, "Введите текст запроса" );
                    }

                    inputParametrs = SqlQueryParser.parseSqlQueryAndParameters( getCurrentDaoMethod() )
                                        .getInputParametrs().getParameter();

                    final boolean isSelect = getQueryType() == SelectType.SELECT;

                    getOutParamsButton.setEnabled( isSelect );
                    generateXMLButton.setEnabled( ! isSelect );
                }
                updateInputParameters( inputParametrs );

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
                final DaoMethod daoMethod  = getCurrentDaoMethod().getSelectType().
                        getOutputParameterExtractor().getOutputParameters( getCurrentDaoMethod() );

                updateOutputParameters( daoMethod.getOutputParametrs().getParameter() );
                updateInputParameters( daoMethod.getInputParametrs().getParameter() );

                generateXMLButton.setEnabled( true );
            }
        } );

        generateXMLButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                if ( fileChooser.showSaveDialog( mainPanel ) == JFileChooser.APPROVE_OPTION ) {
                    final File dirForSave = fileChooser.getSelectedFile();
                    final DaoMethod currentDaoMethods = getCurrentDaoMethod();

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

        generateWikiButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                try {
                    WikiGenerator.main( null );
                } catch ( IOException e1 ) {
                    e1.printStackTrace();
                }
            }
        } );
    }

    private void initializingDeveloperTab() {

        IASKRadioButton.addChangeListener( new ChangeListener() {
            @Override
            public void stateChanged( ChangeEvent e ) {
                Settings.settings().setType( IASKRadioButton.isSelected() ? Type.IASK : Type.DEPO );
            }
        } );

        setSourceDirButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                setSourcePath();
            }
        } );

        setDestDirButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                setOutputPath();
            }
        } );

        startButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                if( validateBeforeStartGenerateJavaClasses() ){
                    saveSettings();
                    DaoGenerator.doAction();
                }
            }
        } );

        sourceDirTextField.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                updateSourcePath();
            }
        } );

        sourceDirTextField.addFocusListener( new FocusListener() {
            @Override
            public void focusGained( FocusEvent e ) { /* do nothing */ }

            @Override
            public void focusLost( FocusEvent e ) {
                updateSourcePath();
            }
        } );
    }

    private void redirectOutAndErrOutputToGui() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(final int b) throws IOException {
                updateTextPane(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                updateTextPane(new String(b, off, len));
            }

            @Override
            public void write(byte[] b) throws IOException {
                write(b, 0, b.length);
            }
        };

        System.setOut(new PrintStream(out, true));
        System.setErr( new PrintStream( out, true ) );
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

    private DaoMethod getCurrentDaoMethod(){
        final DaoMethod result = new DaoMethod();

        result.setCommon( new CommonType() );
        result.getCommon().setConfiguration( new ConfigurationType() );

        if( tabbedPane.getSelectedIndex() == SP_TAB_INDEX ){
            result.getCommon().setSpName( storeProcedureField.getText() );
            result.getCommon().setMethodName( methodNameFieldSpTab.getText() );
            result.getCommon().setComment( commentTextAreaSpTab.getText() );
            result.getCommon().getConfiguration().setType( SelectType.CALL );
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

    private void updateTextPane(final String text) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                Document doc = log.getDocument();
                try {
                    doc.insertString( doc.getLength(), text, null );
                } catch ( BadLocationException e ) {
                    throw new RuntimeException( e );
                }
                log.setCaretPosition( doc.getLength() - 1 );
            }
        } );
    }

    private void loadSettings() {
        final boolean iask = Settings.settings().getType() == Type.IASK;
        IASKRadioButton.setSelected(    iask );
        DEPORadioButton.setSelected( !  iask );

        modelPackageTextField.setVisible( ! iask );
        modelPackageLabel    .setVisible( ! iask );
        daoPackageLabel      .setVisible(   iask );
        daoPackageTextField  .setVisible(   iask );

        destDirTextField.setText( Settings.settings().getOutputPathForJavaClasses() );
        entityPackageTextField.setText( Settings.settings().getEntityPackage() );
        daoPackageTextField.setText( Settings.settings().getDaoPackage() );
        mappingPackageTextField.setText( Settings.settings().getMapperPackage() );
        modelPackageTextField   .setText( Settings.settings().getModelPackage() );

        if ( start ) {
            sourceDirTextField.setText( Settings.settings().getSourcePath() );
            start = false;
        }
    }

    private void saveSettings() {

        Settings.settings().setOutputPathForJavaClasses   ( destDirTextField              .getText    () );
        Settings.settings().setSourcePath                 ( sourceDirTextField            .getText    () );
        Settings.settings().setDaoPackage                 ( daoPackageTextField           .getText    () );
        Settings.settings().setEntityPackage              ( entityPackageTextField        .getText    () );
        Settings.settings().setMapperPackage              ( mappingPackageTextField       .getText    () );
        Settings.settings().setModelPackage               ( modelPackageTextField         .getText    () );
        Settings.settings().setOperationName              ( tempOperationName == null ?
                new File( sourceDirTextField.getText()).getName() : tempOperationName );
    }

    private boolean validateBeforeStartGenerateJavaClasses() {
        if (
                (   IASKRadioButton.isSelected() &&   DEPORadioButton.isSelected() )  ||
                ( ! IASKRadioButton.isSelected() && ! DEPORadioButton.isSelected() )
        ){
            JOptionPane.showMessageDialog( mainPanel, "Выберите один (!) тип проекта." );
            return false;
        }

        if(
                ( ! isPackageName( daoPackageTextField.getText() ) ) ||
                ( ! isPackageName( entityPackageTextField   .getText() ) ) ||
                ( ! isPackageName( mappingPackageTextField  .getText() ) )
        ){
            JOptionPane.showMessageDialog( mainPanel, "Одно или несколкьо имен пакетов не прошло валидацию." );
            return false;
        }

        //TODO надо бы проверить пути

        return true;
    }

    private boolean isPackageName( String packageName ){
        return Pattern.compile( "[\\w\\d]+(\\.[\\w\\d]+)+" ).matcher( packageName ).matches();
    }

    private void setOutputPath() {
        int returnVal = fileChooser.showSaveDialog( null );

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            destDirTextField.setText( file.getAbsolutePath() );
        }
    }


    private void setSourcePath() {
        if ( fileChooser.showOpenDialog( mainPanel ) == JFileChooser.APPROVE_OPTION ) {
            File file           = fileChooser.getSelectedFile();
            tempOperationName   = file.getName();
            sourceDirTextField.setText( file.getAbsolutePath() );
            updateSourcePath();
        }
    }

    private void updateSourcePath() {
        Settings.settings().setSourcePath( sourceDirTextField.getText() );
    }

    public static JPanel getInstance(){
        if ( INSTANCE == null ) {
            INSTANCE = new Form();
        }

        return INSTANCE.mainPanel;
    }

    @Override
    public void typeChanged() {
        loadSettings();
    }

    @Override
    public void sourcePathChanged() {
        loadSettings();
    }
}