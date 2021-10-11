package com.example.main_menu.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.main_menu.helpers.CurveFitHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class TrainingBarPathActivity extends AppCompatActivity
{
    RelativeLayout rl;
    LineChart chart;
    LineDataSet testDataSet;
    LineData chartData;
    LineDataSet calculatedDataSet;
    // Same color as the backgrounds in previous activities
    int backgroundColor = 0xFF121212;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        chart = new LineChart(getApplicationContext());
        rl = new RelativeLayout(getApplicationContext());
        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rl.setBackgroundColor(backgroundColor);
        double[][] testData = new double[][]{
                {0,1},
                {2,0},
                {3,3},
                {4,5},
                {5,4}
        };
        double[][] calculatedData = CurveFitHelper.dataPointsOnCurve(testData, 3);
        ArrayList<Entry> dataPoints = new ArrayList<>();
        ArrayList<Entry> calculatedDataPoints = new ArrayList<>();
        for (int i = 0; i < testData.length; i++)
        {
            //TODO Research a better way to squash the double to float other than casting
            dataPoints.add(new Entry((float) testData[i][0], (float) testData[i][1]));
            calculatedDataPoints.add(new Entry((float)calculatedData[i][0],(float)calculatedData[i][1]));
        }
        //TODO Customize the colors and such on the graph more, looks really basic right now
        testDataSet = new LineDataSet(dataPoints, "Test Data");
        testDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        calculatedDataSet = new LineDataSet(calculatedDataPoints, "Calculated Data");
        calculatedDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        ArrayList<ILineDataSet> chartDataSet = new ArrayList<ILineDataSet>();
        chartDataSet.add(testDataSet);
        chartDataSet.add(calculatedDataSet);
        chartData = new LineData(chartDataSet);
        chart.setData(chartData);
        chart.setGridBackgroundColor(0xFFFFFFFF);
        YAxis yAxis = chart.getAxisLeft();
        XAxis xAxis = chart.getXAxis();
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
        yAxis.setTextColor(0xFFFFFFFF);
        xAxis.setTextColor(0xFFFFFFFF);
        chart.invalidate();
        //RelativeLayout.LayoutParams chartParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        //TODO Research why the hell the layouts used in documentation to display graph don't work but specifically an absolute layout does???
        rl.addView(chart, new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
        this.addContentView(rl, rlParams);
    }
}
