package com.example.main_menu.callbacks;

import com.example.main_menu.interfaces.MatrixMultiplicationListener;

import java.util.concurrent.ExecutorService;

public class MatrixMultiplicationWorker
{
    private ExecutorService executor;
    private MatrixMultiplicationListener listener;
    double aMatrixResult;
    double bMatrixResult;
    double finalResult;
    private Runnable r = new Runnable() {
        @Override
        public void run() {
            finalResult = aMatrixResult * bMatrixResult;
            listener.onMultiplicationComplete(finalResult);
        }
    };
    public MatrixMultiplicationWorker(double aMatrixResult, double bMatrixResult, ExecutorService executor)
    {
        this.aMatrixResult = aMatrixResult;
        this.bMatrixResult = bMatrixResult;
        this.executor = executor;
    }
    public void setMatrixMultiplicationListener(MatrixMultiplicationListener listener)
    {
        this.listener = listener;
    }
    public void multiply()
    {
        executor.execute(r);
    }
}
