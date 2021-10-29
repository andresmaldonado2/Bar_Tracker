package com.example.main_menu.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.main_menu.callbacks.LineChartWorker;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class TrainingBarPathActivity extends AppCompatActivity
{
    private RelativeLayout rl;
    private LineChart chart;
    private LineDataSet graphDataSet;
    private LineData chartData;

    // Same color as the backgrounds in previous activities
    private final int BACKGROUND_COLOR = 0xFF121212;
    private final int LINE_COLOR = 0xFFB00020;
    private final float LINE_WIDTH = 5f;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        chart = new LineChart(getApplicationContext());
        rl = new RelativeLayout(getApplicationContext());
        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rl.setBackgroundColor(BACKGROUND_COLOR);

        ArrayList<Entry> dataPoints = new ArrayList<>();
        graphDataSet = new LineDataSet(dataPoints, "Test Data");
        setLineDataSetDesign(graphDataSet);

        ArrayList<ILineDataSet> chartDataSet = new ArrayList<>();
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
        LineChartWorker chartUpdateListener = new LineChartWorker(graphDataSet);
        chartUpdateListener.setRealTimeDataListener(data -> {
            graphDataSet = data;
            chart.notifyDataSetChanged();
            chart.invalidate();
        });
        chartUpdateListener.startChartUpdates();
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
