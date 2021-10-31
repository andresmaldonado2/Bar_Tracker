package com.example.main_menu.callbacks;

import com.example.main_menu.interfaces.MatrixMultiplicationListener;
import com.example.main_menu.interfaces.MatrixMultiplicationResultListener;

import java.util.concurrent.ExecutorService;

public class MatrixMultiplicationResultWorker
{
    private ExecutorService executor;
    private MatrixMultiplicationResultListener resultListener;
    double finalResult = 0;
    int count;
    int numOfWorkers;
    public MatrixMultiplicationResultWorker(int numOfWorkers, ExecutorService executor)
    {
        this.executor = executor;
        count = 0;
        this.numOfWorkers = numOfWorkers;
    }
    public void addMultiplicationWorker(double aMatrixResult, double bMatrixResult, ExecutorService executor)
    {
        MatrixMultiplicationWorker multiWorker = new MatrixMultiplicationWorker(aMatrixResult, bMatrixResult, executor);
        multiWorker.setMatrixMultiplicationListener(new MatrixMultiplicationListener() {
            @Override
            public void onMultiplicationComplete(double result) {
                finalResult = finalResult + result;
                count++;
                addAllResults();
            }
        });
        multiWorker.multiply();
    }
    public void setResultListener(MatrixMultiplicationResultListener resultListener)
    {
        this.resultListener = resultListener;
    }
    public void addAllResults()
    {
        if(count == numOfWorkers)
        {
            resultListener.onResultComplete(finalResult);
        }
    }
}
