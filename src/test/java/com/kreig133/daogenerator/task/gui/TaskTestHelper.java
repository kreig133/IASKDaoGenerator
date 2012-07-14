package com.kreig133.daogenerator.task.gui;

import com.kreig133.daogenerator.task.model.Common;
import com.kreig133.daogenerator.task.model.Configuration;
import com.kreig133.daogenerator.task.model.IaskTask;
import com.kreig133.daogenerator.task.model.Type;

/**
 * @author kreig133
 * @version 1.0
 */
public class TaskTestHelper {
    public static IaskTask getIaskTask() {
        IaskTask iaskTask = new IaskTask();
        iaskTask.setCommon( new Common() );
        iaskTask.getCommon().setGroup( "Новый таск" );
        iaskTask.getCommon().setName( "Первый таск" );
        iaskTask.setConfiguration( new Configuration() );
        iaskTask.getConfiguration().setQueryText( "SELECT * FROM ${first;int;0;Первый} " +
                "${second;string; ;Второй} ${third;datetime;3-22-1990 23:59:59.000;Какая-то дата}" );
        iaskTask.getConfiguration().setType( Type.QUERY_WITH_XSLT_PROCESSING );
        return iaskTask;
    }
}
