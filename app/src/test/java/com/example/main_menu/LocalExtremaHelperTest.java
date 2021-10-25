package com.example.main_menu;

import static org.junit.Assert.assertEquals;

import com.example.main_menu.helpers.LocalExtremaHelper;

import org.junit.Test;

import java.util.Arrays;

public class LocalExtremaHelperTest
{
    @Test
    public void firstDerivativeTest()
    {
        double[] testData = new double[]{4,-12,9,3};
        double[] expectedMatrixResult = new double[]{-12,18,9};
        double[] actualMatrixResult = LocalExtremaHelper.firstDerivative(testData);
        double epsilon = 0.000001d;
        for(int i = 0; i < expectedMatrixResult.length; i++)
        {
            assertEquals(expectedMatrixResult[i], actualMatrixResult[i], epsilon);
        }
    }
    @Test
    public void firstDerivativeTestCubicTest()
    {
        double[] testData = new double[]{4,-12,9,3};
        double[][] expectedMatrixResult = new double[][]{
                {((-3.0) + Math.sqrt(21.0)) / 3.0, 0},
                {-1.0 * ( (3 + Math.sqrt(21.0)) / 3.0 ), 0}
        };
        double[][] actualMatrixResult = LocalExtremaHelper.firstDerivativeTest(testData);
        double epsilon = 0.000001d;
        for(int i = 0; i < actualMatrixResult.length; i++)
        {
            assertEquals(expectedMatrixResult[i][0], actualMatrixResult[i][0], epsilon);
        }
    }
    @Test
    public void firstDerivativeTestQuadraticTest()
    {
        double[] testData = new double[]{4,-9,3};
        double[][] expectedMatrixResult = new double[][]{
                {3.0/2.0,0}
        };
        double[][] actualMatrixResult = LocalExtremaHelper.firstDerivativeTest(testData);
        double epsilon = 0.000001d;
        for(int i = 0; i < actualMatrixResult.length; i++)
        {
            assertEquals(expectedMatrixResult[i][0], actualMatrixResult[i][0], epsilon);
        }
    }
}
