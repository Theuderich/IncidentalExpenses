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
public class DataListCounters extends DataList {
    
    public DataListCounters( DataType type )
    {
        super( type );
    }
    
    public double getTotalAverage()
    {
        ListEntry eStart = this.datalist.get(0);
        ListEntry eStop = this.datalist.get( this.datalist.size()-1);
        
        return (eStop.getValue() - eStart.getValue())/((eStop.getTimestamp().getTime() - eStart.getTimestamp().getTime() )/1000/60/60/24);
    }
}
