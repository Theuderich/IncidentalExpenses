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
public abstract class ChartBasic extends ApplicationFrame
{
    protected DataType type;
    
    public ChartBasic( DataType t ) 
    {
        super( t.toString() ); 
        type = t;
        setContentPane(createPanel( ));
    }
   
    public XYDataset createDataset( ) 
    {
        /* Place your data series gereration here.*/

        // Dummy dataset generation
        XYSeries series1 = new XYSeries("Series 1");
        series1.add(10.0, 12353.3);
        series1.add(20.0, 13734.4);
        series1.add(30.0, 14525.3);
        series1.add(40.0, 13984.3);
        series1.add(50.0, 12999.4);
        series1.add(60.0, 14274.3);
        series1.add(70.0, 15943.5);
        series1.add(80.0, 14845.3);
        series1.add(90.0, 14645.4);
        series1.add(100.0, 16234.6);
        series1.add(110.0, 17232.3);
        series1.add(120.0, 14232.2);
        series1.add(130.0, 13102.2);
        series1.add(140.0, 14230.2);
        series1.add(150.0, 11235.2);        
        
        return new XYSeriesCollection(series1);

    }
   
   private JFreeChart createChart( XYDataset dataset )
   {
      JFreeChart chart = ChartFactory.createTimeSeriesChart(      
         type.toString(),  // chart title 
         "Time",
         "Consumption",
         dataset,        // data    
         true,           // include legend   
         true, 
         true);
      
      return chart;
   }
   
   public void configureXAxis(JFreeChart chart)
   {
       /* Place your axis format here */
   }
   
   public JPanel createPanel( )
   {
      JFreeChart chart = createChart(createDataset( ) );  
      configureXAxis( chart );
      return new ChartPanel( chart ); 
   }
   
   public void run()
   {
        setSize( 560 , 367 );    
        RefineryUtilities.centerFrameOnScreen( this );    
        setVisible( true ); 
   }
       
    
}