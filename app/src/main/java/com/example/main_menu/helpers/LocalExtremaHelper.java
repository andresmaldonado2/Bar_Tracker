package com.example.main_menu.helpers;
import java.util.ArrayList;

public class LocalExtremaHelper
{
    public static double[][] calculatePositionalData(double[][] data)
    {
        double[][] positionData = new double[data.length][data[0].length];
        for(int i = 0; i < positionData.length; i++)
        {
            positionData[i][0] = calculateXCoordinate(data[i][0], data[i][1]);
            positionData[i][1] = calculateYCoordinate(data[i][0], data[i][1]);
        }
        return positionData;
    }
    public static double calculateXCoordinate(double distance, double angle)
    {
        return distance * Math.sin(Math.toRadians(90.0 - angle));
    }
    public static double calculateYCoordinate(double distance, double angle)
    {
        return distance * Math.sin(Math.toRadians(angle));
    }
    public static double[][] firstDerivativeTest(double[] coefficients)
    {
        double[] slopes = new double[coefficients.length - 1];
        double[] derivativeCoefficients = firstDerivative(coefficients);
        double[] roots;
        double[] testPoints;
        double[][] localExtremaPoints = new double[coefficients.length - 2][2];
        if(coefficients.length == 4)
        {
            roots = quadraticEquation(derivativeCoefficients);
            testPoints = new double[]{roots[0] - 1, roots[0] + 1, roots[1] + 1};
        }
        else if(coefficients.length == 3)
        {
            roots  = linearEquation(derivativeCoefficients);
            testPoints = new double[]{roots[0] - 1, roots[0] + 1};
        }
        else
        {
            throw new RuntimeException("Your derivative test got completely fucked, line 42 LocalExtremaHelper");
        }
        for(int i = 0; i < slopes.length; i++)
        {
            slopes[i] += derivativeCoefficients[0];
            for(int z = 1; z < derivativeCoefficients.length; z++)
            {
                slopes[i] += testPoints[i] * derivativeCoefficients[z];
            }
            //TODO I think I don't need any of this if statement nonsense, it'll probably catch some error but then I'd rather it crash so I can know and fix it tbh
            if(i > 0)
            {
                if( ((slopes[i] > 0) && (slopes[i - 1] < 0)) || ((slopes[i] < 0) && (slopes[i - 1] > 0)) )
                {
                    localExtremaPoints[i - 1][0] = roots[i - 1];
                    double yCoord = coefficients[0];
                    for(int y = 1; y < coefficients.length; y++)
                    {
                        yCoord += roots[i - 1] * coefficients[y];
                    }
                    localExtremaPoints[i - 1][1] = yCoord;
                }
            }
        }
        return localExtremaPoints;
    }
    public static double[] firstDerivative(double[] coefficients)
    {
        double[] derivativeCoefficients = new double[coefficients.length - 1];
        for(int i = 1; i < coefficients.length; i++)
        {
            derivativeCoefficients[i - 1] = coefficients[i] * i;
        }
        return derivativeCoefficients;
    }
    public static double[] quadraticEquation(double[] coefficients)
    {
        double[] roots;
        double a = coefficients[2];
        double b = coefficients[1];
        double c = coefficients[0];
        double determinant = (b*b)-(4*a*c);
        double sqrt = Math.sqrt(determinant);
        if(determinant>0)
        {
            roots = new double[2];
            roots[0] = (-b + sqrt)/(2*a);
            roots[1] = (-b - sqrt)/(2*a);
            return roots;
        }
        else if(determinant == 0)
        {
            roots = new double[1];
            roots[0] = (-b + sqrt)/(2*a);
            return roots;
        }
        else
        {
            return null;
        }
    }
    public static double[] linearEquation(double[] coefficients)
    {
        double[] root = new double[1];
        root[0] = (-1 * coefficients[0] ) / coefficients[1];
        return root;
    }
}
