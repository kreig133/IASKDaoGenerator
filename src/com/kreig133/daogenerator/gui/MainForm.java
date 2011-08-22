package com.kreig133.daogenerator.gui;

import com.kreig133.daogenerator.enums.ReturnType;
import com.kreig133.daogenerator.enums.SelectType;
import com.kreig133.daogenerator.enums.Type;

import javax.swing.*;
import javax.swing.event.ListDataListener;

/**
 * @author eshangareev
 * @version 1.0
 */
public class MainForm {
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JComboBox typeComboBox;
    private JComboBox selectTypeComboBox;
    private JComboBox returnTypeComboBox;

    public MainForm() {
        init();
    }

    private void init(){
        typeComboBox        .setModel( new DefaultComboBoxModel( Type       .values() ) );
        selectTypeComboBox  .setModel( new DefaultComboBoxModel( SelectType .values() ) );
        returnTypeComboBox  .setModel( new DefaultComboBoxModel( ReturnType .values() ) );

    }
}
