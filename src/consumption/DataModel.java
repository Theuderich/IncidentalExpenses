/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consumption;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author mabarthe
 */

public abstract class DataModel {

    protected DataGroup               dataGroup;
    protected List<DataList>          data;
    protected List<DataListener>      subscribed;

    public DataModel ()
    {
        data = new ArrayList<DataList>();
        subscribed = new ArrayList<DataListener>();
        dataGroup = DataGroup.MAX_GROUPS;
    }

//    public static DataModel getInstance()
//    {
//        if( instance == null )
//            instance = new DataModel();
//        return instance;
//    }
    
    /**
     * List Management
     */
    
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
    
    /**
     * Storage Management
     */
    
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
    
    /**
     * Dispatcher Management
     */
    
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
