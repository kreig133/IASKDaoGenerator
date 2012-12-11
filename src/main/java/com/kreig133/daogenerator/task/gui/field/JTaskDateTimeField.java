package com.kreig133.daogenerator.task.gui.field;

import com.kreig133.daogenerator.common.Utils;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.plaf.SpinnerUI;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicSpinnerUI;
import java.awt.*;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author kreig133
 * @version 1.0
 */
public class JTaskDateTimeField extends JTaskField<Date> {

    private JDateChooser dateChooser;
    private JSpinner timeChooser;

    public JTaskDateTimeField() {
        JPanel panel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        dateChooser = new JDateChooser();
        dateChooser.setPreferredSize( new Dimension( 100, 20 ) );


        timeChooser = new JSpinner( new SpinnerDateModel() );
        timeChooser.setSize( new Dimension( 60, 20 ) );

        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor( timeChooser, "HH:mm:ss" );
        timeChooser.setEditor( timeEditor );

        panel.add( dateChooser );
        panel.add( timeChooser );

        this.add( wrapWithVerticalBoxLayout( panel ), BorderLayout.CENTER );
    }

    @Override
    public Date getValue() {
        Calendar result = Calendar.getInstance();
        result.setTime( dateChooser.getDate() );

        Calendar temp = Calendar.getInstance();
        temp.setTime( ( Date ) timeChooser.getValue() );

        result.set( Calendar.HOUR_OF_DAY, temp.get( Calendar.HOUR_OF_DAY ) );
        result.set( Calendar.MINUTE     , temp.get( Calendar.MINUTE      ) );
        result.set( Calendar.SECOND     , temp.get( Calendar.SECOND      ) );

        return result.getTime();
    }

    @Override
    public void setValue( String value ) {
        try {
            Date parsedDate = Utils.getDaoGeneratorDateFormat().parse( value );
            dateChooser.setDate( parsedDate );
            timeChooser.setValue( parsedDate );
        } catch ( ParseException e ) {
            e.printStackTrace();
        }
    }
}
