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
import java.util.Iterator;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author mabarthe
 */
public class ChartBasic extends ApplicationFrame
{
    private DataType type;
    
   public ChartBasic( DataType t ) 
   {
      super( t.toString() ); 
      type = t;
      setContentPane(createPanel( ));
   }
   
   private XYDataset createDataset( ) 
   {
        DataListCounters list;
        list = DataModelCounter.getInstance().getConsList( type );
        
//        TimeSeriesCollection data = new TimeSeriesCollection();
//        XYDataset data=createTimeDataset(values);
        
        
        XYSeries series1 = new XYSeries("Series 1");
        
        ListEntry old = null;
        
        Iterator<ListEntry> it = list.getIterator();
        while(it.hasNext())
        {
            ListEntry cur = it.next();
            if( old != null )
            {
                
                series1.add(cur.getTimestamp().getTime(), (cur.getValue() - old.getValue())/((cur.getTimestamp().getTime() - old.getTimestamp().getTime())/(1000*60*60*24)) );
                System.out.println(String.format("%d: %f, %d", cur.getTimestamp().getTime(), (cur.getValue() - old.getValue()) / ((cur.getTimestamp().getTime() - old.getTimestamp().getTime())/(1000*60*60*24)), (cur.getTimestamp().getTime() - old.getTimestamp().getTime()) ));
            }
            old = cur;
            
        }
        
//        series1.add(10.0, 12353.3);
//        series1.add(20.0, 13734.4);
//        series1.add(30.0, 14525.3);
//        series1.add(40.0, 13984.3);
//        series1.add(50.0, 12999.4);
//        series1.add(60.0, 14274.3);
//        series1.add(70.0, 15943.5);
//        series1.add(80.0, 14845.3);
//        series1.add(90.0, 14645.4);
//        series1.add(100.0, 16234.6);
//        series1.add(110.0, 17232.3);
//        series1.add(120.0, 14232.2);
//        series1.add(130.0, 13102.2);
//        series1.add(140.0, 14230.2);
//        series1.add(150.0, 11235.2);        
        
//        Iterator<ListEntry>     iterator = list.getIterator();
//        while(iterator.hasNext())
//        {
//            data.
//            System.out.println(String.format("%03d: %s", i++, iterator.next().toString()));
//        }
        
       
//       DefaultPieDataset dataset = new DefaultPieDataset( );
//      dataset.setValue( "IPhone 5s" , new Double( 20 ) );  
//      dataset.setValue( "SamSung Grand" , new Double( 20 ) );   
//      dataset.setValue( "MotoG" , new Double( 40 ) );    
//      dataset.setValue( "Nokia Lumia" , new Double( 10 ) );  
      return new XYSeriesCollection(series1);
         
   }
   
   private JFreeChart createChart( XYDataset dataset )
   {
      JFreeChart chart = ChartFactory.createTimeSeriesChart(      
         type.toString(),  // chart title 
         "Time",
         "Consumption",
         dataset,        // data    
         false,           // include legend   
         true, 
         true);

        DateAxis axis = (DateAxis) chart.getXYPlot().getDomainAxis();
	axis.setTickUnit(new DateTickUnit(DateTickUnitType.DAY,1));
	axis.setTickMarkPosition(DateTickMarkPosition.START);
	axis.setDateFormatOverride(new SimpleDateFormat("dd"));
      
      return chart;
   }
   
   public JPanel createPanel( )
   {
      JFreeChart chart = createChart(createDataset( ) );  
      return new ChartPanel( chart ); 
   }
   
   public void run()
   {
        setSize( 560 , 367 );    
        RefineryUtilities.centerFrameOnScreen( this );    
        setVisible( true ); 
   }
       
    
}
