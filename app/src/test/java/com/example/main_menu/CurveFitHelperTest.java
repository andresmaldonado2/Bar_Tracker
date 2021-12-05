package com.example.main_menu;

import com.example.main_menu.helpers.CurveFitHelper;
import com.example.main_menu.helpers.DataSimulationHelper;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

public class CurveFitHelperTest
{
    CurveFitHelper curveHelper = new CurveFitHelper();
    @Test
    public void testThreadedCreateMatrix()
    {

    }
    @Test
    public void testThreadedCofactorMatrix()
    {
        double[][] testData = new double[][]{
                {5,-2,2,7},
                {1,0,0,3},
                {-3,1,5,0},
                {3,-1,-9,4}
        };
        double[][] expectedMatrixResult = new double[][]{
                {-12,-56,4,4},
                {76,208,4,4},
                {-60,-82,-2,20},
                {-36,-58,-10,12}
        };
        double[][] actualMatrixResult = curveHelper.cofactorMatrix(testData);
        double epsilon = 0.0001d;
        for(int i = 0; i < expectedMatrixResult.length; i++)
        {
            for (int z = 0; z < expectedMatrixResult[0].length; z++)
            {
                assertEquals(expectedMatrixResult[i][z], actualMatrixResult[i][z], epsilon);
            }
        }
    }
    @Test
    public void testQuasiMaxStrainQuadraticRegression()
    {
        DataSimulationHelper sim = new DataSimulationHelper(1, 12);
        // Calculated this by taking the standard initial concentric of 1.2 seconds
        // divided by the standard initial time interval
        double[][] testData = new double[75][2];
        int count = 0;
        testData[count] = sim.nextDataPoint();
        while (sim.getConcentricPath())
        {
            count++;
            System.out.println("Count: " + count);
            testData[count] = sim.nextDataPoint();
        }
        double[] coefficientResults = curveHelper.vectorProjection(testData, 2);
        assertEquals(0,0);
    }
    @Test
    public void testUnthreadedMultiplyMatrix()
    {
        DataSimulationHelper sim = new DataSimulationHelper(1, 12);
        // Calculated this by taking the standard initial concentric of 1.2 seconds
        // divided by the standard initial time interval
        double[][] testData = new double[75][2];
        int count = 0;
        testData[count] = sim.nextDataPoint();
        while (sim.getConcentricPath())
        {
            count++;
            System.out.println("Count: " + count);
            testData[count] = sim.nextDataPoint();
        }
        double[] coefficientResults = curveHelper.unthreadedVectorProjection(testData, 2);
        assertEquals(0,0);
    }
    @Test
    public void testMultiplyMatrices()
    {
        double[][] testMatrixA = new double[][]{
                {3,-1},
                {0,2},
                {1,-1}
        };
        double[][] testMatrixB = new double[][]{
                {1,0},
                {-1,4}
        };
        double[][] expectedMatrixResult = new double[][]{
                {4,-4},
                {-2,8},
                {2,-4}
        };
        double[][] actualMatrixResult = curveHelper.multiplyMatrices(testMatrixA, testMatrixB);
        double epsilon = 0.000001d;
        System.out.println(Arrays.deepToString(actualMatrixResult));
        for(int i = 0; i < expectedMatrixResult.length; i++)
        {
            for (int z = 0; z < expectedMatrixResult[0].length; z++)
            {
                assertEquals(expectedMatrixResult[i][z], actualMatrixResult[i][z], epsilon);
            }
        }
    }
    @Test
    public void testMultiplyMatrices1D()
    {
        double[][] testMatrixA = new double[][]{
                {2,4,9},
                {7,6,1},
                {5,9,3}
        };
        double[] testMatrixB = new double[]{8,6,3};
        double[] expectedMatrixResult = new double[]{67, 95, 103};
        double[] actualMatrixResult = curveHelper.multiplyMatrices(testMatrixA, testMatrixB);
        double epsilon = 0.000001d;
        for(int i = 0; i < expectedMatrixResult.length; i++)
        {
            assertEquals(expectedMatrixResult[i], actualMatrixResult[i], epsilon);
        }
    }
    // Appears as if at worst I have only 2 decimal places of accuracy
    // Very small chance the accuracy is fine and the things I'm checking against are the ones that are inaccurate

    @Test
    public void testQuadraticVectorProjection()
    {
        double[][] testData = new double[][]{
                {-3, 7.5},
                {-2, 3},
                {-1, 0.5},
                {0,1},
                {1,3},
                {2,6},
                {3,14}
        };
        double[] expectedResults = new double[]{0.5714, 1, 1.1071};
        double[] actualResults = curveHelper.vectorProjection(testData,2);
        double epsilon = 0.0001d;
        for (int i = 0; i < expectedResults.length; i++)
        {
            assertEquals(expectedResults[i], actualResults[i], epsilon);
        }
    }

    @Test
    public void testCubicVectorProjection()
    {
        double[][] testData = new double[][]{
            {0,1},
            {2,0},
            {3,3},
            {4,5},
            {5,4}
        };
        double[] expectedResults = new double[]{0.9973, -5.0755, 3.0678, -0.3868};
        Long start = System.currentTimeMillis();
        double[] actualResults = curveHelper.vectorProjection(testData,3);
        Long end = System.currentTimeMillis();
        System.out.println("Time spent: " + (end - start));
        double epsilon = 0.01d;
        for (int i = 0; i < expectedResults.length; i++)
        {
            assertEquals(expectedResults[i], actualResults[i], epsilon);
        }
    }
    @Test
    public void testTransposeMatrix()
    {
        double[][] testData = new double[][]{
                {1,0,0,0},
                {1,2,4,8},
                {1,3,9,27},
                {1,4,16,64},
                {1,5,25,125}
        };
        double[][] expectedMatrixResult = new double[][]{
                {1,1,1,1,1},
                {0,2,3,4,5},
                {0,4,9,16,25},
                {0,8,27,64,125}
        };
        double[][] actualMatrixResult = curveHelper.transposeMatrix(testData);
        double epsilon = 0.000001d;
        for(int i = 0; i < expectedMatrixResult.length; i++)
        {
            for (int z = 0; z < expectedMatrixResult[0].length; z++)
            {
                assertEquals(expectedMatrixResult[i][z], actualMatrixResult[i][z], epsilon);
            }
        }
    }
    @Test
    public void testDeterminantMatrix()
    {
        double[][] testData = new double[][]{
                {5,14,54,224},
                {14,54,224,978},
                {54,224,978,4424},
                {224,978,4424,20514}
        };
        double expectedResult = 106848.0;
        double actualResult = curveHelper.determinantMatrix(testData);
        double epsilon = 0.000001d;
        assertEquals(expectedResult, actualResult, epsilon);
    }
    @Test
    public void testInverseMatrix()
    {
        double[][] testData = new double[][]{
                {5,14,54,224},
                {14,54,224,978},
                {54,224,978,4424},
                {224,978,4424,20514}
        };
        double[][] expectedMatrixResult = new double[][]{
                {0.9987, -0.9544, 0.2844, -0.0267},
                {-0.9544, 5.5128, -2.7877, 0.3488},
                {0.2844, -2.7877, 1.4987, -0.1934},
                {-0.0267, 0.3488, -0.1934, 0.0254}
        };
        double[][] actualMatrixResult = curveHelper.inverseMatrix(testData);
        double epsilon = 0.0001d;
        for(int i = 0; i < expectedMatrixResult.length; i++)
        {
            for (int z = 0; z < expectedMatrixResult[0].length; z++)
            {
                assertEquals(expectedMatrixResult[i][z], actualMatrixResult[i][z], epsilon);
            }
        }
    }
    @Test
    public void testCofactorMatrix()
    {
        double[][] testData = new double[][]{
                {5,-2,2,7},
                {1,0,0,3},
                {-3,1,5,0},
                {3,-1,-9,4}
        };
        double[][] expectedMatrixResult = new double[][]{
                {-12,-56,4,4},
                {76,208,4,4},
                {-60,-82,-2,20},
                {-36,-58,-10,12}
        };
        double[][] actualMatrixResult = curveHelper.cofactorMatrix(testData);
        double epsilon = 0.0001d;
        for(int i = 0; i < expectedMatrixResult.length; i++)
        {
            for (int z = 0; z < expectedMatrixResult[0].length; z++)
            {
                assertEquals(expectedMatrixResult[i][z], actualMatrixResult[i][z], epsilon);
            }
        }
    }
    @Test
    public void testAdjointMatrix()
    {
        double[][] testData = new double[][]{
                {5,-2,2,7},
                {1,0,0,3},
                {-3,1,5,0},
                {3,-1,-9,4}
        };
        double[][] expectedMatrixResult = new double[][]{
                {-12,76,-60,-36},
                {-56,208,-82,-58},
                {4,4,-2,-10},
                {4,4,20,12}
        };
        double[][] actualMatrixResult = curveHelper.adjointMatrix(testData);
        double epsilon = 0.0001d;
        for(int i = 0; i < expectedMatrixResult.length; i++)
        {
            for (int z = 0; z < expectedMatrixResult[0].length; z++)
            {
                assertEquals(expectedMatrixResult[i][z], actualMatrixResult[i][z], epsilon);
            }
        }
    }
    @Test
    public void testDataPointsOnCurve()
    {
        double[][] testData = new double[][]{
                {0,1},
                {2,0},
                {3,3},
                {4,5},
                {5,4}
        };
        double[][] expectedMatrixResult = new double[][]{
                {0,1},
                {2,0},
                {3,3},
                {4,5},
                {5,4}
        };
        double[][] actualMatrixResult = curveHelper.dataPointsOnCurve(testData, 3);
        double epsilon = 0.1d;
        System.out.println(Arrays.deepToString(actualMatrixResult));
        for(int i = 0; i < expectedMatrixResult.length; i++)
        {
            for (int z = 0; z < expectedMatrixResult[0].length; z++)
            {
                assertEquals(expectedMatrixResult[i][z], actualMatrixResult[i][z], epsilon);
            }
        }
    }
}
