/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import charts.AnnualConsumptionProgress;
import charts.ChartBasic;
import consumption.DataListCounters;
import consumption.DataModelCounter;
import consumption.DataModelEnvironment;
import consumption.DataType;
import consumption.ListEntry;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import postprocess.ppuModel;
import postprocess.ppuNormDelta;


import org.jfree.ui.RefineryUtilities;
/**
 *
 * @author mabarthe
 */
public class IncidentalExpenses   {

    private static Options  options = new Options();
    private static Option   oHelp = new Option( "h", "help", false, "print this message." );
    private static Option   oTimeformat = new Option("f", "datetimeformat", true, "Changes the expected date time format pattern (dd.MM.yyyy HH:mm:ss).");
    private static Option   oTimestamp = new Option("t", "timestamp", true, "Takes the time stamp of the counter reading in the format of -datetimeformat. If not given, use surrent time.");
    private static Option   oTypeElectricity = new Option("e", "electricity", true, "Takes the counter value of type electricity (kW/h).");
    private static Option   oTypeGas = new Option("g", "gas", true, "Takes the counter value of type gas (m^3).");
    private static Option   oTypeWater = new Option("w", "water", true, "Takes the counter value of type water (m^3).");
    private static Option   oTypeOil = new Option("o", "oil", true, "Takes the counter value of type oil (cm).");
    private static Option   oStats = new Option( "s", "statistics", false, "Prints a table with statistcs." );
    private static Option   oPrintList = new Option("p", "printlist", true, "Prints the counter list for the given DataType.");
    private static Option   oChartConsumption = new Option("c", "chartconsumption", true, "JFreeChart printing the consumption.");
    
    private static CommandLineParser parser;
    private static CommandLine cmd;        
    
    private static String   dateFormat = "dd.MM.yyyy HH:mm:ss";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParseException, org.apache.commons.cli.ParseException {
        // TODO code application logic here
        
        boolean     printHelp = false;
        
        long        ticks = 0;
        boolean     dataFound = false;
        
        options.addOption(oHelp);
        options.addOption(oTimeformat);
        options.addOption(oTimestamp);
        options.addOption(oTypeElectricity);
//        options.addOption(oTypeGas);
        options.addOption(oTypeWater);
        options.addOption(oTypeOil);
        options.addOption(oStats);
        options.addOption(oPrintList);
        
        options.addOption(oChartConsumption);
        
        parser = new DefaultParser();
        cmd = parser.parse( options, args);

        // instantiate singletons
        ppuModel ppu = ppuModel.getInst();
        DataModelCounter dbCounter = DataModelCounter.getInstance();
        DataModelEnvironment dbEnv = DataModelEnvironment.getInstance();
        
        // restore counter lists from file
        dbCounter.loadFromFile();
        dbEnv.loadFromFile();
        dbCounter.dataHasChanged();
        
        if( cmd != null )
        {
            if(args.length == 0)
                printHelp = true;

            if( cmd.hasOption( oHelp.getOpt() ))
                printHelp = true;
        }

        if(printHelp)
        {
            HelpFormatter formatter = new HelpFormatter();
            formatter.setWidth(1000);
            formatter.printHelp( "IncidentalExpenses", options );
            
            System.out.println();
            System.out.println("Available DataTypes:");
            for(int i=0; i<DataType.MAX_TYPES.getLocalIndex(); i++)
                System.out.println( String.format(" - %-25s: %d", DataType.getItem(i).toString(), i));
            System.exit(-1);
        }
        
        /***********************************************************************
         * Time format and time point related
         **********************************************************************/
        
        if( cmd.hasOption( oTimeformat.getOpt() ))
        {
            dateFormat=cmd.getOptionValue( oTimeformat.getOpt() );
        }
        
        DateFormat format = new SimpleDateFormat(dateFormat, Locale.GERMAN);
        
        if( cmd.hasOption( oTimestamp.getOpt() ))
        {
            // secode time from command line argunebt
            String      time;
            time=cmd.getOptionValue( oTimestamp.getOpt() );
            ticks = format.parse(time).getTime();
            System.out.println(String.format("Text: %s\n", time));
        }
        else
        {
            // if option is not set, use current time
            ticks = System.currentTimeMillis();
        }

//        System.out.println(String.format("Tick: %d", ticks));
//        System.out.println(String.format("Date Time: %s", format.format( new Date(ticks))));

        /***********************************************************************
         * Insert new counter values 
         **********************************************************************/
        
        if( cmd.hasOption( oTypeElectricity.getOpt() ) )
        {
            DataListCounters list;
            list = dbCounter.getConsList(DataType.ELECTRICITY);
            double counter = Double.parseDouble( cmd.getOptionValue( oTypeElectricity.getOpt() ) );
            list.add( new ListEntry( ticks, counter));
            
            dataFound = true;
            list.printConsole();
        }
        
        if( cmd.hasOption( oTypeWater.getOpt() ) )
        {
            DataListCounters list;
            list = dbCounter.getConsList(DataType.WATER_ALL);
            double counter = Double.parseDouble( cmd.getOptionValue( oTypeWater.getOpt() ) );
            list.add( new ListEntry( ticks, counter));
            
            dataFound = true;
            list.printConsole();
        }
        
        if( cmd.hasOption( oTypeOil.getOpt() ) )
        {
            DataListCounters list;
            list = dbCounter.getConsList(DataType.OIL_TANK);
            double counter = Double.parseDouble( cmd.getOptionValue( oTypeOil.getOpt() ) );
            list.add( new ListEntry( ticks, counter));
            
            dataFound = true;
            list.printConsole();
        }
        
        if( dataFound )
        {
            dbCounter.saveToFile();
        }

        /***********************************************************************
         * Statistical post processing 
         **********************************************************************/
        
        if( cmd.hasOption( oPrintList.getOpt() ))
        {
            DataListCounters list;
            int id = Integer.parseInt( cmd.getOptionValue( oPrintList.getOpt() ) );
            DataType type = DataType.getItem(id);
            list = dbCounter.getConsList( type );
            list.printConsole();
        }
        
        if( cmd.hasOption( oChartConsumption.getOpt() ))
        {
            int id = Integer.parseInt( cmd.getOptionValue( oChartConsumption.getOpt() ) );
            DataType type = DataType.getItem(id);
            AnnualConsumptionProgress demo = new AnnualConsumptionProgress( type );  
            demo.run();
        }
        
                
        if( cmd.hasOption( oStats.getOpt() ))
        {
            DataListCounters list;
            
            dbCounter.printConsole();
            dbEnv.printConsole();

            ppu.setCounterType(DataType.ELECTRICITY);
            dbCounter.dataHasChanged();
            ppuNormDelta.getInst().printConsole();

            double  infraMonatlich = 0;
            double  preisProEinheit = 0.26;
            double  grundPreis = 89.61;
            String  f = "%-20s %10s %-10s";
            DecimalFormat df = new DecimalFormat("##0.000");

            list = dbCounter.getConsList(DataType.ELECTRICITY);
            System.out.println( String.format(f, "Total Average:", df.format(list.getTotalAverage()), "kW/d"));
            System.out.println( String.format(f, "Year Consumption", df.format(list.getTotalAverage()*365), "kW/y"));
            System.out.println( String.format(f, "Arbeitspreis:", df.format(list.getTotalAverage()*365*preisProEinheit), "€"));
            System.out.println( String.format(f, "Grundpreis:", df.format(grundPreis), "€"));
            System.out.println( String.format(f, "Gesamt:", df.format(list.getTotalAverage()*365*preisProEinheit+grundPreis), "€"));
            System.out.println( String.format(f, "Monatlich:", df.format((list.getTotalAverage()*365*preisProEinheit+grundPreis)/12), "€"));
            infraMonatlich += (list.getTotalAverage()*365*preisProEinheit+grundPreis)/12;
            System.out.println( );




            ppu.setCounterType(DataType.WATER_ALL);
            dbCounter.dataHasChanged();
            ppuNormDelta.getInst().printConsole();
            list = dbCounter.getConsList(DataType.WATER_ALL);

            preisProEinheit = 2.27;
            grundPreis = 98.35;
            System.out.println( String.format(f, "Total Average:", df.format(list.getTotalAverage()), "m3/d"));
            System.out.println( String.format(f, "Year Consumption", df.format(list.getTotalAverage()*365), "m3/y"));
            System.out.println( String.format(f, "Arbeitspreis:", df.format(list.getTotalAverage()*365*preisProEinheit), "€"));
            System.out.println( String.format(f, "Grundpreis:", df.format(grundPreis), "€"));
            System.out.println( String.format(f, "Gesamt:", df.format(list.getTotalAverage()*365*preisProEinheit+grundPreis), "€"));
            System.out.println( String.format(f, "Monatlich:", df.format((list.getTotalAverage()*365*preisProEinheit+grundPreis)/12), "€"));
            infraMonatlich += (list.getTotalAverage()*365*preisProEinheit+grundPreis)/12;
            System.out.println( );
    //        System.out.println( String.format("Average: %f m3/d", ppuNormDelta.getInst().getAverageValue()));
    //        System.out.println( String.format("Expected Year Consumption: %f m3/y", ppuNormDelta.getInst().getAverageValue()*365));
    //        System.out.println( String.format("Arbeitspreis: %f Euro", ppuNormDelta.getInst().getAverageValue()*365*preisProEinheit));
    //        System.out.println( String.format("Grundpreis: %f Euro", grundPreis));
    //        System.out.println( String.format("Gesamt: %f Euro", ppuNormDelta.getInst().getAverageValue()*365*preisProEinheit+grundPreis));
    //        System.out.println( String.format("Monatlich: %f Euro", (ppuNormDelta.getInst().getAverageValue()*365*preisProEinheit+grundPreis)/12));

            ppu.setCounterType(DataType.WATER_GARDEN);
            dbCounter.dataHasChanged();
            ppuNormDelta.getInst().printConsole();
            list = dbCounter.getConsList(DataType.WATER_GARDEN);

            preisProEinheit = -1.80;
            grundPreis = 0;
            System.out.println( String.format(f, "Total Average:", df.format(list.getTotalAverage()), "m3/d"));
            System.out.println( String.format(f, "Year Consumption", df.format(list.getTotalAverage()*365), "m3/y"));
            System.out.println( String.format(f, "Arbeitspreis:", df.format(list.getTotalAverage()*365*preisProEinheit), "€"));
            System.out.println( String.format(f, "Grundpreis:", df.format(grundPreis), "€"));
            System.out.println( String.format(f, "Gesamt:", df.format(list.getTotalAverage()*365*preisProEinheit+grundPreis), "€"));
            System.out.println( String.format(f, "Monatlich:", df.format((list.getTotalAverage()*365*preisProEinheit+grundPreis)/12), "€"));
            infraMonatlich += (list.getTotalAverage()*365*preisProEinheit+grundPreis)/12;
            System.out.println( );


            System.out.println( );
            System.out.println( String.format(f, "Infra Fürth:", df.format(infraMonatlich), "€"));
            System.out.println( );
            System.out.println( );

        }
        
        
        
    }
}
