package com.example.main_menu.helpers;

// in case future me forgets the basics of the cubic regression math: https://www.omnicalculator.com/statistics/cubic-regression
// other useful link: https://www.geeksforgeeks.org/adjoint-inverse-matrix/ (never did I expect that I would find the best explaination of the math *here*)
// wish I could get back all those hours trying to wrap my head around all the other stuff I had read

import androidx.annotation.VisibleForTesting;

import com.example.main_menu.callbacks.MatrixMultiplicationResultWorker;
import com.example.main_menu.callbacks.MatrixMultiplicationThreadWorker;
import com.example.main_menu.interfaces.MatrixMultiplicationResultListener;
import com.example.main_menu.interfaces.MatrixMultiplicationThreadListener;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CurveFitHelper
{
    private ExecutorService executor;
    public CurveFitHelper()
    {
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }
    public double[][] dataPointsOnCurve(double[][] data, int degree)
    {
        double[][] calculatedPoints = new double[data.length][data[0].length];
        double[] coefficients = vectorProjection(data, degree);
        for(int i = 0; i < data.length; i++)
        {
            calculatedPoints[i][0] = data[i][0];
            calculatedPoints[i][1] = coefficients[0] + (coefficients[1] * data[i][0]) + (coefficients[2] * Math.pow(data[i][0],2)) + (coefficients[3] * Math.pow(data[i][0], 3));
        }
        return calculatedPoints;
    }
    public double[] unthreadedVectorProjection(double[][] positionData, int degree)
    {
        double[] yVector =  new double[positionData.length];
        double[][] matrix = unthreadedCreateMatrix(positionData, degree);
        double[][] transposedMatrix = transposeMatrix(matrix);
        for (int i = 0; i < positionData.length; i++)
        {
            yVector[i] = positionData[i][1];
        }
        // Equation for this is ((X^t*X)^-1)*X^t*y
        double[] result = multiplyMatrices(unthreadedMultiplyMatrix(unthreadedInverseMatrix(unthreadedMultiplyMatrix(transposedMatrix, matrix)), transposedMatrix), yVector);
        System.out.println(Arrays.toString(result));
        return (result);
    }
    public double[] vectorProjection(double[][] positionData, int degree)
    {
        double[] yVector =  new double[positionData.length];
        double[][] matrix = createMatrix(positionData, degree);
        double[][] transposedMatrix = transposeMatrix(matrix);
        for (int i = 0; i < positionData.length; i++)
        {
            yVector[i] = positionData[i][1];
        }
        // Equation for this is ((X^t*X)^-1)*X^t*y
        double[] result = multiplyMatrices(unthreadedMultiplyMatrix(unthreadedInverseMatrix(unthreadedMultiplyMatrix(transposedMatrix, matrix)), transposedMatrix), yVector);
        System.out.println(Arrays.toString(result));
        return (result);
    }
    public double[][] unthreadedCreateMatrix(double[][] data, int degree)
    {
        double[][] matrix = new double[data.length][degree + 1];
        for(int i = 0; i < data.length; i++)
        {
            for(int z = 0; z < degree + 1; z++)
            {
                matrix[i][z] = Math.pow(data[i][0], z);
            }
        }
        return matrix;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public double[][] createMatrix(double[][] data, int degree)
    {
        double[][] matrix = new double[data.length][degree + 1];
        CountDownLatch latch = new CountDownLatch(data.length * degree);
        for(int i = 0; i < data.length; i++)
        {
            for(int z = 0; z < degree + 1; z++)
            {
                int finalI = i;
                int finalZ = z;
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        matrix[finalI][finalZ] = Math.pow(data[finalI][0], finalZ);
                        latch.countDown();
                    }
                });
            }
        }
        try {
            latch.await();
        }
        catch (InterruptedException e)
        {

        }
        System.out.println("Created matrix: " + Arrays.deepToString(matrix));
        return matrix;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public double[][] transposeMatrix(double[][] data)
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
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public double[][] multiplyMatrices(double[][] aMatrix, double[][] bMatrix)
    {
        final double[][] productMatrix = new double[aMatrix.length][bMatrix[0].length];
        CountDownLatch threadLatch = new CountDownLatch(productMatrix[0].length * aMatrix.length);

        MatrixMultiplicationThreadWorker threadWorker = new MatrixMultiplicationThreadWorker(productMatrix[0].length * aMatrix.length * aMatrix[0].length);
        threadWorker.setMatrixMultiplicationThreadListener(new MatrixMultiplicationThreadListener() {
            @Override
            public void allThreadsDone() {

            }
        });
        for (int i = 0; i < productMatrix[0].length; i++)
        {
            for (int z = 0; z < aMatrix.length; z++)
            {
                // This seems like such a hack but at least according to stackoverflow this is how you deal
                // With this issue
                // TODO please for the love of god figure out the proper way to do this
                int finalI = i;
                int finalZ = z;
                MatrixMultiplicationResultWorker resultWorker = new MatrixMultiplicationResultWorker(aMatrix[0].length, executor, threadLatch);
                resultWorker.setResultListener(new MatrixMultiplicationResultListener() {
                    @Override
                    public void onResultComplete(double result) {
                        productMatrix[finalZ][finalI] = result;
                    }
                });
                for(int a = 0; a < aMatrix[0].length; a++)
                {
                    resultWorker.addMultiplicationWorker(aMatrix[z][a], bMatrix[a][i], executor);
                }
            }
        }
        try {
            threadLatch.await();
        }
        catch (InterruptedException e)
        {

        }
        return productMatrix;
    }
    public double[][] unthreadedMultiplyMatrix(double[][] aMatrix, double[][] bMatrix)
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
    public double[][] differentMultiplyMatrix(double[][] aMatrix, double[][] bMatrix)
    {
        double[][] productMatrix = new double[aMatrix.length][bMatrix[0].length];
        CountDownLatch latch = new CountDownLatch(productMatrix[0].length);
        for (int i = 0; i < productMatrix[0].length; i++)
        {
            int finalI = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    for (int z = 0; z < aMatrix.length; z++)
                    {
                        double result = 0.0;
                        for (int a = 0; a < aMatrix[0].length; a++)
                        {
                            result = result + (aMatrix[z][a] * bMatrix[a][finalI]);
                        }
                        productMatrix[z][finalI] = result;
                        latch.countDown();
                    }
                }
            });
        }
        try {
            latch.await();
        }
        catch (InterruptedException e)
        {

        }
        return productMatrix;
    }
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public double[] multiplyMatrices(double[][] aMatrix, double[] bMatrix)
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
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public double[][] unthreadedInverseMatrix(double[][] data)
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
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public double[][] inverseMatrix(double[][] data)
    {
        double[][] inversedMatrix = new double[data.length][data[0].length];
        double determinant = determinantMatrix(data);
        double[][] adjointMatrix = adjointMatrix(data);
        CountDownLatch latch = new CountDownLatch(data.length * data[0].length);
        for (int i = 0; i < data.length; i++)
        {
            for(int z = 0; z < data[0].length; z++)
            {
                int finalI = i;
                int finalZ = z;
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        inversedMatrix[finalI][finalZ] = adjointMatrix[finalI][finalZ] / determinant;
                        latch.countDown();
                    }
                });
            }
        }
        try
        {
            latch.await();
        }
        catch (InterruptedException e)
        {

        }
        return inversedMatrix;
    }
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public double[][] adjointMatrix(double[][] data)
    {
        return transposeMatrix(cofactorMatrix(data));
    }
    public double[][] cofactorMatrix(double[][] data)
    {
        double[][] cofactoredMatrix = new double[data.length][data[0].length];
        CountDownLatch latch = new CountDownLatch(data.length * data[0].length);
        for (int i = 0; i < data.length; i++)
        {
            for (int z = 0; z < data[0].length; z++)
            {
                int finalI = i;
                int finalZ = z;
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        double[][] tempMatrix = new double[data.length - 1][data[0].length - 1];
                        for(int x = 0; x < data.length; x++)
                        {
                            for(int y = 0; y < data[0].length; y++)
                            {
                                if(x < finalI)
                                {
                                    if(y < finalZ)
                                    {
                                        tempMatrix[x][y] = data[x][y];
                                    }
                                    else if(y > finalZ)
                                    {
                                        tempMatrix[x][y - 1] = data[x][y];
                                    }
                                }
                                else if (x > finalI)
                                {
                                    if(y < finalZ)
                                    {
                                        tempMatrix[x - 1][y] = data[x][y];
                                    }
                                    else if(y > finalZ)
                                    {
                                        tempMatrix[x - 1][y - 1] = data[x][y];
                                    }
                                }
                            }
                        }
                        cofactoredMatrix[finalI][finalZ] = (Math.pow((-1),((finalI + 1) + (finalZ + 1)))) * determinantMatrix(tempMatrix);
                        latch.countDown();
                    }
                });
            }
        }
        try {
            latch.await();
        }
        catch (InterruptedException e)
        {

        }
        return cofactoredMatrix;
    }
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public double[][] unthreadedCofactorMatrix(double[][] data)
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

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public double determinantMatrix(double[][] data)
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
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public double[][] laplaceExpansionMatrixCreator(double[][] data, int col)
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