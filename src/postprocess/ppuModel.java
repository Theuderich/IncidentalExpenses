/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package postprocess;

import consumption.DataListener;
import consumption.DataType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author mabarthe
 */
public class ppuModel {
    
    static private ppuModel inst = null;
    List<ppuListener>   subscribed;
    
    private consumption.DataType counterType;
    
    private ppuModel()
    {
        subscribed = new ArrayList<ppuListener>();
        
        counterType = consumption.DataType.ELECTRICITY;
        ppuNormDelta.getInst();
    }
    
    static public ppuModel getInst()
    {
        if( inst == null )
        {
            inst = new ppuModel();
        }
        return inst;
    }
    
   /**
     * Dispatcher Management
     */
    
    public boolean existsListener( ppuListener obj )
    {
        boolean                     status = false;
        Iterator<ppuListener>      itr = subscribed.iterator();
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
    
    public void subscribe( ppuListener obj )
    {
        if( !existsListener(obj) )
        {
            subscribed.add(obj);
        }
    }

    public void dataHasChanged( )
    {
        Iterator<ppuListener> itr = subscribed.iterator();
        while( itr.hasNext() )
        {
            ppuListener obj = itr.next();
            obj.notifyPPUChanged();
        }
    }

    /**
     * PPU Selector
     */
    
    public DataType getCounterType() {
        return counterType;
    }

    public void setCounterType(DataType counterType) {
        this.counterType = counterType;
    }

    
    
}
