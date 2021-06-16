package iti.mans.g1.mansouragroupone;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class AppEntry {

    public static void main(String[] args) throws Exception {
        
        AppEntry xChartExamples = new AppEntry ();
        List<TitanicData> allPassengers = xChartExamples.getPassengersFromJsonFile ();
        
        try {
            System.in.read();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(AppEntry.class.getName()).log(Level.SEVERE, null, ex);
        }
        xChartExamples.graphPassengerAges (allPassengers);
        xChartExamples.graphPassengerClass (allPassengers);
        xChartExamples.graphPassengerSex (allPassengers);
    }
    public List<TitanicData> getPassengersFromJsonFile() {
        List<TitanicData> allPassengers = new ArrayList<TitanicData> ();
        ObjectMapper objectMapper = new ObjectMapper ();
        objectMapper.configure (DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try (InputStream input = new FileInputStream ("D:\\Java_course\\day_4\\titanic_csv.json")) {
            allPassengers = objectMapper.readValue (input, new TypeReference<List<TitanicData>> () {
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
        return allPassengers;
    }

    public void graphPassengerAges(List<TitanicData> passengerList) {
        List<Float> pAges = passengerList.stream ().map (TitanicData::getAge).limit (8).collect (Collectors.toList ());
        List<String> pNames = passengerList.stream ().map (TitanicData::getName).limit (8).collect (Collectors.toList ());
        String[] names = new String[pNames.size ()];
        Float ages[] = new Float[pAges.size ()];
        ages = pAges.toArray (ages);
        names = pNames.toArray (names);        
        CategoryChart chart = new CategoryChartBuilder ().width (1024).height (768).title ("Age Histogram").xAxisTitle ("Names").yAxisTitle ("Age").build ();
        chart.getStyler ().setLegendPosition (Styler.LegendPosition.InsideNW);
        chart.getStyler ().setHasAnnotations (true);
        chart.getStyler ().setStacked (true);
        chart.addSeries ("Passenger's Ages", pNames, pAges);
        new SwingWrapper (chart).displayChart ();
    }

    public void graphPassengerClass(List<TitanicData> passengerList) {
        Map<String, Long> result =
                passengerList.stream ().collect (
                        Collectors.groupingBy (
                                TitanicData::getPclass, Collectors.counting ()                           
                        )
                );
        PieChart chart = new PieChartBuilder ().width (800).height (600).title (getClass ().getSimpleName ()).build ();
        Color[] sliceColors = new Color[]{new Color (180, 68, 50), new Color (130, 105, 120), new Color (80, 143, 160)};
        chart.getStyler ().setSeriesColors (sliceColors);
        chart.addSeries ("First Class", result.get ("1"));
        chart.addSeries ("Second Class", result.get ("2"));
        chart.addSeries ("Third Class", result.get ("3"));
        new SwingWrapper (chart).displayChart ();
    }
    
    public void graphPassengerSex(List<TitanicData> passengerList) {
        Map<String, Long> result =
                passengerList.stream ().collect (
                        Collectors.groupingBy (
                                TitanicData::getSex, Collectors.counting ()                           
                        )
                );
        PieChart chart2 = new PieChartBuilder ().width (800).height (600).title (getClass ().getSimpleName ()).build ();
        Color[] sliceColors2 = new Color[]{new Color (100,50,10), new Color (80,150,160)};
        chart2.getStyler ().setSeriesColors (sliceColors2);
        chart2.addSeries ("Males", result.get ("male"));
        chart2.addSeries ("Famales", result.get ("female"));
        new SwingWrapper (chart2).displayChart ();
    }
}
