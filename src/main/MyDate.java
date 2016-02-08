/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author mabarthe
 */
public class MyDate extends Date {

    DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMAN);

    public MyDate()
    {
        super();
    }
    
    public MyDate(long milliseconds)
    {
        super(milliseconds);
    }
    
    static public MyDate convert( Date d )
    {
        return new MyDate( d.getTime() );
    }
    
    public String myFormat()
    {
        return format.format( this );
    }
}
