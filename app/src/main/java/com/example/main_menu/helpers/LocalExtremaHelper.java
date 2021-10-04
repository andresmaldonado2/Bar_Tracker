package com.example.main_menu.helpers;
import java.util.ArrayList;

public class LocalExtremaHelper {
    // TODO change the return type to a normal array rather than an arraylist
    // TODO Check and see if its even necessary to send over x values?
    public static ArrayList<ArrayList<Double>> calculatePositionalData(ArrayList<ArrayList<Double>> data)
    {
        ArrayList<ArrayList<Double>> positionData = new ArrayList<ArrayList<Double>>();
        for (int x = 0; x < data.size(); x++)
        {
            ArrayList<Double> row = new ArrayList<>();
            row.add(0, calculateXCoordinate(data.get(x).get(0), data.get(x).get(1)));
            row.add(1, calculateYCoordinate(data.get(x).get(0), data.get(x).get(1)));
            positionData.add(x, row);
        }
        return positionData;
    }
    public static double calculateXCoordinate(double distance, double angle)
    {
        return distance * Math.sin(90.0 - angle);
    }
    public static double calculateYCoordinate(double distance, double angle)
    {
        return distance * Math.sin(angle);
    }

}
