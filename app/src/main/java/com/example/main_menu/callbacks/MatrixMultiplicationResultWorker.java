package com.example.main_menu.callbacks;

import com.example.main_menu.interfaces.MatrixMultiplicationListener;
import com.example.main_menu.interfaces.MatrixMultiplicationResultListener;
import com.example.main_menu.interfaces.MatrixMultiplicationThreadListener;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class MatrixMultiplicationResultWorker
{
    private ExecutorService executor;
    private MatrixMultiplicationResultListener resultListener;
    private CountDownLatch latch;
    Double finalResult = 0.0;
    AtomicInteger count;
    int numOfWorkers;
    public MatrixMultiplicationResultWorker(int numOfWorkers, ExecutorService executor, CountDownLatch latch)
    {
        this.latch = latch;
        this.executor = executor;
        count = new AtomicInteger(0);
        this.numOfWorkers = numOfWorkers;
    }
    public void addMultiplicationWorker(double aMatrixResult, double bMatrixResult, ExecutorService executor)
    {
        MatrixMultiplicationWorker multiWorker = new MatrixMultiplicationWorker(aMatrixResult, bMatrixResult, executor);
        multiWorker.setMatrixMultiplicationListener(new MatrixMultiplicationListener() {
            @Override
            public void onMultiplicationComplete(double result)
            {
                synchronized(finalResult)
                {
                    finalResult = finalResult + result;
                    addAllResults();
                }
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
        if(count.incrementAndGet() == numOfWorkers)
        {
            resultListener.onResultComplete(finalResult.doubleValue());
            latch.countDown();
        }
    }
}
