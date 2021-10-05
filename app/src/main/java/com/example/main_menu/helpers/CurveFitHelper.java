package com.example.main_menu.helpers;

// in case future me forgets the basics of the cubic regression math: https://www.omnicalculator.com/statistics/cubic-regression
// other useful link: https://www.geeksforgeeks.org/adjoint-inverse-matrix/ (never did I expect that I would find the best explaination of the math *here*)
// wish I could get back all those hours trying to wrap my head around all the other stuff I had read

import java.util.Arrays;

public class CurveFitHelper
{
    public static double[][] dataPointsOnCurve(double[][] data)
    {
        double[][] calculatedPoints = new double[data.length][data[0].length];
        double[] coefficients = vectorProjection(data);
        for(int i = 0; i < data.length; i++)
        {
            calculatedPoints[i][0] = i;
            calculatedPoints[i][1] = coefficients[0] + (coefficients[1] * i) + (coefficients[2] * Math.pow(i,2)) + (coefficients[3] * Math.pow(i, 3));
        }
        return calculatedPoints;
    }
    public static double[] vectorProjection(double[][] positionData)
    {
        double[] yVector =  new double[positionData.length];
        double[][] matrix = createMatrix(positionData);
        double[][] transposedMatrix = transposeMatrix(matrix);
        for (int i = 0; i < positionData.length; i++)
        {
            yVector[i] = positionData[i][1];
        }
        // Equation for this is ((X^t*X)^-1)*X^t*y
        double[] result = multiplyMatrices(multiplyMatrices(inverseMatrix(multiplyMatrices(transposedMatrix, matrix)), transposedMatrix), yVector);
        System.out.println(Arrays.toString(result));
        return (result);
    }
    public static double[][] createMatrix(double[][] data)
    {
        double[][] matrix = new double[data.length][4];
        for(int i = 0; i < data.length; i++)
        {
            for(int z = 0; z < 4; z++)
            {
                matrix[i][z] = Math.pow(data[i][0], z);
            }
        }
        return matrix;
    }
    public static double[][] transposeMatrix(double[][] data)
    {
        double[][] transposedMatrix = new double[data[0].length][data.length];
        for (int i = 0; i < data[0].length; i++)
        {
            for (int z = 0; z < data.length; z++)
            {
                transposedMatrix[i][z] = data[z][i];
            }
        }
        return transposedMatrix;
    }
    public static double[][] multiplyMatrices(double[][] aMatrix, double[][] bMatrix)
    {
        double[][] productMatrix = new double[aMatrix.length][bMatrix[0].length];
        for (int i = 0; i < productMatrix[0].length; i++)
        {
            for (int z = 0; z < aMatrix.length; z++)
            {
                double result = 0.0;
                for (int a = 0; a < aMatrix[0].length; a++)
                {
                    result = result + (aMatrix[z][a] * bMatrix[a][i]);
                }
                productMatrix[z][i] = result;
            }
        }
        return productMatrix;
    }
    public static double[] multiplyMatrices(double[][] aMatrix, double[] bMatrix)
    {
        double[] productMatrix = new double[aMatrix.length];
        for (int z = 0; z < productMatrix.length; z++)
        {
            double result = 0.0;
            for (int a = 0; a < aMatrix[0].length; a++)
            {
                result = result + (aMatrix[z][a] * bMatrix[a]);
            }
            productMatrix[z] = result;
        }
        return productMatrix;
    }
    public static double[][] inverseMatrix(double[][] data)
    {
        double[][] inversedMatrix = new double[data.length][data[0].length];
        double determinant = determinantMatrix(data);
        double[][] adjointMatrix = adjointMatrix(data);
        for (int i = 0; i < data.length; i++)
        {
            for(int z = 0; z < data[0].length; z++)
            {
                inversedMatrix[i][z] = adjointMatrix[i][z] / determinant;
            }
        }
        return inversedMatrix;
    }
    public static double[][] adjointMatrix(double[][] data)
    {
        return transposeMatrix(cofactorMatrix(data));
    }
    public static double[][] cofactorMatrix(double[][] data)
    {
       double[][] cofactoredMatrix = new double[data.length][data[0].length];
        for (int i = 0; i < data.length; i++)
        {
            for (int z = 0; z < data[0].length; z++)
            {
                double[][] tempMatrix = new double[data.length - 1][data[0].length - 1];
                for(int x = 0; x < data.length; x++)
                {
                    for(int y = 0; y < data[0].length; y++)
                    {
                        if(x < i)
                        {
                            if(y < z)
                            {
                                tempMatrix[x][y] = data[x][y];
                            }
                            else if(y > z)
                            {
                                tempMatrix[x][y - 1] = data[x][y];
                            }
                        }
                        else if (x > i)
                        {
                            if(y < z)
                            {
                                tempMatrix[x - 1][y] = data[x][y];
                            }
                            else if(y > z)
                            {
                                tempMatrix[x - 1][y - 1] = data[x][y];
                            }
                        }
                    }
                }
                cofactoredMatrix[i][z] = (Math.pow((-1),((i + 1) + (z + 1)))) * determinantMatrix(tempMatrix);
            }
        }
        return cofactoredMatrix;
    }
    public static double determinantMatrix(double[][] data)
    {
        double finalDeterminant = 0.0;
        if (data.length > 2)
        {
            for (int z = 0; z < data[0].length; z++)
            {
                if ((z + 1) % 2 > 0)
                {
                    finalDeterminant = finalDeterminant + (data[0][z] * determinantMatrix(laplaceExpansionMatrixCreator(data, z)));
                }
                else
                {
                    finalDeterminant = finalDeterminant - (data[0][z] * determinantMatrix(laplaceExpansionMatrixCreator(data, z)));
                }
            }
        }
        else
        {
            finalDeterminant = finalDeterminant + ((data[0][0] * data[1][1]) - (data[0][1] * data[1][0]));
        }
        return finalDeterminant;
    }
    public static double[][] laplaceExpansionMatrixCreator(double[][] data, int col)
    {
        double[][] tempMatrix = new double[data.length - 1][data[0].length - 1];
        for (int i = 0; i < data[0].length; i++)
        {
            for (int y = 1; y < data.length; y++)
            {
                if(i < col)
                {
                    tempMatrix[y-1][i] = data[y][i];
                }
                else if(i > col)
                {
                    tempMatrix[y-1][i-1] = data[y][i];
                }
            }
        }
        return tempMatrix;
    }
}