/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import consumption.DataListCounters;
import consumption.DataModelCounter;
import consumption.DataModelEnvironment;
import consumption.DataType;
import consumption.ListEntry;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import postprocess.ppuModel;
import postprocess.ppuNormDelta;

/**
 *
 * @author mabarthe
 */
public class IncidentalExpenses {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParseException {
        // TODO code application logic here
        
        ppuModel ppu = ppuModel.getInst();
        
        DataModelCounter dbCounter = DataModelCounter.getInstance();
        DataModelEnvironment dbEnv = DataModelEnvironment.getInstance();
        
        dbCounter.loadFromFile();
        dbEnv.loadFromFile();

        dbCounter.dataHasChanged();
        
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMAN);

        String datum = "15.10.2015 12:20:00";
        DataListCounters list;
        
        list = dbCounter.getConsList(DataType.OIL_TANK);
        list.add( new ListEntry( format.parse(datum).getTime(), 80));

//        list = dbCounter.getConsList(DataType.ELECTRICITY);
//        list.add( new ListEntry( format.parse(datum).getTime(), 79642.6));
//        list.add( new ListEntry( format.parse("18.06.2015 07:15:00").getTime(), 77420.4));
//        list.add( new ListEntry( format.parse("19.06.2015 08:51:00").getTime(), 77431.7));
//        
//        list = dbCounter.getConsList(DataType.WATER_ALL);
//        list.add( new ListEntry( format.parse(datum).getTime(), 1014.400));
//        ^
//        list = dbCounter.getConsList(DataType.WATER_GARDEN);
//        list.add( new ListEntry( format.parse(datum).getTime(), 115.556));
        
        dbCounter.saveToFile();
        
//        dbEnv.getConsList(DataType.TEMPERATURE).loadArchiveData( format.parse("17.06.2015 00:00:00"), format.parse("17.06.2015 00:00:00"));
//        dbEnv.saveToFile();
        
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
