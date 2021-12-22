package com.example.main_menu.callbacks;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.main_menu.charting.WorkoutData;
import com.example.main_menu.helpers.DataSimulationHelper;
import com.example.main_menu.helpers.LocalExtremaHelper;
import com.example.main_menu.helpers.PerformanceMetric;
import com.example.main_menu.interfaces.RealTimeDataListener;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;

public class LineChartWorker {
    private int TIME_INTERVAL = 16;
    private final int NUMBER_OF_EXPECTED_REPS = 5;
    // In inches, we gotta make an assumption of some type, I have a fairly low to the ground bench so this is accurate for *me*
    // May have to increase for normal height benches
    private final double DISTANCE_FROM_TRACKER = 18;
    private RealTimeDataListener dataListener;
    private DataSimulationHelper sim;
    private double[] dataPointSim = new double[2];
    private WorkoutData data;
    private Handler dataSimHandler = new Handler();
    private Handler saveWorkoutHandler;
    private ILineDataSet dataSet;
    private JSONArray jsonArr;
    private int jsonArrLength;
    private int saveArrCount;
    private PerformanceMetric metricHelper;

    public LineChartWorker(WorkoutData data, int weight, boolean inKG) {
        this.data = data;
        dataSet = data.getDataSetByIndex(0);
        sim = new DataSimulationHelper(NUMBER_OF_EXPECTED_REPS, DISTANCE_FROM_TRACKER);
        metricHelper = new PerformanceMetric(weight, inKG);
    }
    public LineChartWorker(WorkoutData data, JSONArray jsonArr)
    {
        this.data = data;
        saveWorkoutHandler = new Handler();
        this.jsonArr = jsonArr;
    }

    public void setRealTimeDataListener(RealTimeDataListener dataListener) {
        this.dataListener = dataListener;
    }

    public void startChartUpdates() {
        dataSimHandler.post(r);
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            dataPointSim = sim.nextDataPoint();
            if (dataPointSim != null) {
                double[] metrics = metricHelper.getPerformanceMetrics(dataPointSim);
                double xCoor = LocalExtremaHelper.calculateXCoordinate(dataPointSim[0], dataPointSim[1]);
                double yCoor = LocalExtremaHelper.calculateYCoordinate(dataPointSim[0], dataPointSim[1]);;
                data.addEntry(new Entry( ((float) xCoor), ((float) yCoor)), 0 );
                data.removeEntry(dataSet.getEntryForIndex(0), 0);
                dataListener.onLineDataSetUpdate(data, metrics);
                dataSimHandler.postDelayed(this, TIME_INTERVAL);
            }
        }
    };

    public void startSaveDataChartUpdates ()
    {
        saveArrCount = 0;
        jsonArrLength = jsonArr.length();
        saveWorkoutHandler.post(n);
    }

    Runnable n = new Runnable() {
        @Override
        public void run()
        {
            try
            {
                if(saveArrCount < jsonArrLength)
                {
                    JSONArray dataPoint = jsonArr.getJSONArray(saveArrCount);
                    data.removeEntry(data.getDataSetByIndex(0).getEntryForIndex(0), 0);
                    data.addEntry(new Entry(Float.parseFloat(dataPoint.getString(0)), Float.parseFloat(dataPoint.getString(1))), 0);
                    System.out.println("X: " + Float.parseFloat(dataPoint.getString(0)) + "Y: " + Float.parseFloat(dataPoint.getString(1)));
                    dataListener.onLineDataSetUpdate(data, null);
                    saveArrCount++;
                    saveWorkoutHandler.postDelayed(this, TIME_INTERVAL);
                }
            }
            catch(JSONException e)
            {

            }
        }
    };
}
