/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consumption;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import nekore.MyDate;

/**
 *
 * @author mabarthe
 */
public class ListEntry implements Comparable<ListEntry> {
    
    private Date    timestamp;
    private double  value;
    
    public ListEntry ()
    {
        this.timestamp = new Date(0);
        this.value = 0;
    }
    
    public ListEntry(long milliseconds, double value) {
        this.timestamp = new Date(milliseconds);
        this.value = value;
    }
    
    public MyDate getTimestamp() {
        return MyDate.convert(timestamp);
    }

    public String getDateString()
    {
        return MyDate.convert(timestamp).myFormat();
    }
    
    public String getDateString_Month()
    {
        DateFormat dateFormat = new SimpleDateFormat("MM.yyyy");
        return dateFormat.format( this.timestamp ); 
    }

    public double getValue() {
        return value;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isDate(Date date)
    {
        return (timestamp.equals(date)) ? true : false;
    }
    
    public String toString()
    {
        return String.format("%s %.3f", timestamp.toString(), value);
    }
    
    public void printConsole()
    {
        System.out.println( toString() );
    }

    @Override
    public int compareTo(ListEntry o) {
        return timestamp.compareTo(o.timestamp);
    }
    
    
    
}
