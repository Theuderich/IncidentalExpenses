/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consumption;

import java.util.Iterator;

/**
 *
 * @author mabarthe
 */
public class DataModelCounter extends DataModel {
    
    
    /**
     * Singleton implementation
     */
    
    private static DataModelCounter instance;
    
    private DataModelCounter()
    {
        super();
        dataGroup = DataGroup.COUNTER;
        for( int i=0; i<DataType.MAX_TYPES.getLocalIndex(); i++)
            if( DataType.getItem(i).getGroup().equals( dataGroup ) )
                data.add( new DataListCounters( DataType.getItem(i) ) );
        
    }
    
    public static DataModelCounter getInstance()
    {
        if( instance == null )
            instance = new DataModelCounter();
        return instance;
    }
    
    public DataListCounters getConsList( DataType type )
    {
        if( !type.getGroup().equals( DataGroup.COUNTER) )
            return null;
        
        return (DataListCounters) super.getConsList(type);
        
    }
    
}
