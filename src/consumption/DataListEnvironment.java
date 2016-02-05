/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consumption;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author mabarthe
 */
public class DataListEnvironment extends DataList {
    
    private static final String urlArchive = "http://umweltdaten.nuernberg.de/wetterdaten/messstation-nuernberg-flugfeld/archiv.html";
    private static final String urlTempWeek = "http://umweltdaten.nuernberg.de/wetterdaten/messstation-nuernberg-flugfeld/lufttemperatur.html";

    private static final String WEB_DATE_FORMAT = "dd.MM.yyyy HH:mm";
    private static final String WEB_DATE_REQUEST = "dd.MM.yyyy";

    SimpleDateFormat sdfDecode;
    SimpleDateFormat sdfRequest;
    
    public DataListEnvironment( DataType type )
    {
        super( type );
        sdfDecode = new SimpleDateFormat(WEB_DATE_FORMAT);
        sdfRequest = new SimpleDateFormat(WEB_DATE_REQUEST);
    }

    private Date parseDate( String str, String year ) 
    {
        Date point;
        
        String[] date = str.split(" ");
        date[0] += year;
        String time = String.join(" ", date);

        try {
            point = sdfDecode.parse(time);
        } catch (ParseException ex) {
            point = new Date();
            Logger.getLogger(DataListEnvironment.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return point;
    }

    private Date parseDate( String str )
    {
        Date point;
        
        try {
            point = sdfDecode.parse(str);
        } catch (ParseException ex) {
            point = new Date();
            Logger.getLogger(DataListEnvironment.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return point;
    }

    private double parseValue( String str ) 
    {
        double d;

        String temp = str.replace(",", ".");
        
        try{
            d = Double.parseDouble(temp);
        } catch ( NumberFormatException e ) {
            d = Double.NaN;
        }
        
        return d;
    }

    private ListEntry parseCells( Elements sCells, String year ) 
    {
        ListEntry e;
        Date    date;
        double  value;
        
        date = parseDate( sCells.get(0).text(), year );
        value = parseValue( sCells.get(1).text() );
        e = new ListEntry( date.getTime(), value );
        
        return e;
    }
    
    private ListEntry parseCells( Elements sCells ) 
    {
        ListEntry e;
        Date    date;
        double  value;
        
        date = parseDate( sCells.get(0).text() );
        value = parseValue( sCells.get(1).text() );
        e = new ListEntry( date.getTime(), value );
        
        return e;
    }

    public void loadArchiveData( Date start, Date stop )
    {
        try {
            
            
            
            Document doc = Jsoup.connect( urlArchive )
                    .data("tx_suncharts_pi1[datum_von]", sdfRequest.format(start))
                    .data("tx_suncharts_pi1[datum_bis]", sdfRequest.format(stop))
                    .data("tx_suncharts_pi1[messstation]", "1")
                    .data("tx_suncharts_pi1[messwerttyp]", String.format("%d", this.dataType.getWebIndex()))
                    .data("submit", "1")
                    .post();

            Elements messwerTabelle = doc.select("table[class=messwert-tabelle]");

            for (Element table : messwerTabelle) 
            {
                Elements rows = table.select("tr[class~=.*gerade]");
                
                for(Element row : rows)
                {
                   
                    Elements cell = row.select("td");
                    ListEntry e =  parseCells( cell );
                    if(this.add( e ) )
                    {
                        System.out.println( e.toString() );
                    }
                }                    
            }
            
            this.sortByTimestamp();
            
            System.out.println("feching data finished.");
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());

        }  
        
    }
      
    
    
}
