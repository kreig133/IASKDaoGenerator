package com.kreig133.daogenerator.gui;

import com.kreig133.daogenerator.common.SourcePathChangeListener;
import com.kreig133.daogenerator.db.extractors.in.SpInputParameterExtractor;
import com.kreig133.daogenerator.jaxb.*;
import com.kreig133.daogenerator.presenter.FormInterface;
import com.kreig133.daogenerator.presenter.FormPresenter;
import com.kreig133.daogenerator.presenter.PresenterException;
import com.kreig133.daogenerator.settings.Settings;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.kreig133.daogenerator.gui.GuiUtils.getNewFileChooser;

/**
 * @author eshangareev
 * @version 1.0
 */
public class Form  implements SourcePathChangeListener, FormInterface{
    private static Form INSTANCE;
    private static final String WARNING_DIALOG_TITLE = "Голактего в опастносте!!11один";
    private static final String ATTENTION = "Говорит DaoGenerator:";

    private JPanel mainPanel;
    private JTable inputParametrs;
    private JTable outputParametrs;
    private JButton getInParamsButton;
    private JButton SPTextButton;
    private JButton generateXMLButton;
    private JButton getOutParamsButton;
    private JCheckBox isMultipleResultCheckBox;
    private JEditorPane queryTextArea;
    private JTextField methodNameField;
    private JTextArea commentTextArea;
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
    private JLabel daoPackageLabel;
    private JTextField textField1;
    private JButton button1;
    private JLabel entityPackageLable;
    private JButton prepareQueryButton;
    private JRadioButton singleQueryRadioButton;
    private JRadioButton doubleQueryRadioButton;
    private JEditorPane secondQuery;
    private JScrollPane secondQueryPanel;
    private JFrame windowWithText;

    private boolean start = true;

    private FormPresenter presenter;

    private Form(FormPresenter presenter) {

        this.presenter = presenter;
        redirectOutAndErrOutputToGui();

        initializingAnalyticTab();
        initializingDeveloperTab();

        loadSettings();

        Settings.settings().addSourcePathChangeListener( this );
    }

    private void initializingAnalyticTab() {

        queryTextArea.setContentType( "text/sql" );
        secondQuery.setContentType( "text/sql" );
        inputParametrs.getTableHeader().setReorderingAllowed( false );
        outputParametrs.getTableHeader().setReorderingAllowed( false );

        singleQueryRadioButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                secondQueryPanel.setVisible(!singleQueryRadioButton.isSelected());
                ((JSplitPane) secondQueryPanel.getParent()).setDividerLocation(0.5);
                secondQueryPanel.getParent().repaint();
            }
        });

        prepareQueryButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                prepareQueryButtonListener();
            }
        } );

        getInParamsButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                getInParamsButtonListener();
            }
        } );

        SPTextButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                SPTextButtonListener();
            }
        } );

        getOutParamsButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                getOutParamsButtonListener();
            }
        } );

        generateXMLButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                generateXMLButtonListener();
            }
        } );

        generateWikiButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                generateWikiButtonListener();
            }
        } );
    }

    private void prepareQueryButtonListener(){
        try {
        presenter.prepareQuery();
        }
        catch (PresenterException ex) {
            JOptionPane.showMessageDialog( mainPanel, ex.getMessage(),
                    WARNING_DIALOG_TITLE, JOptionPane.WARNING_MESSAGE );
        }
    }

    private void getInParamsButtonListener(){
        try {
            updateInputParameters( presenter.getInParameters());
            setButtonsStateAfterGetInParams();
        }
        catch (PresenterException ex){
            JOptionPane.showMessageDialog( getInParamsButton, ex.getMessage());
        }
    }

    private void setButtonsStateAfterGetInParams(){
        SPTextButton.setEnabled( presenter.getIsSpCall() );
        getOutParamsButton.setEnabled( presenter.getIsSpCall() || presenter.getIsSelect() );
        generateXMLButton.setEnabled( ! ( presenter.getIsSpCall() || presenter.getIsSelect() ) );
    }

    private void SPTextButtonListener(){
        presenter.getSPText();
    }

    private void getOutParamsButtonListener(){
        try {
            updateOutputParameters( presenter.getOutParameters() );
            generateXMLButton.setEnabled( true );
        }
        catch (PresenterException ex){
            JOptionPane.showMessageDialog( mainPanel, ex.getMessage(),
                    WARNING_DIALOG_TITLE, JOptionPane.WARNING_MESSAGE );
        }
    }

    private void generateXMLButtonListener(){
        try {
            presenter.generateXML();
        }
        catch (PresenterException ex){
            JOptionPane.showMessageDialog( mainPanel, ex.getMessage(),
                    WARNING_DIALOG_TITLE, JOptionPane.WARNING_MESSAGE );
        }
    }

    private void generateWikiButtonListener(){
        try {
            presenter.generateWiki();
        }
        catch (PresenterException ex){
            JOptionPane.showMessageDialog( mainPanel, ex.getMessage(),
                    WARNING_DIALOG_TITLE, JOptionPane.WARNING_MESSAGE );
        }
    }

    private void initializingDeveloperTab() {
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
                startButtonListener();
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

    private void startButtonListener(){
        try{
            if( presenter.validateBeforeStartGenerateJavaClasses() ){
                saveSettings();
                log.setText("");
                tabbedPane1.setSelectedIndex(3);
                presenter.startProjectGenerating();
            }
        } catch (PresenterException ex){
            JOptionPane.showMessageDialog( mainPanel, ex.getMessage(),
                    WARNING_DIALOG_TITLE, JOptionPane.WARNING_MESSAGE );
        }
    }

    /**
     * Отображает диалоговое окно с ошибкой
     * @param text текст ошибки
     */
    private void showError(String text) {
    	JOptionPane.showMessageDialog( mainPanel, text, WARNING_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE );
    }
    
    /**
     * Перенаправляем стандартный вывод сообщений и ошибок на панель
     */
    private void redirectOutAndErrOutputToGui() {
        OutputStream out = new OutputStream() {
            @Override
            public void write( final int b ) throws IOException {
                updateTextPane( String.valueOf( ( char ) b ) );
            }

            @Override
            public void write( byte[] b, int off, int len ) throws IOException {
                updateTextPane( new String( b, off, len ) );
            }

            @Override
            public void write( @NotNull byte[] b ) throws IOException {
                write( b, 0, b.length );
            }
        };

        System.setOut( new PrintStream( out, true ) );
        System.setErr(new PrintStream(out, true));
    }

    private void updateOutputParameters( List<ParameterType> inputParametrsForSP ) {
        updateTable( outputParametrs, inputParametrsForSP );
    }

    private void updateInputParameters( List<ParameterType> inputParametrsForSP ) {
        updateTable( inputParametrs, inputParametrsForSP );
    }

    private void updateTable( @NotNull JTable table, List<ParameterType> inputParametrsForSP ) {
        final List<ParameterType> parameterTypes =
                ( (ParametrsModel) ( table.getModel() ) ).getParameterTypes();
        parameterTypes.clear();
        parameterTypes.addAll( inputParametrsForSP );
        table.updateUI();
    }

    JFrame getWindowWithText() {
        if ( windowWithText == null ) {
            windowWithText = new JFrame();
            windowWithText.setSize( 1024, 700 );
            windowWithText.setDefaultCloseOperation( WindowConstants.HIDE_ON_CLOSE );
            windowWithText.add( TextView.getInstance() );
        }
        return windowWithText;
    }

    private void createUIComponents() {
        inputParametrs  = new JTable( new ParametrsModel() );
        outputParametrs = new JTable( new ParametrsModel() );
    }

    private void updateTextPane( final String text ) {
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
        destDirTextField.setText( Settings.settings().getOutputPathForJavaClasses() );
        entityPackageTextField.setText( Settings.settings().getEntityPackage() );
        daoPackageTextField.setText( Settings.settings().getDaoPackage() );
        mappingPackageTextField.setText( Settings.settings().getMapperPackage() );

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
    }

    private void setOutputPath() {
        JFileChooser newFileChooser = getNewFileChooser();
        if ( newFileChooser.showSaveDialog( mainPanel ) == JFileChooser.APPROVE_OPTION ) {
            File file = newFileChooser.getSelectedFile();
            destDirTextField.setText( file.getAbsolutePath() );
        }
    }


    private void setSourcePath() {
        JFileChooser newFileChooser = getNewFileChooser();
        if ( newFileChooser.showOpenDialog( mainPanel ) == JFileChooser.APPROVE_OPTION ) {
            File file           = newFileChooser.getSelectedFile();
            sourceDirTextField.setText( file.getAbsolutePath() );
            updateSourcePath();
        }
    }

    private void updateSourcePath() {
        Settings.settings().setSourcePath( sourceDirTextField.getText() );
    }

    public synchronized static JPanel getMainPanel(FormPresenter presenter){
        if ( INSTANCE == null ) {
            INSTANCE = new Form(presenter);
        }
        return INSTANCE.mainPanel;
    }

    public static Form getInstance(FormPresenter presenter){
        if ( INSTANCE == null ) {
            INSTANCE = new Form(presenter);
        }
        return INSTANCE;
    }

    @Override
    public void sourcePathChanged() {
        loadSettings();
    }

    @Override
    public boolean getIsMultipleResult() {
        return isMultipleResultCheckBox.isSelected();
    }

    @Override
    public void setIsMultipleResult(boolean value) {
        isMultipleResultCheckBox.setSelected(value);
    }

    @Override
    public boolean getSingleQueryChecked() {
        return  singleQueryRadioButton.isSelected();
    }

    @Override
    public void setSingleQuery(boolean value) {
        singleQueryRadioButton.setSelected(value);
    }

    @Override
    public boolean getDoubleQueryChecked() {
        return doubleQueryRadioButton.isSelected();
    }

    @Override
    public void setDoubleQuery(boolean value) {
        doubleQueryRadioButton.setSelected(value);
    }

    @Override
    public String getQueryTextArea() {
        return queryTextArea.getText();
    }

    @Override
    public void setQueryTextArea(String text) {
        queryTextArea.setText(text);
    }

    @Override
    public String getSecondQuery() {
        return secondQuery.getText();
    }

    @Override
    public void setSecondQuery(String text) {
        secondQuery.setText(text);
    }

    @Override
    public String getMethodName() {
        return methodNameField.getText();
    }

    @Override
    public void setMethodName(String name) {
        methodNameField.setText(name);
    }

    @Override
    public String getCommentText() {
        return commentTextArea.getText();
    }

    @Override
    public void setCommentText(String text) {
        commentTextArea.setText(text);
    }

    @Override
    public ParametrsModel getInputParametersTypes() {
        return (ParametrsModel)(inputParametrs.getModel());
    }

    @Override
    public ParametrsModel getOutputParametersTypes() {
        return (ParametrsModel)(outputParametrs.getModel());
    }

    @Override
    public String getDirForSave() {
        JFileChooser newFileChooser = getNewFileChooser();
        if ( newFileChooser.showSaveDialog( mainPanel ) == JFileChooser.APPROVE_OPTION ) {
            final File dirForSave = newFileChooser.getSelectedFile();
            return dirForSave.getAbsolutePath();
        }
        return null;
    }

    @Override
    public String getFileForSave() {
        final JFileChooser newFileChooser = getNewFileChooser();
        if ( newFileChooser.showOpenDialog( generateWikiButton ) == JFileChooser.APPROVE_OPTION ) {
            return newFileChooser.getSelectedFile().getAbsolutePath();
        }
        else throw PresenterException.warn("Файл не выбран!");
    }

    @Override
    public void showWindowWithText(String text){
        TextView.setText(text);
        getWindowWithText().setVisible(true);
    }

    @Override
    public String getMappingPackageText() {
        return mappingPackageTextField.getText();
    }

    @Override
    public void setMappingPackageText(String text) {
        mappingPackageTextField.setText(text);
    }

    @Override
    public String getEntityPackageText() {
        return entityPackageTextField.getText();
    }

    @Override
    public void setEntityPackageTextField(String text) {
        entityPackageTextField.setText(text);
    }

    @Override
    public String getDaoPackageText() {
        return daoPackageTextField.getText();
    }

    @Override
    public void setDaoPackageTextField(String text) {
        daoPackageTextField.setText(text);
    }

    @Override
    public String getDestDirText() {
        return destDirTextField.getText();
    }

    @Override
    public void setDestDirText(String text) {
        destDirTextField.setText(text);
    }

    @Override
    public boolean showWarningDialog(String message) {
        if( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
                mainPanel, message,
                WARNING_DIALOG_TITLE, JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE )
                ) return true;
        else return false;
    }


    @Override
    public boolean showAttentionDialog(String message) {
        if( JOptionPane.showConfirmDialog( mainPanel, message,
                ATTENTION, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE ) == JOptionPane.YES_OPTION
                ) return true;
        else return false;
    }

    @Override
    public void lockGui() {
        tabbedPane1.setEnabled( false );
    }

    @Override
    public void unlockGui() {
        tabbedPane1.setEnabled( true );
    }
}