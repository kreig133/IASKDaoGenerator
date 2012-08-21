package com.kreig133.daogenerator.presenter;

import com.kreig133.daogenerator.gui.ParametrsModel;

public interface FormInterface {

    //для вкладки AnalyticMode

    public boolean getIsMultipleResult();
    public void setIsMultipleResult(boolean value);

    public boolean getSingleQueryChecked();
    public void setSingleQuery(boolean value);

    public boolean getDoubleQueryChecked();
    public void setDoubleQuery(boolean value);

    public String getQueryTextArea();
    public void setQueryTextArea(String text);

    public String getSecondQuery();
    public void setSecondQuery(String text);

    public String getMethodName();
    public void setMethodName(String name);

    public String getCommentText();
    public void setCommentText(String text);

    public ParametrsModel getInputParametersTypes();

    public ParametrsModel getOutputParametersTypes();

    public String getDirForSave();

    public String getFileForSave();

    public void showWindowWithText(String text);

    //для вкладки DeveloperMode

    public String getMappingPackageText();
    public void setMappingPackageText(String text);

    public String getEntityPackageText();
    public void setEntityPackageTextField(String text);

    public String getDaoPackageText();
    public void setDaoPackageTextField(String text);

    public String getDestDirText();
    public void setDestDirText(String text);

    //общие
    public boolean showWarningDialog(String message);
    public boolean showAttentionDialog(String message);

}
