package com.example.main_menu.helpers;


import java.util.Random;

// We're gonna say most reps go from about 1 second to 3+ seconds
// Source: Dude Trust Me TM
// TODO Find actual published journal source for this, I know its true I just gotta find it in the literature
public class DataSimulationHelper
{
    Random rand;
    double timeInterval;
    public DataSimulationHelper(double timeInterval)
    {
        rand = new Random();
        this.timeInterval = timeInterval;
    }
    public double[][] createSimulationData(int arrayLength, int numberOfExpectedReps)
    {
        double[][] simData = new double[arrayLength][2];
        double totalTimeElapsed = 0;
        for (int i = 0; i < arrayLength; i++)
        {
            simData[i][0] = totalTimeElapsed;
            simData[i][1] = -Math.sin(i - Math.PI);
            timeInterval = timeInterval + generateIntervalIncrease(totalTimeElapsed, numberOfExpectedReps);
            totalTimeElapsed = totalTimeElapsed + timeInterval;
        }
        return simData;
    }
    public double generateIntervalIncrease(double timeElapsed, int numberOfExpectedReps)
    {
        double intervalChange = rand.nextDouble();
        if(intervalChange > Math.exp((numberOfExpectedReps/3) / timeElapsed))
        {
            return intervalChange;
        }
        else
        {
            return 0.0;
        }
    }
}
