package com.kreig133.daogenerator.gui;

import static com.kreig133.daogenerator.gui.GuiUtils.getNewFileChooser;

import com.kreig133.daogenerator.JaxbHandler;
import com.kreig133.daogenerator.MavenProjectGenerator;
import com.kreig133.daogenerator.WikiGenerator;
import com.kreig133.daogenerator.common.SourcePathChangeListener;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.db.extractors.Extractor;
import com.kreig133.daogenerator.db.extractors.in.InputParameterExtractor;
import com.kreig133.daogenerator.db.extractors.in.SpInputParameterExtractor;
import com.kreig133.daogenerator.db.extractors.out.OutputParameterExtractor;
import com.kreig133.daogenerator.db.preparators.DoubleQueryPreparator;
import com.kreig133.daogenerator.db.preparators.QueryPreparator;
import com.kreig133.daogenerator.files.PackageAndFileUtils;
import com.kreig133.daogenerator.files.builder.FileBuilder;
import com.kreig133.daogenerator.jaxb.CommonType;
import com.kreig133.daogenerator.jaxb.ConfigurationType;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.jaxb.ParametersType;
import com.kreig133.daogenerator.jaxb.ParentType;
import com.kreig133.daogenerator.jaxb.SelectType;
import com.kreig133.daogenerator.jaxb.validators.DaoMethodValidator;
import com.kreig133.daogenerator.settings.Settings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * @author eshangareev
 * @version 1.0
 */
public class Form  implements SourcePathChangeListener{
    private static Form INSTANCE;
    private static final String WARNING_DIALOG_TITLE = "Предупреждение";
    private static final String ATTENTION = "Сообщение";

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
    private JButton parentSpTextButton;
    private JRadioButton singleQueryRadioButton;
    private JRadioButton doubleQueryRadioButton;
    private JEditorPane secondQuery;
    private JScrollPane secondQueryPanel;
    private JComboBox outputParamsType;
    private JComboBox inputParamsType;
    private JButton clearButton;
    private JFrame windowWithText;

    private boolean start = true;

    private Form() {
        redirectOutAndErrOutputToGui();

        initializingAnalyticTab();
        initializingDeveloperTab();

        loadSettings();

        Settings.settings().addSourcePathChangeListener( this );
    }

    private void initializingAnalyticTab() {
        outputParamsType.setModel(
                new DefaultComboBoxModel( new ParentType[] { ParentType.DEFAULT, ParentType.CATALOGUE } ) );
        inputParamsType.setModel(
                new DefaultComboBoxModel( new ParentType[] { ParentType.DEFAULT, ParentType.SAVED_QUERY } ) );

        queryTextArea.setContentType( "text/sql" );
        secondQuery.setContentType( "text/sql" );
        inputParametrs.getTableHeader().setReorderingAllowed( false );
        outputParametrs.getTableHeader().setReorderingAllowed( false );

        singleQueryRadioButton.addChangeListener( new ChangeListener() {
            @Override
            public void stateChanged( ChangeEvent e ) {
                secondQueryPanel.setVisible( ! singleQueryRadioButton.isSelected() );
                ( ( JSplitPane ) secondQueryPanel.getParent() ).setDividerLocation( 0.5 );
                secondQueryPanel.getParent().repaint();
            }
        } );
        prepareQueryButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                if ( determineQueryType() == SelectType.CALL ) {
                    JOptionPane.showMessageDialog( mainPanel, "Предварительная обработка для ХП не требуется!",
                            WARNING_DIALOG_TITLE, JOptionPane.WARNING_MESSAGE );
                    return;
                }
                String preparedQuery;
                if ( doubleQueryRadioButton.isSelected() ) {
                    preparedQuery = DoubleQueryPreparator.instance().prepareQuery( queryTextArea.getText(),
                            secondQuery.getText() );
                } else {
                    preparedQuery = QueryPreparator.instance().prepareQuery( queryTextArea.getText() );
                }
                queryTextArea.setText( preparedQuery );
            }
        } );
        getInParamsButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                if ( StringUtils.isBlank( queryTextArea.getText() ) ) {
                    JOptionPane.showMessageDialog( getInParamsButton,
                            "Введите текст запроса или название хранимой процедуры" );
                    return;
                }
                DaoMethod currentDaoMethod = getCurrentDaoMethod();
                final boolean isSpCall = currentDaoMethod.getSelectType() == SelectType.CALL;
                final boolean isSelect = currentDaoMethod.getSelectType() == SelectType.SELECT;

                SPTextButton.setEnabled( isSpCall );
                parentSpTextButton.setEnabled( isSpCall );
                getOutParamsButton.setEnabled( isSpCall || isSelect );
                generateXMLButton.setEnabled( ! ( isSelect || isSpCall ) );
                DaoMethod daoMethod = InputParameterExtractor.getInstance( currentDaoMethod )
                        .extractInputParams( currentDaoMethod );

                updateInputParameters( daoMethod.getInputParametrs().getParameter() );
                updateOutputParameters( new ArrayList<ParameterType>() );
            }
        } );
        SPTextButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                TextView.setText( SpInputParameterExtractor.getSPText() );
                getWindowWithText().setVisible( true );
            }
        } );
        parentSpTextButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                String spText = SpInputParameterExtractor.getParenSpText();
                if ( spText == null ) {
                    JOptionPane.showMessageDialog( mainPanel, "Хранимка не является оберткой!",
                    		ATTENTION , JOptionPane.INFORMATION_MESSAGE );
                    return;
                }
                TextView.setText( spText );

                getWindowWithText().setVisible( true );
            }
        } );
        getOutParamsButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                final DaoMethod daoMethod = OutputParameterExtractor.instance(
                        getCurrentDaoMethod().getSelectType()
                ).getOutputParameters( getCurrentDaoMethod() );

                updateOutputParameters( daoMethod.getOutputParametrs().getParameter() );
                updateInputParameters( daoMethod.getInputParametrs().getParameter() );

                generateXMLButton.setEnabled( true );
            }
        } );
        generateXMLButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                final DaoMethod currentDaoMethod = getCurrentDaoMethod();

                if ( ! validateBeforeGenerateXML( currentDaoMethod ) ) return;

                if ( ! DaoMethodValidator.checkDaoMethods( Arrays.asList( currentDaoMethod ), true ) ) {
                    if( JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(
                            mainPanel, "<html>Имеются ошибки! (сведения на вкладке Log)<br>Продолжить?",
                            WARNING_DIALOG_TITLE, JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE )
                    ){
                        return;
                    }
                }

                JFileChooser newFileChooser = getNewFileChooser();
                if ( newFileChooser.showSaveDialog( mainPanel ) == JFileChooser.APPROVE_OPTION ) {
                    final File dirForSave = newFileChooser.getSelectedFile();


                    final String xmlFilePath = dirForSave.getAbsolutePath() + "/" +
                            currentDaoMethod.getCommon().getMethodName() + ".xml";

                    JaxbHandler.marshallInFile( new File( xmlFilePath ), currentDaoMethod );

                    new Thread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        WikiGenerator.generateWikiForXmlFile( xmlFilePath,
                                                outputParamsType.getSelectedItem() == ParentType.CATALOGUE );

                                        TextView.setText(
                                                Utils.streamToString( new FileInputStream( xmlFilePath + ".txt" ) ) );

                                        getWindowWithText().setVisible( true );
                                    } catch ( IOException ex ) {
                                        ex.printStackTrace();
                                    } catch ( InterruptedException ex ) {
                                        ex.printStackTrace();
                                    }
                                }
                            }
                    ).start();
                }
            }
        } );
        generateWikiButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                final JFileChooser newFileChooser = getNewFileChooser();
                if ( newFileChooser.showOpenDialog( generateWikiButton ) == JFileChooser.APPROVE_OPTION ) {

                    new Thread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        WikiGenerator.generateWiki(
                                                newFileChooser.getSelectedFile().getAbsolutePath() );
                                    } catch ( IOException ex ) {
                                        ex.printStackTrace();
                                    } catch ( InterruptedException ex ) {
                                        ex.printStackTrace();
                                    }
                                }
                            }
                    ).start();
                }
            }
        } );
    }

    private boolean validateBeforeGenerateXML( DaoMethod currentDaoMethod ) {
        if ( StringUtils.isBlank( methodNameField.getText() ) ) {
            JOptionPane.showMessageDialog( mainPanel,
                    "Необходимо ввести название метода!",
                    WARNING_DIALOG_TITLE,
                    JOptionPane.WARNING_MESSAGE
            );
            return false;
        }
        if(
                currentDaoMethod.getInputParametrs ().getParent() == ParentType.SAVED_QUERY &&
                currentDaoMethod.getOutputParametrs().getParent() == ParentType.CATALOGUE
        ){
            JOptionPane.showMessageDialog( mainPanel,
                    "Неправильное сочетание типов входных и выходных параметров",
                    WARNING_DIALOG_TITLE,
                    JOptionPane.WARNING_MESSAGE
            );
            return false;
        }
        return true;
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
                if( validateBeforeStartGenerateJavaClasses() ){
                    saveSettings();
                    log.setText( "" );
                    tabbedPane1.setSelectedIndex( 3 );
                    //TODO
                    new Thread(
                        new Runnable(){
                            @Override
                            public void run() {
                                try{
                                    tabbedPane1.setEnabled( false );
                                    if( ! FileBuilder.generateJavaCode() ) return;
                                    int status = MavenProjectGenerator.installProject();
                                    if( status == 0 ) {
                                    	// TODO (Marat Fayzullin) Так как пока не реализовано, то нефиг мучать пользователя дополнительными вопросами
                                        /*if (
                                                JOptionPane.showConfirmDialog(
                                                        mainPanel,
                                                        "Тестирование успешно завершено.\nСкопировать файлы в проект?"
                                                ) == JOptionPane.OK_OPTION
                                        ) {
                                            JOptionPane.showMessageDialog(
                                                    mainPanel,
                                                    "Функциональность еще не реализована"
                                            );
                                        }*/
                                    } else {
                                        JOptionPane.showMessageDialog(
                                                mainPanel,
                                                "Тестирование провалилось.\nИзмените входные данные или сообщите " +
                                                        "разработчикам о возникшей проблеме"
                                                );
                                    }
                                } finally {
                                    tabbedPane1.setEnabled( true );
                                }
                            }
                        }
                    ).start();
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
        clearButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
            	checkAndClearTargetFolder();
            }
        } );
    }

    /**
     * Проверяет и удаляет папку с ранее сформированными проектными файлами
     */
    private boolean checkAndClearTargetFolder() {
    	String folderName = destDirTextField.getText();
    	File directory = new File(folderName);

    	if (directory.exists()) {
    		if (!directory.isDirectory() ) {
    			showError("Указанное в поле значение не является папкой!");
    			return false;
            }
    	
    		if (JOptionPane.showConfirmDialog(
                    mainPanel, ( String.format("Указанная папка \"%s\" существует.\nУдалить её?", folderName)),
                    ATTENTION, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE ) == JOptionPane.YES_OPTION
                ) {
    				if (!PackageAndFileUtils.removeDirectory( directory ) ) {
    					showError("При удалении папки возникли ошибки. Проверьте, что она не используется другими программами.");
    					return false;
    				}
    		}
        }
    	return true;
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
        System.setErr( new PrintStream( out, true ) );
    }

    private void updateOutputParameters( List<ParameterType> inputParametrsForSP ) {
        updateTable( outputParametrs, inputParametrsForSP );
    }

    private void updateTable( @NotNull JTable table, List<ParameterType> inputParametrsForSP ) {
        final List<ParameterType> parameterTypes =
                ( ( ParametrsModel ) ( table.getModel() ) ).getParameterTypes();
        parameterTypes.clear();
        parameterTypes.addAll( inputParametrsForSP );
        table.updateUI();
    }

    private void updateInputParameters( List<ParameterType> inputParametrsForSP ) {
        updateTable( inputParametrs, inputParametrsForSP );
    }

    @NotNull
    private DaoMethod getCurrentDaoMethod(){
        final DaoMethod result = new DaoMethod();

        result.setCommon( new CommonType() );
        result.getCommon().setConfiguration( new ConfigurationType() );

        result.getCommon().getConfiguration().setType( determineQueryType() );
        result.getCommon().getConfiguration().setMultipleResult( isMultipleResultCheckBox.isSelected() );

        if( result.getSelectType() == SelectType.CALL ){
            result.getCommon().setSpName( Extractor.getStoreProcedureName( queryTextArea.getText() ) );
        }

        result.getCommon().setQuery( queryTextArea.getText() );
        result.getCommon().setMethodName( methodNameField.getText() );
        result.getCommon().setComment( commentTextArea.getText() );
        result.getCommon().setMethodName( methodNameField.getText() );

        result.setInputParametrs( new ParametersType() );
        result.getInputParametrs().setParent( ( ParentType ) inputParamsType.getSelectedItem() );
        result.getInputParametrs().getParameter().addAll(
                ( ( ParametrsModel ) ( inputParametrs.getModel() ) ).getParameterTypes()
        );

        result.setOutputParametrs( new ParametersType() );
        result.getOutputParametrs().setParent( ( ParentType ) outputParamsType.getSelectedItem() );
        result.getOutputParametrs().getParameter().addAll(
                ( (ParametrsModel) ( outputParametrs.getModel() ) ).getParameterTypes()
        );
        return result;
    }

    @NotNull
    private SelectType determineQueryType() {
        return Extractor.determineQueryType( queryTextArea.getText() );
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

    private boolean validateBeforeStartGenerateJavaClasses() {
        if(
                ( ! isPackageName( mappingPackageTextField  .getText() ) )
        ){
            JOptionPane.showMessageDialog( mainPanel, "Одно или несколько имен пакетов не прошло валидацию." );
            return false;
        }
        if (!checkAndClearTargetFolder())
        	return false;
        //TODO надо бы проверить пути
        return true;
    }

    private boolean isPackageName( String packageName ){
        return Pattern.compile( "[\\w\\d]+(\\.[\\w\\d]+)+" ).matcher( packageName ).matches();
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

    public synchronized static JPanel getInstance(){
        if ( INSTANCE == null ) {
            INSTANCE = new Form();
        }
        return INSTANCE.mainPanel;
    }

    @Override
    public void sourcePathChanged() {
        loadSettings();
    }
}