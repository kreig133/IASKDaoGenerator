package com.kreig133.daogenerator.task.gui;

import com.kreig133.daogenerator.task.model.Common;
import com.kreig133.daogenerator.task.model.Configuration;
import com.kreig133.daogenerator.task.model.IaskTask;
import com.kreig133.daogenerator.task.model.Type;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kreig133
 * @version 1.0
 */
public class TaskTestHelper {
    
    public static IaskTask getIaskTask() {
        return getIaskTask( "" );
    }

    private static IaskTask getIaskTask( Object obj ) {
        IaskTask iaskTask = new IaskTask();
        iaskTask.setCommon( new Common() );
        iaskTask.getCommon().setGroup( "Новый таск" + obj );
        iaskTask.getCommon().setName( "Первый таск" + obj );
        iaskTask.setConfiguration( new Configuration() );
        iaskTask.getConfiguration().setQueryText( "SELECT * FROM ${first;int;0;Первый} " +
                "${second;string; ;Второй} ${third;datetime;3-22-1990 23:59:59.000;Какая-то дата}" );
        iaskTask.getConfiguration().setType( Type.QUERY_WITH_XSLT_PROCESSING );
        return iaskTask;
    }

    public static List<IaskTask> getIaskTaskList( int count ) {
        List<IaskTask> result = new ArrayList<IaskTask>( count );
        for ( int i = 0; i < count; i++ ) {
            result.add( getIaskTask( i ) );
        }
        return result;
    }

    public static void showComponent( JComponent panel ) {
        JFrame jFrame = new JFrame();
        jFrame.add( panel );
        jFrame.pack();
        jFrame.setVisible( true );
    }
}
