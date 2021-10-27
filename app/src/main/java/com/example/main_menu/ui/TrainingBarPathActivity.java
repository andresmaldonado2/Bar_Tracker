package com.example.main_menu.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.main_menu.helpers.DataSimulationHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.concurrent.Executor;

public class TrainingBarPathActivity extends AppCompatActivity
{
    RelativeLayout rl;
    LineChart chart;
    // TODO Figure out how to do this with callbacks? This seems a little too hacked together to seem correct
    volatile LineDataSet graphDataSet;
    volatile DataSimulationHelper sim;
    LineData chartData;

    // Same color as the backgrounds in previous activities
    private final int BACKGROUND_COLOR = 0xFF121212;
    private final int LINE_COLOR = 0xFFB00020;
    private final int NUMBER_OF_EXPECTED_REPS = 5;
    private final float LINE_WIDTH = 5f;

    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        chart = new LineChart(getApplicationContext());
        rl = new RelativeLayout(getApplicationContext());
        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rl.setBackgroundColor(BACKGROUND_COLOR);

        sim = new DataSimulationHelper(NUMBER_OF_EXPECTED_REPS);
        ArrayList<Entry> dataPoints = new ArrayList<>();
        double[] dataPointSim = sim.nextDataPoint();
        dataPoints.add(new Entry(((float)dataPointSim[0]), (float)dataPointSim[1]));

        graphDataSet = new LineDataSet(dataPoints, "Test Data");
        setLineDataSetDesign(graphDataSet);

        ArrayList<ILineDataSet> chartDataSet = new ArrayList<ILineDataSet>();
        chartDataSet.add(graphDataSet);
        chartData = new LineData(chartDataSet);
        chart.setData(chartData);
        setChartDesign(chart);
        chart.invalidate();
        //RelativeLayout.LayoutParams chartParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        // TODO Research why the hell the layouts used in documentation to display graph don't work but specifically an absolute layout does???
        rl.addView(chart, new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
        this.addContentView(rl, rlParams);

        // TODO Need to make this its own thread with a .postDelayed() to get a time interval
        while(dataPointSim != null)
        {
            graphDataSet.addEntry(new Entry(((float)dataPointSim[0]), (float)dataPointSim[1]));
            graphDataSet.getEntryForIndex(graphDataSet.getEntryCount() - 1);
            chart.notifyDataSetChanged();
            chart.invalidate();
            dataPointSim = sim.nextDataPoint();
        }
    }
    private void setAxisDesign(LineChart chart)
    {
        YAxis yAxis = chart.getAxisLeft();
        XAxis xAxis = chart.getXAxis();
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
        yAxis.setTextColor(0xFFFFFFFF);
        xAxis.setTextColor(0xFFFFFFFF);
    }
    private void setLineDataSetDesign(LineDataSet lineDataSet)
    {
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setLineWidth(LINE_WIDTH);
        lineDataSet.setFillColor(LINE_COLOR);
        lineDataSet.setDrawCircles(false);
    }
    private void setChartDesign(LineChart chart)
    {
        chart.setGridBackgroundColor(0xFFFFFFFF);
        setAxisDesign(chart);
    }
}
