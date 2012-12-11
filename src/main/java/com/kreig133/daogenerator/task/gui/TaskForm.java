package com.kreig133.daogenerator.task.gui;

import com.kreig133.daogenerator.task.model.IaskTask;

import javax.swing.*;

/**
 * @author kreig133
 * @version 1.0
 */
public class TaskForm {
    private JPanel root;
    private JTree tree;
    private JTextArea descriptionTextArea;
    private JButton runButton;
    private JPanel panelWIthFields;
    private JLabel pathLabel;

    IaskTask currentTask;

    public void update( IaskTask task, JTaskPanel fields ) {
        currentTask = task;

        pathLabel.setText( task.getCommon().getGroup() + ">" + task.getCommon().getName() );
        descriptionTextArea.setText( task.getCommon().getDescription() );
        panelWIthFields.removeAll();
        panelWIthFields.add( fields );

        root.repaint();
    }

    private void createUIComponents() {
        tree = new GuiCreator().createGui( TaskTestHelper.getIaskTaskList( 10 ) );
        JTaskTree taskTree = ( JTaskTree ) tree;
        taskTree.setTaskForm( this );
    }

    public JPanel getUI() {
        return root;
    }
}
