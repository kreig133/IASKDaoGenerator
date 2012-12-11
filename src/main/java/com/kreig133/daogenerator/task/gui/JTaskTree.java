package com.kreig133.daogenerator.task.gui;

import com.kreig133.daogenerator.task.model.IaskTask;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kreig133
 * @version 1.0
 */
public class JTaskTree extends JTree implements TreeSelectionListener{

    public JTaskTree( DefaultMutableTreeNode root ) {
        super( root );
        addTreeSelectionListener( this );
    }

    Map<String, Map.Entry<IaskTask, JTaskPanel>> map;

    TaskForm taskForm;

    public void setMap( Map<String, Map.Entry<IaskTask, JTaskPanel>> map ) {
        this.map = map;
    }

    public void setTaskForm( TaskForm taskForm ) {
        this.taskForm = taskForm;
    }


    @Override
    public void valueChanged( TreeSelectionEvent e ) {
        if( e.getNewLeadSelectionPath().getPathCount() == 3 ){
            Map.Entry<IaskTask, JTaskPanel> iaskTaskJTaskPanelEntry =
                    map.get( e.getNewLeadSelectionPath().getLastPathComponent().toString() );
            taskForm.update( iaskTaskJTaskPanelEntry.getKey(), iaskTaskJTaskPanelEntry.getValue() );
        }
    }
}
