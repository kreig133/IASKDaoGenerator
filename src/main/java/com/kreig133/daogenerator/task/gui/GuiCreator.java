package com.kreig133.daogenerator.task.gui;

import com.kreig133.daogenerator.jaxb.ParameterType;
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

    private JPanel createGuiForTask( IaskTask task ) {
        JPanel panel = new JPanel( new SpringLayout() );
        switch ( task.getConfiguration().getType() ) {
            case OTHER:
                break;
            default:
                List<ParameterType> parameterTypeList = extractInputParams( task.getConfiguration().getQueryText() );

                for ( ParameterType parameterTypes : parameterTypeList ) {
                    JLabel label = new JLabel( parameterTypes.getName(), JLabel.TRAILING );
                    JTextField field = new JTextField();
                    panel.add( label );
                    label.setLabelFor( field );
                    panel.add( field );
                }
                SpringUtilities.makeCompactGrid( panel,
                        parameterTypeList.size(), 2,
                        6, 6,
                        6, 6 );
        }
        return panel;
    }
}
