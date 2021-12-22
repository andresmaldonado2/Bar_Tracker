package com.example.main_menu.helpers;

import java.util.Arrays;

public class PerformanceMetric
{
    private int weight;
    private boolean inKG;
    private int numOfRepsPerformed;
    private int startTickOfRep;
    private boolean isConcentric;
    private double initialVelocity;
    private double avgForce;
    private double peakForce;
    private double currentForce;
    private double forceDuringRep;
    private double avgVelocity;
    private double peakVelocity;
    private double currentVelocity;
    private double[] results;
    private double[] jniData;
    private double[] metrics;
    private int totalTickCount;
    private int concentricTickCount;
    private final double EPSILON = 0.001;

    public PerformanceMetric(int weight, boolean inKG)
    {
        this.weight = weight;
        this.inKG = inKG;
        numOfRepsPerformed = 0;
        startTickOfRep = 0;
        isConcentric = false;
        initialVelocity = 0.0;
        avgForce = 0.0;
        peakForce = 0.0;
        avgForce = 0.0;
        peakForce = 0.0;
        currentForce = 0.0;
        forceDuringRep = 0.0;
        avgVelocity = 0.0;
        peakVelocity = 0.0;
        currentVelocity = 0.0;
        totalTickCount = 0;
        concentricTickCount = 0;
        results = new double[] {0.0, 0.0, 0.0};
        jniData = new double[] {0.0, 0.0, 0.0, 0.0};
        metrics = new double[7];
    }
    public double[] getPerformanceMetrics(double[] dataPoint)
    {
        jniData[0] = jniData[2];
        jniData[1] = jniData[3];
        jniData[2] = dataPoint[0];
        jniData[3] = dataPoint[1];
        totalTickCount++;
        if (totalTickCount >= 2)
        {
            results = new PerformanceMetricHelper().performanceMetrics(jniData, weight, results[1], inKG);
            if (Math.abs(results[2] - 1.0) < EPSILON)
            {
                concentricTickCount++;
                if (!isConcentric)
                {
                    isConcentric = true;
                    startTickOfRep = concentricTickCount;
                }
                if (results[0] > peakForce)
                {
                    peakForce = results[0];
                }
                if (results[1] > peakVelocity)
                {
                    peakVelocity = results[1];
                }
                avgForce += results[0];
                avgVelocity += results[1];

                currentForce += results[0];
                currentVelocity = results[1];
            }
            else
            {
                if(isConcentric)
                {
                    forceDuringRep = currentForce / (concentricTickCount - startTickOfRep);
                    currentForce = 0.0;
                    numOfRepsPerformed++;
                    isConcentric = false;
                }
            }
            metrics[0] = avgForce / concentricTickCount;
            metrics[1] = peakForce;
            metrics[2] = forceDuringRep;
            metrics[3] = avgVelocity / concentricTickCount;
            metrics[4] = peakVelocity;
            metrics[5] = currentVelocity;
            metrics[6] = numOfRepsPerformed;
            System.out.println(Arrays.toString(metrics));
            return metrics;
        }
        else
        {
            return new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        }
    }
}
