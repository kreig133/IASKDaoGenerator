package com.kreig133.daogenerator.task.gui;

import org.junit.Test;

import javax.swing.*;

/**
 * @author kreig133
 * @version 1.0
 */
public class TaskFormTest {
    @Test
    public void test() throws InterruptedException {
        JPanel ui = new TaskForm().getUI();
        TaskTestHelper.showComponent( ui );

//        Thread.sleep( 100000 );
    }
}
