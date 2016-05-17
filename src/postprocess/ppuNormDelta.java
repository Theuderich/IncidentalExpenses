/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package postprocess;

import consumption.DataListCounters;
import consumption.DataModelCounter;
import consumption.ListEntry;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import main.MyDate;

/**
 *
 * @author mabarthe
 */

public class ppuNormDelta extends ppu {
    
    static private ppuNormDelta inst = null;
    private List<NormDelta> data;
//    DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMAN);
    
    private ppuNormDelta ()
    {
        super();
        data = new ArrayList<NormDelta>();
        DataModelCounter.getInstance().subscribe(this);
    }
    
    static public ppuNormDelta getInst()
    {
        if( inst == null )
        {
            inst = new ppuNormDelta();
        }
        return inst;
    }
    
    public void notifyDataChanged()
    {
        data.clear();
        
        consumption.DataType type = ppuModel.getInst().getCounterType();
        
        DataModelCounter dbCounter = DataModelCounter.getInstance();
        DataListCounters list = dbCounter.getConsList(type);
        
        Iterator<ListEntry> it = list.getIterator();
        ListEntry eOld = null;
        
        while(it.hasNext())
        {
            
            ListEntry eCur = it.next();
            if( eOld != null )
            {
                NormDelta delta = new NormDelta();                
                delta.start = eOld.getTimestamp();
                delta.stop = eCur.getTimestamp();
                delta.average = (eCur.getValue() - eOld.getValue() )/delta.getTimeDiffDay();
                data.add(delta);
            }
            eOld = eCur;
        }
    }
    
    public Iterator<NormDelta> getIterator()
    {
        return data.iterator();
    }
    
    
//    public double getAverageValue()
//    {
//        double  sum = 0;
//        int     count = 0;
//
//
//        
//        Iterator<NormDelta> it = data.iterator();
//        while(it.hasNext())
//        {
//            sum += it.next().average;
//            count++;
//        }
//        
//        if( count != 0 )
//            return sum / count;
//        
//        return 0;
//    }
//    
    public void printConsole()
    {
        String  format = "%-20s | %-20s | %5s | %7s";
        System.out.println("Normalized Deltas of: " + ppuModel.getInst().getCounterType().toString());
        System.out.println( String.format(format, "Start Date", "Stop Date", "Days", "Average"));
        System.out.println( String.format("-------------------------------------------------------------"));
        
        Iterator<NormDelta> it = data.iterator();
        while(it.hasNext())
        {
            NormDelta delta = it.next();
            System.out.println( String.format(format, delta.start.myFormat(), delta.stop.myFormat(), new DecimalFormat("#0.0").format(delta.getTimeDiffDay()), new DecimalFormat("##0.000").format(delta.average)));
        }
        System.out.println();
    }
    
}