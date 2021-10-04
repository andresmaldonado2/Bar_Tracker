package com.example.main_menu.ui;

import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.main_menu.databinding.ActivityMainBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

public class TrainingBarPathActivity extends AppCompatActivity
{
    private ActivityMainBinding binding;
    RelativeLayout rl;
    LineChart chart;
    LineDataSet chartDataSet;
    LineData chartData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        chart = new LineChart(getApplicationContext());
        rl = new RelativeLayout(getApplicationContext());

        double[][] testData = new double[][]{
                {0, 1},
                {2, 0},
                {3, 3},
                {4, 5},
                {5, 4}
        };
        ArrayList<Entry> dataPoints = new ArrayList<>();
        for (int i = 0; i < testData.length; i++)
        {
            //TODO Research a better way to squash the double to float other than casting
            dataPoints.add(new Entry((float) testData[i][0], (float) testData[i][1]));
        }
        chartDataSet = new LineDataSet(dataPoints, "Hello chart");
        chartData = new LineData(chartDataSet);
        chart.setData(chartData);

        rl.addView(chart);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
