package com.example.main_menu.helpers;

public class PerformanceMetricHelper
{

    static {
        System.loadLibrary("performanceMetricsLib");
    }

    public native double[] performanceMetrics(double[] positionData, int weight, double intialVelocity, boolean inKG);
}
