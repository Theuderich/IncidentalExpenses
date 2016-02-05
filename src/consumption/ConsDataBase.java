/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consumption;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mabarthe
 */

public class ConsDataBase {

    private static ConsDataBase instance;
    
    private List<DataList>          data;
    private List<DataListener>      subscribed;

    private ConsDataBase ()
    {
        data = new ArrayList<DataList>();
        for( int i=0; i<DataType.MAX_TYPES.getLocalIndex(); i++)
            data.add( new DataList( DataType.getItem(i) ) );
        subscribed = new ArrayList<DataListener>();
    }

    public static ConsDataBase getInstance()
    {
        if( instance == null )
            instance = new ConsDataBase();
        return instance;
    }
    
    public DataList getConsList( DataType type )
    {
        DataList            list = null;
        DataList            res = null;
        
        Iterator<DataList>  itr = data.iterator();
        while(itr.hasNext())
        {
            list = itr.next();
            if( list.isType(type) )
            {
                res = list;
                break;
            }
        }
        return res;
    }

    public void printConsole( )
    {
        Iterator<DataList>  itr = data.iterator();
        while(itr.hasNext())
        {
            itr.next().printConsole();
        }
    }
    
    public void saveToFile( )
    {
        Iterator<DataList>  itr = data.iterator();
        while(itr.hasNext())
        {
            itr.next().save();
        }
    }
    
    public void loadFromFile( )
    {
        Iterator<DataList>  itr = data.iterator();
        while(itr.hasNext())
        {
            itr.next().load();
        }
    }
    
    public boolean existsListener( DataListener obj )
    {
        boolean                     status = false;
        Iterator<DataListener>      itr = subscribed.iterator();
        while( itr.hasNext() )
        {
           if( itr.next() == obj )
           {
               status = true;
               break;
           }
        }
        return status;
    }
    
    public void subscribe( DataListener obj )
    {
        if( !existsListener(obj) )
        {
            subscribed.add(obj);
        }
    }

    public void dataHasChanged( )
    {
        Iterator<DataListener> itr = subscribed.iterator();
        while( itr.hasNext() )
        {
            DataListener obj = itr.next();
            obj.notifyDataChanged();
        }
    }
    
}
