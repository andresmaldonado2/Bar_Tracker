package com.example.main_menu.callbacks;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.main_menu.helpers.DataSimulationHelper;
import com.example.main_menu.interfaces.RealTimeDataListener;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

public class LineChartWorker
{
    private int TIME_INTERVAL = 16;
    private final int NUMBER_OF_EXPECTED_REPS = 5;
    private RealTimeDataListener dataListener;
    private DataSimulationHelper sim;
    private double[] dataPointSim = new double[2];
    private LineData data;
    private Handler dataSimHandler = new Handler();

    public LineChartWorker(LineData data)
    {
        this.data = data;
        sim = new DataSimulationHelper(NUMBER_OF_EXPECTED_REPS);
    }
    public void setRealTimeDataListener(RealTimeDataListener dataListener)
    {
        this.dataListener = dataListener;
    }
    public void startChartUpdates()
    {
        dataSimHandler.post(r);
    }
    Runnable r = new Runnable() {
        @Override
        public void run() {
            dataPointSim = sim.nextDataPoint();
            if(dataPointSim != null)
            {
                data.addEntry(new Entry(((float) dataPointSim[0]), (float) dataPointSim[1]),0);
                dataListener.onLineDataSetUpdate(data);
                dataSimHandler.postDelayed(this, TIME_INTERVAL);
            }
        }
    };
}
