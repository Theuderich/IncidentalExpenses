/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consumption;

import com.thoughtworks.xstream.XStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nekore.MyDate;

/**
 *
 * @author mabarthe
 */
public class DataList {
    
    private XStream             xstream;
    protected DataType          dataType;
    protected List<ListEntry>     datalist;

    public DataList( DataType type ) 
    {
        xstream = new XStream();
        xstream.alias("entry", ListEntry.class);
        xstream.alias("datalist", DataList.class);
        xstream.addImplicitCollection(DataList.class, "datalist");    
        
        dataType = type;
        this.datalist = new ArrayList<ListEntry>();
    }
    /***************************************************************************
     * 
     * Data Management
     * 
     * Manipulation of the List Data
     */ 

    public boolean isType( DataType type )
    {
        return (dataType == type) ? true : false;
    }

    public boolean isGroup( DataGroup group )
    {
        return (dataType.getGroup() == group) ? true : false;
    }

    public boolean add(ListEntry e)
    {
        boolean status = false; 
        ListEntry element = getByDate((MyDate)e.getTimestamp());
        if( element == null && !Double.isNaN(e.getValue()))
        {
            status = true;
            datalist.add(e);
        }
        
        return status;
    }
    
    public ListEntry getByDate( MyDate date)
    {
        ListEntry en = null;
        Iterator<ListEntry>     iterator = datalist.iterator();
        while(iterator.hasNext())
        {
            ListEntry temp = iterator.next();
            if(temp.isDate(date))
            {
                en = temp;
                break;
            }
        }
        return en;
    }

    public ListEntry getFirst()
    {
        ListEntry e = null;
        if(datalist.size() > 0)
            e = datalist.get(0);
        return e;
    }

    public ListEntry getLast()
    {
        ListEntry e = null;
        if(datalist.size() > 0)
            e = datalist.get( datalist.size()-1 );
        return e;
    }
    
    public Date getMinDate()
    {
        return datalist.get(0).getTimestamp();
    }
    
    public Date getMaxDate()
    {
        return datalist.get( datalist.size()-1 ).getTimestamp();
    }
    
    public double getValueRangeInBetween( Date start, Date stop )
    {
        ListEntry eStart = interpolateAtTimestamp(start);
        ListEntry eStop = interpolateAtTimestamp(stop);
        double  diff = 0;
        
        if( (eStart != null) && (eStop != null) )
        {
            diff = eStop.getValue() - eStart.getValue();
        }
        else if( (eStart != null) && (eStop == null) )
        {
            double  timeframe = stop.getTime() - start.getTime();
            
            ListEntry eLast = this.getLast();
            double diffValue = eLast.getValue() - eStart.getValue();
            double diffTime = eLast.getTimestamp().getTime() - eStart.getTimestamp().getTime();
            
            diff = (diffValue * timeframe) / diffTime; 
        }
        
        return diff;
    }
    
    public boolean removeNaN( )
    {
        boolean status = false;
        ListEntry en = null;
        Iterator<ListEntry>     iterator = datalist.iterator();
        while(iterator.hasNext())
        {
            ListEntry temp = iterator.next();
            if(Double.isNaN(temp.getValue()))
            {
                status = true;
                iterator.remove();
            }
        }
        return status;
    }
    
    public void sortByTimestamp()
    {
        datalist.sort(null);
    }
    
    public Iterator<ListEntry> getIterator()
    {
        return datalist.iterator();
    }
    
    public double getSize()
    {
        return datalist.size();
    }
    
    /***************************************************************************
     * 
     * JFreeChart
     * 
     * Converting data into special formats
     */
   
    public ListEntry interpolateAtTimestamp( Date point )
    {
        ListEntry res = null;
        ListEntry cur = null;
        ListEntry before = null;

        // search pair with the point in the middle
        Iterator<ListEntry>     iterator = datalist.iterator();
        while(iterator.hasNext())
        {
            cur = iterator.next();
            if( cur.getTimestamp().compareTo(point) >= 0 )
                break;
            before = cur;
        }
        
        if ( before == null )
            return null;
        
        if( cur.getTimestamp().compareTo(point) > 0 )
        {
            // found pair is around the point
            double slope = (cur.getValue() - before.getValue())/(cur.getTimestamp().getTime() - before.getTimestamp().getTime());
            double inter = cur.getValue() - (slope * cur.getTimestamp().getTime());
            res = new ListEntry( point.getTime(), slope * point.getTime() + inter );
            
        }
        else if( cur.getTimestamp().compareTo(point) < 0 )
        {
            // found pair is still before the point
            // extrapolate
            res = null;
        }
        else
        {
            // found cur Entry hits directly the point
            res = cur;
        }
        
        return res;
    }
    
    public double getAvgOfDay( Date point )
    {
        ListEntry cur = null;
        double  avg = 0;
        int     count = 0;
        
        Iterator<ListEntry>     iterator = datalist.iterator();
        while(iterator.hasNext())
        {
            cur = iterator.next();
            long diff = cur.getTimestamp().getTime() - point.getTime();
            if( diff >= 0 && diff < (1 * 1000 * 60 * 60 * 24) )
            {
                if( !Double.isNaN(cur.getValue()) )
                {
                    avg += cur.getValue();
                    count++;
                }
            }
        }
        return avg / count;
    }
    
    /***************************************************************************
     * 
     * File Access
     * 
     * Saving and Loading Data
     * 
     */
    
    /**
     * Generates the file name of this data list
     * 
     * @return 
     */
    private String getFileName()
    {
        return String.format("DataList_%s.xml", this.dataType);
    }
    
    /**
     * Saves the current contend into an XML file
     * 
     */
    public void save() 
    {
        try (PrintStream out = new PrintStream(new FileOutputStream(getFileName()))) {
            
            out.print(xstream.toXML(datalist));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Reads the contend of a ASCII file into a single string
     * 
     * @param path
     * @param encoding
     * @return
     * @throws IOException 
     */
    private String readFile(String path, Charset encoding) 
        throws IOException 
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }    
    
    /**
     * Loads the stored data into the data list
     * 
     */
    public void load() 
    {
        String content = null;
        try {
            content = readFile(getFileName(), StandardCharsets.UTF_8);
            datalist = (List<ListEntry>)xstream.fromXML(content);
        } catch (IOException ex) {
//            Logger.getLogger(DataList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /***************************************************************************
     * 
     * Debug
     * 
     * Console printing
     */
    
    public void printConsole()
    {
        System.out.println(String.format("Data Set: %s", dataType));

        int                 i = 0;
        Iterator<ListEntry>     iterator = datalist.iterator();
        while(iterator.hasNext())
        {
            System.out.println(String.format("%03d: %s", i++, iterator.next().toString()));
        }
        System.out.println();
    }
}
