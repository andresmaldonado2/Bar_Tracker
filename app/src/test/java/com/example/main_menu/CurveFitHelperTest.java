package com.example.main_menu;

import com.example.main_menu.helpers.CurveFitHelper;
import org.junit.Test;
import static org.junit.Assert.*;

public class CurveFitHelperTest
{
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
        double[][] actualMatrixResult = CurveFitHelper.multiplyMatrices(testMatrixA, testMatrixB);
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
    public void testMultiplyMatrices1D()
    {
        double[][] testMatrixA = new double[][]{
                {2,4,9},
                {7,6,1},
                {5,9,3}
        };
        double[] testMatrixB = new double[]{8,6,3};
        double[] expectedMatrixResult = new double[]{67, 95, 103};
        double[] actualMatrixResult = CurveFitHelper.multiplyMatrices(testMatrixA, testMatrixB);
        double epsilon = 0.000001d;
        for(int i = 0; i < expectedMatrixResult.length; i++)
        {
            assertEquals(expectedMatrixResult[i], actualMatrixResult[i], epsilon);
        }
    }
    // TODO Results are accurate but I'm losing precision somewhere in the math, need to investigate further.
    // Appears as if at worst I have only 2 decimal places of accuracy
    // Very small chance the accuracy is fine and the things I'm checking against are the ones that are inaccurate
    @Test
    public void testVectorProjection()
    {
        double[][] testData = new double[][]{
        {0,1},
        {2,0},
        {3,3},
        {4,5},
        {5,4}
        };
        double[] expectedResults = new double[]{0.9973, -5.0755, 3.0678, -0.3868};
        double[] actualResults = CurveFitHelper.vectorProjection(testData);
        double epsilon = 0.0001d;
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
        double[][] actualMatrixResult = CurveFitHelper.transposeMatrix(testData);
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
        double actualResult = CurveFitHelper.determinantMatrix(testData);
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
        double[][] actualMatrixResult = CurveFitHelper.inverseMatrix(testData);
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
        double[][] actualMatrixResult = CurveFitHelper.cofactorMatrix(testData);
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
        double[][] actualMatrixResult = CurveFitHelper.adjointMatrix(testData);
        double epsilon = 0.0001d;
        for(int i = 0; i < expectedMatrixResult.length; i++)
        {
            for (int z = 0; z < expectedMatrixResult[0].length; z++)
            {
                assertEquals(expectedMatrixResult[i][z], actualMatrixResult[i][z], epsilon);
            }
        }
    }
}
