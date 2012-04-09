package com.kreig133.daogenerator.gui;

import javax.swing.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class TextView {
    private static final TextView INSTANCE = new TextView();

    private JEditorPane textArea;
    private JPanel panel;

    private TextView() {
    }

    public static JPanel getInstance(){
        return INSTANCE.panel;
    }

    public static void setText( String s  ){
        INSTANCE.textArea.setContentType( "text/sql" );
        INSTANCE.textArea.setText( s );
    }
}
