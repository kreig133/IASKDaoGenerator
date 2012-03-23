package com.kreig133.daogenerator.gui;

import javax.swing.*;

/**
 * @author eshangareev
 * @version 1.0
 */
public class TextView {
    private static TextView INSTANCE;

    private JEditorPane textArea;
    private JPanel panel;


    public static JPanel getInstance(){
        if ( INSTANCE == null ) {
            INSTANCE = new TextView();
        }

        return INSTANCE.panel;
    }

    public static void setText( String s  ){
        if ( INSTANCE == null ) {
            INSTANCE = new TextView();
            INSTANCE.textArea.setContentType( "text/sql" );
        }
        INSTANCE.textArea.setText( s );
    }


}
