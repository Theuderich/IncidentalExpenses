/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package charts;

import consumption.DataListCounters;
import consumption.DataModelCounter;
import consumption.DataType;
import consumption.ListEntry;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import postprocess.NormDelta;
import postprocess.ppu;
import postprocess.ppuModel;
import postprocess.ppuNormDelta;

/**
 *
 * @author mabarthe
 */
public class AnnualConsumptionProgress extends ChartBasic 
{
    public AnnualConsumptionProgress (DataType t)
    {
        super(t);
    }
    
    public void configureXAxis(JFreeChart chart)
    {
        DateAxis axis = (DateAxis) chart.getXYPlot().getDomainAxis();
        axis.setTickUnit(new DateTickUnit(DateTickUnitType.MONTH,1));
        axis.setTickMarkPosition(DateTickMarkPosition.START);
        axis.setDateFormatOverride(new SimpleDateFormat("MMM"));
        axis.setLabel("Month");
    }
      
    public XYDataset createDataset( ) 
    {
        ppuModel.getInst().setCounterType(type);
        DataModelCounter.getInstance().dataHasChanged();
        Iterator<NormDelta> it = ppuNormDelta.getInst().getIterator();
        
        TimeSeriesCollection collection = new TimeSeriesCollection();
        TimeSeries series = null;
        
        while(it.hasNext())
        {
            NormDelta cur = it.next();
            Day start;
            Day stop;
            
            if( series == null )
            {
                series = new TimeSeries( String.format("%d", cur.start.getYear()+1900) );
            }
            
            start = new Day(cur.start.getDayOfMonth(), cur.start.getMonthOfYear(), 1900);
            series.addOrUpdate(start, cur.average);

            if( cur.start.getYear() != cur.stop.getYear())
            {
                stop = new Day( 31, 12, 1900);
                series.addOrUpdate( stop , cur.average);
                
                collection.addSeries(series);
                series = new TimeSeries( String.format("%d", cur.stop.getYear()+1900) );
                
                start = new Day(1, 1, 1900);
                series.addOrUpdate( start, cur.average);
            }

            Calendar c = Calendar.getInstance();
            c.setTime( cur.stop );
            c.add( Calendar.DATE, -1);
            stop = new Day( c.get( Calendar.DAY_OF_MONTH ), c.get( Calendar.MONTH ) + 1, 1900);
            series.addOrUpdate( stop , cur.average);
            
        }

        collection.addSeries(series);
        return collection;
   }    
}
