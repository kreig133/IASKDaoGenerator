package com.kreig133.daogenerator.presenter;

import com.kreig133.daogenerator.gui.ParametrsModel;

public interface FormInterface {

    //для вкладки AnalyticMode

    boolean getIsMultipleResult();
    void setIsMultipleResult(boolean value);

    boolean getSingleQueryChecked();
    void setSingleQuery(boolean value);

    boolean getDoubleQueryChecked();
    void setDoubleQuery(boolean value);

    String getQueryTextArea();
    void setQueryTextArea(String text);

    String getSecondQuery();
    void setSecondQuery(String text);

    String getMethodName();
    void setMethodName(String name);

    String getCommentText();
    void setCommentText(String text);

    ParametrsModel getInputParametersTypes();

    ParametrsModel getOutputParametersTypes();

    String getDirForSave();

    String getFileForSave();

    void showWindowWithText(String text);

    //для вкладки DeveloperMode

    String getMappingPackageText();
    void setMappingPackageText(String text);

    String getEntityPackageText();
    void setEntityPackageTextField(String text);

    String getDaoPackageText();
    void setDaoPackageTextField(String text);

    String getDestDirText();
    void setDestDirText(String text);

    void lockGui();
    void unlockGui();

    //общие
    boolean showWarningDialog(String message);
    boolean showAttentionDialog(String message);

}
