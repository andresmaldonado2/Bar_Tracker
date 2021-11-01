package com.example.main_menu;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.main_menu.callbacks.LineChartWorker;
import com.example.main_menu.helpers.CurveFitHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

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
        // Need to preload graph with something, else negative array size errors get thrown
        // Something weird in the way the library was implemented, not noted in documentation either :shrug:
        graphDataSet.addEntry(new Entry(0, 0));
        setLineDataSetDesign(graphDataSet);

        chartData = new LineData(graphDataSet);
        chart.setData(chartData);
        setChartDesign(chart);
        chart.invalidate();
        CurveFitHelper ch = new CurveFitHelper();
        //RelativeLayout.LayoutParams chartParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        // TODO Research why the hell the layouts used in documentation to display graph don't work but specifically an absolute layout does???
        rl.addView(chart, new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
        this.addContentView(rl, rlParams);

        LineChartWorker chartUpdateListener = new LineChartWorker(chartData);
        chartUpdateListener.setRealTimeDataListener(data -> {
            chartData = data;
            // TODO figure this out later chart.moveViewToX(chartData.getEntryCount()/2 - 1);
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
        yAxis.setDrawGridLines(false);
        xAxis.setDrawGridLines(false);

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
        //chart.setVisibleXRange(0, 15);
        setAxisDesign(chart);
    }
}
