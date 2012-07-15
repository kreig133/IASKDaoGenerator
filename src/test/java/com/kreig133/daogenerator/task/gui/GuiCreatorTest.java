package com.kreig133.daogenerator.task.gui;

import org.junit.Test;

import javax.swing.*;

import static com.kreig133.daogenerator.task.gui.TaskTestHelper.showComponent;

/**
 * @author kreig133
 * @version 1.0
 */
public class GuiCreatorTest{
    @Test
    public void testCreateGui() throws Exception {

    }

    @Test
    public void testCreateGuiForQueryTask() throws InterruptedException {
        JPanel panel = new GuiCreator().createGuiForQueryTask( TaskTestHelper.getIaskTask() );

        showComponent( panel );

        Thread.sleep( 100000 );
    }



    @Test
    public void test() throws InterruptedException {
        JTree gui = new GuiCreator().createGui( TaskTestHelper.getIaskTaskList( 10 ) );
        showComponent( gui );

        Thread.sleep( 100000 );
    }


}
