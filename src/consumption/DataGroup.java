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
public enum DataGroup {
    COUNTER         (0),
    ENVIRONMENT     (1),
    CONTRACT        (2),
    MAX_GROUPS      (3);
    
    private int index;
    
    private DataGroup( int index_ )
    {
        index = index_;
    }
    
    public static DataGroup getItem( int value_ )
    {
        for (DataGroup item : DataGroup.values()) {
          if (item.index == value_) 
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
                sName="Counter";
                break;
                
            case 1:
                sName="Environment";
                break;
                
            case 2:
                sName="Contract";
                break;
                
            default:
                sName="Unknown DataGroup";
                break;
        }
        return sName;
    }
        
}
