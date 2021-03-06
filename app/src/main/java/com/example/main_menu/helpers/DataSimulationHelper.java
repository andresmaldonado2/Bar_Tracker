package com.example.main_menu.helpers;

import androidx.annotation.VisibleForTesting;
import java.util.Random;

// We're gonna say most reps go from about 1 second to 3+ seconds
// Source: Dude Trust Me TM
// TODO Find actual published journal source for this, I know its true I just gotta find it in the literature
public class DataSimulationHelper
{
    private Random rand;
    private final static double HEIGHT_OF_BAR_PATH = 12;
    // for the sake of demonstration however, we'll say this individual can survive a real grinder of a rep
    // That way its more obvious the time is increasing
    private final double MAXIMUM_LIFT_TIME = 3.75;
    // Because of sin wave used in simulation, the number used in the formula is doubled
    private final double CALC_HEIGHT = HEIGHT_OF_BAR_PATH / 2;
    private final double STARTING_CONCENTRIC_TIME_INTERVAL = 1.2;
    private final double ECCENTRIC_TIME_INTERVAL = 0.5;
    private final double DISTANCE_OF_TRACKER_FROM_BAR;
    private double timeElapsed;

    private boolean concentricPath;
    private double timeInterval;
    private double totalTimeElapsed;
    private double initialTimeInterval;
    private int numberOfRepsPerformed;
    private int numberOfExpectedReps;


    public DataSimulationHelper(int expectedReps, double distanceFromBar)
    {
        timeElapsed = 0;
        timeInterval = STARTING_CONCENTRIC_TIME_INTERVAL;
        totalTimeElapsed = 0.0;
        initialTimeInterval = 0.016;
        rand = new Random();
        numberOfRepsPerformed = 0;
        concentricPath = true;
        numberOfExpectedReps = expectedReps;
        DISTANCE_OF_TRACKER_FROM_BAR = distanceFromBar;
    }

    private Double calculateYDataPoint()
    {
        if(timeInterval < MAXIMUM_LIFT_TIME) //&& numberOfRepsPerformed < numberOfExpectedReps)
        {
            if(concentricPath)
            {
                double temp;
                temp = -1 * CALC_HEIGHT * Math.sin(((Math.PI / timeInterval) * timeElapsed) + (0.5 * Math.PI)) + CALC_HEIGHT;
                totalTimeElapsed = totalTimeElapsed + initialTimeInterval;
                timeElapsed = timeElapsed + initialTimeInterval;
                if(timeElapsed > timeInterval)
                {
                    concentricPath = false;
                    timeElapsed = 0;
                }
                return temp;
            }
            else
            {
                double temp;
                temp= CALC_HEIGHT * Math.sin(((Math.PI / ECCENTRIC_TIME_INTERVAL) * timeElapsed) + (0.5 * Math.PI)) + CALC_HEIGHT;
                totalTimeElapsed = totalTimeElapsed + initialTimeInterval;
                timeElapsed = timeElapsed + initialTimeInterval;
                if(timeElapsed > ECCENTRIC_TIME_INTERVAL)
                {
                    concentricPath = true;
                    timeElapsed = 0;
                    numberOfRepsPerformed++;
                    timeInterval = timeInterval + generateIntervalIncrease(numberOfRepsPerformed, numberOfExpectedReps);
                }
                return temp;
            }
        }
        else
        {
            return null;
        }
    }
    public double[] nextDataPoint()
    {
        Double temp = calculateYDataPoint();
        if(temp != null)
        {
            double distance = Math.sqrt(Math.pow(temp,2) + Math.pow(DISTANCE_OF_TRACKER_FROM_BAR, 2));
            // It shouldn't occur with how the math is written but technically speaking it is possible
            // For this to result in NaN and I should check to see that it hasn't
            double tempAngle = Math.asin(temp/distance);
            if(Double.isNaN(tempAngle))
            {
                throw new RuntimeException("Angle is measured is physically impossible, check sensor for damage");
            }
            double angle = Math.toDegrees(tempAngle);
            double[] dataPoint = new double[2];
            dataPoint[0] = distance;
            dataPoint[1] = angle;
            return dataPoint;
        }
        return null;
    }
    private double generateIntervalIncrease(int numberOfRepsPerformed, int numberOfExpectedReps)
    {
        double intervalChange = rand.nextDouble();
        // Equation works out so that when #ofRepsPerformed == #ofExpectedReps a increase in time is guaranteed
        // since f(#ofExpectedReps) always equals 1.0
        if(intervalChange < ((1.0/Math.pow(numberOfExpectedReps, 3.0)) * Math.pow(numberOfRepsPerformed,3.0)))
        {
            return intervalChange;
        }
        else
        {
            return 0.0;
        }
    }
    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public boolean getConcentricPath()
    {
        return concentricPath;
    }
    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public int getNumberOfRepsPerformed()
    {
        return numberOfRepsPerformed;
    }
}
