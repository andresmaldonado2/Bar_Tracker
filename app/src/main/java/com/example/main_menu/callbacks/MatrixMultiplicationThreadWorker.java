package com.example.main_menu.callbacks;

import com.example.main_menu.interfaces.MatrixMultiplicationThreadListener;

public class MatrixMultiplicationThreadWorker
{
    private int numberOfWorkers;
    private MatrixMultiplicationThreadListener listener;
    private int count;

    public MatrixMultiplicationThreadWorker(int numberOfWorkers)
    {
        this.numberOfWorkers = numberOfWorkers;
    }
    public void setMatrixMultiplicationThreadListener(MatrixMultiplicationThreadListener listener)
    {
        this.listener = listener;
    }
    public synchronized void incrementCount()
    {
        count++;
    }
    public void checkIfAllThreadsDone()
    {
        if(count == numberOfWorkers)
        {
            listener.allThreadsDone();
        }
    }
}
