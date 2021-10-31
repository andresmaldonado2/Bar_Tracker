package com.example.main_menu.helpers;


import android.util.Log;

import java.util.Random;

// We're gonna say most reps go from about 1 second to 3+ seconds
// Source: Dude Trust Me TM
// TODO Find actual published journal source for this, I know its true I just gotta find it in the literature
public class DataSimulationHelper
{
    // This is roughly .26 seconds, or a little over a quarter of a second
    // Keeping it in PI makes things 1000% easier, trust me, just keep it in terms of PI
    private static Random rand;
    private final static double HEIGHT_OF_BAR_PATH = 12;
    // Because of sin wave used in simulation, the number used in the formula is doubled
    private final static double CALC_HEIGHT = HEIGHT_OF_BAR_PATH / 2;
    private final static double STARTING_CONCENTRIC_TIME_INTERVAL = 1.2;
    private final static double ECCENTRIC_TIME_INTERVAL = 1.2;

    private static double timeElapsed;
    private static boolean concentricPath;
    private static double timeInterval;
    private static double totalTimeElapsed;
    private static double initialTimeInterval;
    private static int numberOfRepsPerformed;
    private static int numberOfExpectedReps;

    public DataSimulationHelper(int expectedReps)
    {
        timeElapsed = 0;
        timeInterval = STARTING_CONCENTRIC_TIME_INTERVAL;
        totalTimeElapsed = 0.0;
        initialTimeInterval = 0.016;
        rand = new Random();
        numberOfRepsPerformed = 0;
        concentricPath = true;
        numberOfExpectedReps = expectedReps;
        Log.d("DATASIM", "Sim objected created");
    }

    public double[] nextDataPoint()
    {
        if(timeInterval < 4.0 && numberOfRepsPerformed < numberOfExpectedReps)
        {
            Log.d("DATASIM", "Number of reps: " + Integer.toString(numberOfRepsPerformed));
            if(concentricPath)
            {
                double[] temp = new double[2];
                temp[0] = totalTimeElapsed;
                temp[1] = -1 * CALC_HEIGHT * Math.sin(((Math.PI / timeInterval) * timeElapsed) + (0.5 * Math.PI)) + CALC_HEIGHT;
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
                double[] temp = new double[2];
                temp[0] = totalTimeElapsed;
                temp[1] = CALC_HEIGHT * Math.sin(((Math.PI / ECCENTRIC_TIME_INTERVAL) * timeElapsed) + (0.5 * Math.PI)) + CALC_HEIGHT;
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
        /*
        while(timeInterval < 4.0)
        {
            for (double timeElapsed = 0; timeElapsed <= timeInterval; timeElapsed = timeElapsed + initialTimeInterval)
            {
                ArrayList<Double> temp = new ArrayList<>();
                temp.add(totalTimeElapsed);
                temp.add(-1 * CALC_HEIGHT * Math.sin(((Math.PI / timeInterval) * timeElapsed) + (0.5 * Math.PI)) + CALC_HEIGHT);
                simData.add(temp);
                //TODO May need to change it so a data point is added at the very top of the sine wave, not sure yet
                totalTimeElapsed = totalTimeElapsed + initialTimeInterval;
            }
            for(double timeElapsed = 0; timeElapsed <= CONCENTRIC_TIME_INTERVAL; timeElapsed = timeElapsed + initialTimeInterval)
            {
                ArrayList<Double> temp = new ArrayList<>();
                temp.add(totalTimeElapsed);
                temp.add(CALC_HEIGHT * Math.sin(((Math.PI / CONCENTRIC_TIME_INTERVAL) * timeElapsed) + (0.5 * Math.PI)) + CALC_HEIGHT);
                simData.add(temp);
                totalTimeElapsed = totalTimeElapsed + initialTimeInterval;
            }
            numberOfRepsPerformed++;
            timeInterval = timeInterval + generateIntervalIncrease(numberOfRepsPerformed, numberOfExpectedReps);
        }
         */
    }
    private static double generateIntervalIncrease(int numberOfRepsPerformed, int numberOfExpectedReps)
    {
        double intervalChange = rand.nextDouble();
        // Equation works out so that when #ofRepsPerformed == #ofExpectedReps a increase in time is guaranteed
        // since f(#ofExpectedReps) always equals 1.0
        if(intervalChange > ((1.0/Math.pow(numberOfExpectedReps, 3.0)) * Math.pow(numberOfRepsPerformed,3.0)))
        {
            return intervalChange * 0.75;
        }
        else
        {
            return 0.0;
        }
    }
}
