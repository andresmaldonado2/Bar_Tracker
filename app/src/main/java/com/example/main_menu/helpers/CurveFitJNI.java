package com.example.main_menu.helpers;

import java.util.Arrays;

public class CurveFitJNI
{
    static {
        System.loadLibrary("native");
    }
    private native double[] vectorProjection(double[][] positionData, int degree);
    public static void main(String[] args)
    {
        double[][] positionData = new double[][]{
                {0,1},
                {2,0},
                {3,3},
                {4,5},
                {5,4}
        };
        int degree = 3;
        double[] data = new CurveFitJNI().vectorProjection(positionData, degree);
        System.out.println(Arrays.toString(data));
    }
}
