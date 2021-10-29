package com.example.main_menu.callbacks;

import android.os.Handler;
import android.os.Looper;

import com.example.main_menu.helpers.DataSimulationHelper;
import com.example.main_menu.interfaces.RealTimeDataListener;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

public class LineChartWorker
{
    private int TIME_INTERVAL = 250;
    private final int NUMBER_OF_EXPECTED_REPS = 5;
    private RealTimeDataListener dataListener;
    private DataSimulationHelper sim;
    private double[] dataPointSim = new double[2];
    private LineDataSet dataSet;
    private Handler dataSimHandler = new Handler();

    public LineChartWorker(LineDataSet dataSet)
    {
        this.dataSet = dataSet;
        sim = new DataSimulationHelper(NUMBER_OF_EXPECTED_REPS);
    }
    public void setRealTimeDataListener(RealTimeDataListener dataListener)
    {
        this.dataListener = dataListener;
    }
    public void startChartUpdates()
    {
        r.run();
        dataListener.onLineDataSetUpdate(dataSet);
    }
    Runnable r = new Runnable() {
        @Override
        public void run() {
            dataPointSim = sim.nextDataPoint();
            if(dataPointSim != null)
            {
                dataSet.addEntry(new Entry(((float) dataPointSim[0]), (float) dataPointSim[1]));
                dataSimHandler.postDelayed(this, TIME_INTERVAL);
            }
        }
    };
}
