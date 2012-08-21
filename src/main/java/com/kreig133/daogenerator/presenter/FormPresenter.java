package com.kreig133.daogenerator.presenter;

import com.kreig133.daogenerator.JaxbHandler;
import com.kreig133.daogenerator.MavenProjectGenerator;
import com.kreig133.daogenerator.WikiGenerator;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.db.extractors.Extractor;
import com.kreig133.daogenerator.db.extractors.in.InputParameterExtractor;
import com.kreig133.daogenerator.db.extractors.in.SpInputParameterExtractor;
import com.kreig133.daogenerator.db.extractors.out.OutputParameterExtractor;
import com.kreig133.daogenerator.db.preparators.DoubleQueryPreparator;
import com.kreig133.daogenerator.db.preparators.QueryPreparator;
import com.kreig133.daogenerator.files.PackageAndFileUtils;
import com.kreig133.daogenerator.files.builder.FileBuilder;
import com.kreig133.daogenerator.jaxb.*;
import com.kreig133.daogenerator.jaxb.validators.DaoMethodValidator;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class FormPresenter {

    private FormInterface view;

    private boolean isSpCall;
    private boolean isSelect;

    public void prepareQuery(){
        if (determineQueryType(view.getQueryTextArea()) == SelectType.CALL)
            throw PresenterException.warn("Предварительная обработка для ХП не требуется!");
        String preparedQuery;
        if (view.getDoubleQueryChecked()) {
            preparedQuery = DoubleQueryPreparator.instance().prepareQuery(view.getQueryTextArea(),
                    view.getSecondQuery());
        } else {
            preparedQuery = QueryPreparator.instance().prepareQuery(view.getQueryTextArea());
        }
        view.setQueryTextArea(preparedQuery);
    }

    public List<ParameterType> getInParameters(){
        if ( StringUtils.isBlank(view.getQueryTextArea()) )
            throw PresenterException.warn("Введите текст запроса или название хранимой процедуры");
        DaoMethod currentDaoMethod = getCurrentDaoMethod();
        isSpCall = currentDaoMethod.getSelectType() == SelectType.CALL;
        isSelect = currentDaoMethod.getSelectType() == SelectType.SELECT;

        DaoMethod daoMethod = InputParameterExtractor.instance(currentDaoMethod)
                .extractInputParams( currentDaoMethod );

        return daoMethod.getInputParametrs().getParameter();
    }

    public List<ParameterType> getOutParameters(){
        final DaoMethod daoMethod = OutputParameterExtractor.instance(
                getCurrentDaoMethod().getSelectType()).getOutputParameters( getCurrentDaoMethod() );
        return daoMethod.getOutputParametrs().getParameter();
    }

    public void generateXML(){
        final DaoMethod currentDaoMethod = getCurrentDaoMethod();

        if( StringUtils.isBlank(currentDaoMethod.getCommon().getMethodName()) ) {
            InputParameterExtractor.instance( currentDaoMethod ).fillMethodName( currentDaoMethod );
        }

        if ( ! DaoMethodValidator.checkDaoMethods(Arrays.asList(currentDaoMethod)) ) {
            {
                if (!view.showWarningDialog("<html>Имеются ошибки! (Детали во вкладке Log)<br>Продолжить?"))
                    throw PresenterException.warn("Операция отменена!");
            }
        }

        String dirForSave = view.getDirForSave();
        if (dirForSave == null){
            throw PresenterException.warn("Не выбран путь для сохранения файла!");
        }

        final String xmlFilePath = dirForSave + "/" +
                currentDaoMethod.getCommon().getMethodName() + ".xml";

        JaxbHandler.marshallInFile(new File(xmlFilePath), currentDaoMethod);

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            WikiGenerator.generateWikiForXmlFile(xmlFilePath);
                            view.showWindowWithText(Utils.streamToString(new FileInputStream(xmlFilePath + ".txt")));
                        } catch ( IOException ex ) {
                            ex.printStackTrace();
                        } catch ( InterruptedException ex ) {
                            ex.printStackTrace();
                        }
                    }
                }
        ).start();
    }

    public void generateWiki(){
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            WikiGenerator.generateWiki(view.getFileForSave());
                        } catch ( Exception ex ) {
                            ex.printStackTrace();
                            throw PresenterException.warn("Ошибка при создании разметки! (см. логи)");
                        }
                    }
                }
        ).start();
    }

    public void startProjectGenerating(){
        if( validateBeforeStartGenerateJavaClasses() ){
            new Thread(
                new Runnable(){
                    @Override
                    public void run() {
                        if(!FileBuilder.generateJavaCode()) return;
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
                            throw PresenterException.warn
                                    ("Тестирование провалилось.\nИзмените входные данные или сообщите " +
                                            "разработчику этой фигни, что он, возможно, где-то накосячил");
                        }
                    }
                }
            ).start();
        }
    }

    public boolean validateBeforeStartGenerateJavaClasses() {
        if(
                ( ! isPackageName(view.getMappingPackageText())) ||
                        ( ! isPackageName(view.getEntityPackageText())) ||
                        ( ! isPackageName(view.getDaoPackageText()))
                ){
            throw PresenterException.warn("<html>Одно или несколько имен пакетов не прошло валидацию." +
                    "<br>(Обязательно должно начинаться с ru.sbrf.iask.[см. \"Стандарт разработки\"])"
            );
        }
        if (!checkAndClearTargetFolder())
            return false;
        //TODO надо бы проверить пути
        return true;
    }

    private boolean isPackageName( String packageName ){
        return Pattern.compile("[\\w\\d]+(\\.[\\w\\d]+)+").matcher( packageName ).matches()
                && packageName.startsWith( "ru.sbrf.iask." );
    }

    /**
     * Проверяет и удаляет папку с ранее сформированными проектными файлами
     */
    private boolean checkAndClearTargetFolder() {
        String folderName = view.getDestDirText();
        File directory = new File(folderName);

        if (directory.exists()) {
            if (!directory.isDirectory() ) {
                throw PresenterException.warn("Указанное в поле значение не является папкой!");
            }

            if (view.showAttentionDialog(String.format("Указанная папка \"%s\" существует.\nУдалить её?", folderName))){
                if (!PackageAndFileUtils.removeDirectory(directory) ) {
                    throw PresenterException.warn("При удалении папки возникли ошибки. " +
                            "Проверьте, что она не используется другими программами.");
                }
            }
        }
        return true;
    }

    @NotNull
    private DaoMethod getCurrentDaoMethod(){
        final DaoMethod result = new DaoMethod();

        result.setCommon(new CommonType());
        result.getCommon().setConfiguration(new ConfigurationType());

        result.getCommon().getConfiguration().setType(determineQueryType(view.getQueryTextArea()));
        result.getCommon().getConfiguration().setMultipleResult(view.getIsMultipleResult());

        if( result.getSelectType() == SelectType.CALL ){
            result.getCommon().setSpName( Extractor.getStoreProcedureName(view.getQueryTextArea()));
        }

        result.getCommon().setQuery(view.getQueryTextArea());
        result.getCommon().setMethodName(view.getMethodName());
        result.getCommon().setComment(view.getCommentText());

        result.setInputParametrs( new ParametersType() );
        result.getInputParametrs().getParameter().addAll(
                (view.getInputParametersTypes()).getParameterTypes()
        );

        result.setOutputParametrs(new ParametersType());
        result.getOutputParametrs().getParameter().addAll(
                (view.getOutputParametersTypes()).getParameterTypes()
        );
        return result;
    }

    @NotNull
    private SelectType determineQueryType(String firstAreaQueryText) {
        return Extractor.determineQueryType(firstAreaQueryText);
    }

    public void getSPText(){
        view.showWindowWithText(SpInputParameterExtractor.getSPText());
    }

    public void setView(FormInterface view){
        if (this.view == null)
            this.view = view;
    }

    public boolean getIsSpCall() {
        return isSpCall;
    }

    public boolean getIsSelect() {
        return isSelect;
    }
}
