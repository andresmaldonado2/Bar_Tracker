package com.example.main_menu;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.main_menu.callbacks.LineChartWorker;
import com.example.main_menu.charting.WorkoutData;
import com.example.main_menu.helpers.SaveWorkoutsHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class TrainingBarPathActivity extends AppCompatActivity
{
    private RelativeLayout rl;
    private LineChart chart;
    private LineDataSet graphDataSet;
    private LineDataSet saveDataSet;
    private WorkoutData chartData;
    private WorkoutData saveData;
    private YAxis yAxis;
    private XAxis xAxis;
    private CountDownTimer cdt;
    private TextView countdownText;
    private TextView[] metricTextViews;
    private TextView[] metricNameTextViews;
    private TextView[] metricUnitTextViews;
    private FloatingActionButton backButton;
    // Same color as the backgrounds in previous activities
    private final int BACKGROUND_COLOR = 0xFF121212;
    private final int LINE_COLOR = 0xFFB00020;

    private final float LINE_WIDTH = 5f;
    private final float Y_AXIS_DEFAULT = 18.0f;
    private final float X_AXIS_DEFAULT = 36.0f;
    private LineChartWorker chartUpdateListener;
    private final DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Eventually transfer this over to a proper constraint layout rather than the relative layout
        super.onCreate(savedInstanceState);
        Bundle metaData = getIntent().getExtras();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        chart = new LineChart(getApplicationContext());
        rl = new RelativeLayout(getApplicationContext());
        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rl.setBackgroundColor(BACKGROUND_COLOR);

        setUpMetricViews();
        setBackButton();
        setChartDesign();
        setAxisDesign();
        setUpSaveData(metaData);
        setUpGraphData(metaData);
        chart.invalidate();
        // TODO Research why the hell the layouts used in documentation to display graph don't work but specifically an absolute layout does???
        // TODO Look into changing the relative layout into something better like a constraint layout
        rl.addView(chart, new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
        this.addContentView(rl, rlParams);
        setChartUpdateListener(metaData);
        setCountDown();
    }
    private void setUpMetricViews()
    {
        setMetricNameTextViews();
        setMetricTextViews();
        setMetricUnitTextViews();
    }
    private void setMetricNameTextViews()
    {
        metricNameTextViews = new TextView[7];
        String[] metricNames = new String[] {"Average Force: ", "Peak Force: ", "Force During Rep: ", "Average Velocity: ", "Peak Velocity: ", "Current Velocity: ", "Reps Performed: "};
        metricNameTextViews[0] = new TextView(TrainingBarPathActivity.this);
        metricNameTextViews[0].setId(View.generateViewId());
        RelativeLayout.LayoutParams metricNameTextViewParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        metricNameTextViewParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        metricNameTextViewParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        metricNameTextViewParam.setMargins(8,8,8,0);
        metricNameTextViews[0].setLayoutParams(metricNameTextViewParam);
        metricNameTextViews[0].setTextColor(ContextCompat.getColor(this, R.color.white));
        metricNameTextViews[0].setText(metricNames[0]);
        rl.addView(metricNameTextViews[0]);
        for(int i = 1; i < metricNameTextViews.length; i++)
        {
            metricNameTextViews[i] = new TextView(TrainingBarPathActivity.this);
            metricNameTextViews[i].setId(View.generateViewId());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.BELOW, metricNameTextViews[i - 1].getId());
            params.setMargins(8,8,8,0);
            metricNameTextViews[i].setLayoutParams(params);
            metricNameTextViews[i].setTextColor(ContextCompat.getColor(this, R.color.white));
            metricNameTextViews[i].setText(metricNames[i]);
            rl.addView(metricNameTextViews[i]);
        }
    }
    private void setMetricTextViews()
    {
        metricTextViews = new TextView[7];
        metricTextViews[0] = new TextView(TrainingBarPathActivity.this);
        metricTextViews[0].setId(View.generateViewId());
        RelativeLayout.LayoutParams metricTextViewParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        metricTextViewParam.addRule(RelativeLayout.RIGHT_OF, metricNameTextViews[0].getId());
        metricTextViewParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        metricTextViewParam.setMargins(8,8,8,0);
        metricTextViews[0].setLayoutParams(metricTextViewParam);
        metricTextViews[0].setTextColor(ContextCompat.getColor(this, R.color.white));
        rl.addView(metricTextViews[0]);
        for(int i = 1; i < 7; i++)
        {
            metricTextViews[i] = new TextView(TrainingBarPathActivity.this);
            metricTextViews[i].setId(View.generateViewId());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.RIGHT_OF, metricNameTextViews[i].getId());
            params.addRule(RelativeLayout.BELOW, metricTextViews[i - 1].getId());
            params.setMargins(8,8,8,0);
            metricTextViews[i].setLayoutParams(params);
            metricTextViews[i].setTextColor(ContextCompat.getColor(this, R.color.white));
            rl.addView(metricTextViews[i]);
        }
    }
    private void setMetricUnitTextViews()
    {
        metricUnitTextViews = new TextView[6];
        String[] metricUnits = new String[]{"N/in/s^2", "N/in/s^2", "N/in/s^2", "in/s", "in/s", "in/s"};
        metricUnitTextViews[0] = new TextView(TrainingBarPathActivity.this);
        metricUnitTextViews[0].setId(View.generateViewId());
        RelativeLayout.LayoutParams metricUnitTextViewParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        metricUnitTextViewParam.addRule(RelativeLayout.RIGHT_OF, metricTextViews[0].getId());
        metricUnitTextViewParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        metricUnitTextViewParam.setMargins(8,8,8,0);
        metricUnitTextViews[0].setLayoutParams(metricUnitTextViewParam);
        metricUnitTextViews[0].setTextColor(ContextCompat.getColor(this, R.color.white));
        metricUnitTextViews[0].setText(metricUnits[0]);
        rl.addView(metricUnitTextViews[0]);
        for(int i = 1; i < metricUnitTextViews.length; i++)
        {
            metricUnitTextViews[i] = new TextView(TrainingBarPathActivity.this);
            metricUnitTextViews[i].setId(View.generateViewId());
            RelativeLayout.LayoutParams metricUnitTextViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            metricUnitTextViewParams.addRule(RelativeLayout.RIGHT_OF, metricTextViews[i].getId());
            metricUnitTextViewParams.addRule(RelativeLayout.BELOW, metricUnitTextViews[i - 1].getId());
            metricUnitTextViewParams.setMargins(4,8,8,0);
            metricUnitTextViews[i].setLayoutParams(metricUnitTextViewParams);
            metricUnitTextViews[i].setTextColor(ContextCompat.getColor(this, R.color.white));
            metricUnitTextViews[i].setText(metricUnits[i]);
            rl.addView(metricUnitTextViews[i]);
        }
    }
    private void setBackButton()
    {
        backButton = new FloatingActionButton(TrainingBarPathActivity.this);
        // Needs to have unique ID set in order for rules to play nice
        // IDK why, but it does work and its an easy fix so :shrug:
        backButton.setId(View.generateViewId());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.setMargins(0,0,8,8);
        backButton.setLayoutParams(params);
        backButton.setBackgroundTintList(getApplicationContext().getColorStateList(R.color.buttonRed));
        backButton.setClickable(true);
        backButton.setFocusable(true);
        backButton.setImageResource(R.drawable.ic_baseline_arrow_back_24);
        backButton.setOnClickListener(view -> {
            chartData.setWorkoutEndTime(new Date());
            saveData.setWorkoutEndTime(new Date());
            SaveWorkoutsHelper.createSave(saveData, rl.getContext());
            Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);
            startActivity(i);
        });
        rl.addView(backButton);
    }
    private void setChartDesign()
    {
        chart.setGridBackgroundColor(0xFFFFFFFF);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
    }
    private void setAxisDesign()
    {
        yAxis = chart.getAxisLeft();
        xAxis = chart.getXAxis();
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
        yAxis.setTextColor(0xFFFFFFFF);
        xAxis.setTextColor(0xFFFFFFFF);
        yAxis.setDrawGridLines(false);
        xAxis.setDrawGridLines(false);
        yAxis.setDrawAxisLine(false);
        xAxis.setDrawAxisLine(false);
        yAxis.setDrawLabels(false);
        xAxis.setDrawLabels(false);

        yAxis.setAxisMaximum(Y_AXIS_DEFAULT);
        yAxis.setAxisMinimum(0.0f);
        xAxis.setAxisMaximum(X_AXIS_DEFAULT);
        xAxis.setAxisMinimum(0.0f);
    }
    private void setUpSaveData(Bundle metaData)
    {
        // TODO Possible to optimize this? Does it need to be an ArrayList of specifically Entry? Investigate further
        ArrayList<Entry> saveDataPoints = new ArrayList<>();
        saveDataSet = new LineDataSet(saveDataPoints, "Test Data");
        // Need to preload graph with something, else negative array size errors get thrown
        // Something weird in the way the library was implemented, not noted in documentation either :shrug:
        saveDataSet.addEntry(new Entry(0, 0));
        saveData = new WorkoutData(saveDataSet);
        saveData.setExerciseType(metaData.getString("exercise"));
        saveData.setWeightAmount(Integer.parseInt(metaData.getString("weight")));
        saveData.setMeasurementUnit(metaData.getString("measurementUnit"));
    }
    private void setUpGraphData(Bundle metaData)
    {
        ArrayList<Entry> dataPoints = new ArrayList<>();
        graphDataSet = new LineDataSet(dataPoints, "Test Data");
        // Need to preload graph with something, else negative array size errors get thrown
        // Something weird in the way the library was implemented, not noted in documentation either :shrug:
        graphDataSet.addEntry(new Entry(0, 0));
        setLineDataSetDesign();
        chartData = new WorkoutData(graphDataSet);
        chartData.setExerciseType(metaData.getString("exercise"));
        chartData.setWeightAmount(Integer.parseInt(metaData.getString("weight")));
        chartData.setMeasurementUnit(metaData.getString("measurementUnit"));
        chart.setData(chartData);
    }
    private void setLineDataSetDesign()
    {
        graphDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        graphDataSet.setLineWidth(LINE_WIDTH);
        graphDataSet.setColor(LINE_COLOR);
        graphDataSet.setDrawCircles(false);
        graphDataSet.setCircleColor(LINE_COLOR);
        graphDataSet.setCircleHoleColor(LINE_COLOR);
        graphDataSet.setCircleRadius(10f);
    }
    private void setChartUpdateListener(Bundle metaData)
    {
        chartUpdateListener = new LineChartWorker(
                chartData,
                Integer.parseInt(metaData.getString("weight")),
                metaData.getString("measurementUnit").equals("kg")
        );
        chartUpdateListener.setRealTimeDataListener((data, metrics) -> {
            chartData = data;
            updateTextViews(metrics);
            Entry dataEntry = data.getDataSetByIndex(0).getEntryForIndex(0);
            saveData.addEntry(dataEntry, 0);
            // Setting a fixed maximum for both of the axis means the library won't try and automatically
            // Change the size to fit the data currently on the screen, which kinda defeats the entire goal
            // Of what im doing here
            if(dataEntry.getY() > yAxis.getAxisMaximum())
            {
                yAxis.setAxisMaximum(dataEntry.getY());
            }
            if(dataEntry.getX() > (xAxis.getAxisMaximum() / 2))
            {
                // We want the data points to be in the middle of the graph, so I double it for the axis
                // So the data is roughly center
                xAxis.setAxisMaximum(dataEntry.getX() * 2);
            }
            chart.notifyDataSetChanged();
            chart.invalidate();
        });
    }
    private void updateTextViews(double[] metrics)
    {
        int len = metricTextViews.length;
        for(int i = 0; i < len; i++)
        {
            metricTextViews[i].setText(df.format(metrics[i]));
        }
    }
    private void setCountDown()
    {
        countdownText = new TextView(TrainingBarPathActivity.this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        countdownText.setLayoutParams(params);
        countdownText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        // Gonna be consistent with the shade of red used
        countdownText.setTextColor(LINE_COLOR);
        rl.addView(countdownText);
        // TODO Make this only start counting once screen is fully drawn and responsive
        // That way I don't have to do this hacky shit starting a full second early and hoping no one notices
        cdt = new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long l) {
                countdownText.setText(Long.toString(l/1000));
            }

            @Override
            public void onFinish() {
                countdownText.setText("");
                chartData.setWorkoutStartTime(new Date());
                saveData.setWorkoutStartTime(new Date());
                reenableCircles();
                chartUpdateListener.startChartUpdates();
            }
        };
        cdt.start();
    }
    private void reenableCircles()
    {
        graphDataSet.setDrawCircles(true);
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        destroyCountDownTimer();
    }
    private void destroyCountDownTimer()
    {
        if(cdt != null)
        {
            cdt.cancel();
        }
    }
}
