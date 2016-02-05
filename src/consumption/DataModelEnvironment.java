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
public class DataModelEnvironment extends DataModel {
    
    
    /**
     * Singleton implementation
     */
    
    private static DataModelEnvironment instance;
    
    private DataModelEnvironment()
    {
        super();
        dataGroup = DataGroup.ENVIRONMENT;
        for( int i=0; i<DataType.MAX_TYPES.getLocalIndex(); i++)
            if( DataType.getItem(i).getGroup().equals( dataGroup ) )
                data.add( new DataListEnvironment( DataType.getItem(i) ) );
    }
    
    public static DataModelEnvironment getInstance()
    {
        if( instance == null )
            instance = new DataModelEnvironment();
        return instance;
    }
    
    public DataListEnvironment getConsList( DataType type )
    {
        if( !type.getGroup().equals( DataGroup.ENVIRONMENT) )
            return null;
        
        return (DataListEnvironment) super.getConsList(type);
    }
    
    
}
