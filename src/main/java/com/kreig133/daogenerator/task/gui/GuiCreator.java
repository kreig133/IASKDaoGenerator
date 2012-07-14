package com.kreig133.daogenerator.task.gui;

import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.task.gui.field.JTaskField;
import com.kreig133.daogenerator.task.gui.field.JTaskFieldFactory;
import com.kreig133.daogenerator.task.gui.lib.SpringUtilities;
import com.kreig133.daogenerator.task.model.IaskTask;

import javax.swing.*;
import java.util.*;

import static com.kreig133.daogenerator.sql.creators.QueryCreator.extractInputParams;

/**
 * @author kreig133
 * @version 1.0
 */
public class GuiCreator {
    public JPanel createGui( List<IaskTask> taskList ) {
        getTaskGroupedByRootMenuName( taskList );

        return null;
    }

    private Map<String, List<IaskTask>> getTaskGroupedByRootMenuName( List<IaskTask> taskList ) {
        Map<String, List<IaskTask>> taskGroupedByRootMenuName = new HashMap<String, List<IaskTask>>();
        for ( IaskTask iaskTask : taskList ) {
            if ( taskGroupedByRootMenuName.get( iaskTask.getCommon().getGroup() ) == null ) {
                taskGroupedByRootMenuName.put( iaskTask.getCommon().getGroup(), new ArrayList<IaskTask>() );
            }
            taskGroupedByRootMenuName.get( iaskTask.getCommon().getGroup() ).add( iaskTask );
        }
        return taskGroupedByRootMenuName;
    }

    JPanel createGuiForAnyTask( IaskTask task ) {
        switch ( task.getConfiguration().getType() ) {
            case OTHER:
                break;
            default:
                return createGuiForQueryTask( task );
        }
        throw new RuntimeException( "Косяк" );
    }

    JPanel createGuiForQueryTask( IaskTask task ) {
        List<ParameterType> parameterTypeList = extractInputParams( task.getConfiguration().getQueryText() );

        JTaskPanel panel = new JTaskPanel();
        panel.setLayout( new SpringLayout() );

        for ( ParameterType parameterType : parameterTypeList ) {
            JLabel label = new JLabel( parameterType.getName(), JLabel.TRAILING );
            JTaskField field = JTaskFieldFactory.newJTaskField( parameterType );

            panel.add( label );
            label.setLabelFor( field );
            panel.add( field, parameterType );

            field.setValue( parameterType.getTestValue() );
            field.setToolTipText(
                    parameterType.getComment() == null ? "Входной параметр для запроса" : parameterType.getComment()
            );
        }
        SpringUtilities.makeCompactGrid( panel,
                parameterTypeList.size(), 2,
                6, 6,
                6, 6 );
        return panel;
    }
}
