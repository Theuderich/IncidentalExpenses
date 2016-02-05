/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package consumption;

/**
 *
 * @author mabarthe
 */
public enum DataType {
    OIL_TANK        (0, DataGroup.COUNTER, -1), 
    OIL_VOLUME      (1, DataGroup.COUNTER, -1), 
    ELECTRICITY     (2, DataGroup.COUNTER, -1), 
    WATER_ALL       (3, DataGroup.COUNTER, -1),
    WATER_GARDEN    (4, DataGroup.COUNTER, -1),
    TEMPERATURE     (5, DataGroup.ENVIRONMENT, 16), 
    HUMIDITY        (6, DataGroup.ENVIRONMENT, 17),
    WINDSPEED       (7, DataGroup.ENVIRONMENT, 14),
    WINDDIRECTION   (8, DataGroup.ENVIRONMENT, 15),
    AIRPRESURE      (9, DataGroup.ENVIRONMENT, 20),
    SUNPOWER        (10, DataGroup.ENVIRONMENT, 28),
    RAIN            (11, DataGroup.ENVIRONMENT, 29),
    MAX_TYPES       (12, DataGroup.MAX_GROUPS, -1);
    
    private int localIndex;
    private DataGroup group;
    private int webIndex;
    
    private DataType (int local, DataGroup group_, int web)
    {
        localIndex = local;
        group = group_;
        webIndex = web;
    }
    
    public int getLocalIndex()
    {
        return localIndex;
    }
    
    public int getWebIndex ()
    {
        return webIndex;
    }
   
    public DataGroup getGroup ()
    {
        return group;
    }
   
    public static DataType getItem( int value_ )
    {
        for (DataType item : DataType.values()) {
          if (item.localIndex == value_) 
              return item;
      }
      throw new IllegalArgumentException("Invalid Item Id?");
    }
    
    
    @Override public String toString()
    {
        String sName;
        
        switch( ordinal() )
        {
            case 0:
                sName="Oil Tank";
                break;
                
            case 1:
                sName="Oil Volume";
                break;
                
            case 2:
                sName="Electricity";
                break;
                
            case 3:
                sName = "Water All";
                break;
                
            case 4:
                sName = "Water Garden";
                break;

            case 5:
                sName = "Temperature";
                break;
                
            case 6:
                sName = "Humidity";
                break;
                
            case 7:
                sName = "Wind Speed";
                break;
                
            case 8:
                sName = "Wind Direction";
                break;
                
            case 9:
                sName = "Air Pressure";
                break;
                
            case 10:
                sName = "Solar Radiation";
                break;
            
            case 11:
                sName = "Amount Of Precipitation";
                break;
                
            default:
                sName="Unknown ConsType";
                break;
        }
        return sName;
    }
    
}
