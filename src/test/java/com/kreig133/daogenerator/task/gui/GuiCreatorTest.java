package com.kreig133.daogenerator.task.gui;

import org.junit.Test;

import javax.swing.*;

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

        JFrame jFrame = new JFrame();
        jFrame.add( panel );
        jFrame.pack();
        jFrame.setVisible( true );

        Thread.sleep( 100000 );
    }
}
