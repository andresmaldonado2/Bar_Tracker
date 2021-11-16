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

import com.example.main_menu.callbacks.LineChartWorker;
import com.example.main_menu.charting.WorkoutData;
import com.example.main_menu.helpers.CurveFitHelper;
import com.example.main_menu.helpers.SaveWorkoutsHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

public class TrainingBarPathActivity extends AppCompatActivity
{
    private RelativeLayout rl;
    private LineChart chart;
    private LineDataSet graphDataSet;
    private WorkoutData chartData;
    private CountDownTimer cdt;
    private TextView countdownText;
    private FloatingActionButton backButton;
    // Same color as the backgrounds in previous activities
    private final int BACKGROUND_COLOR = 0xFF121212;
    private final int LINE_COLOR = 0xFFB00020;
    private final float LINE_WIDTH = 5f;
    private LineChartWorker chartUpdateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle metaData = getIntent().getExtras();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        chart = new LineChart(getApplicationContext());
        rl = new RelativeLayout(getApplicationContext());
        backButton = new FloatingActionButton(TrainingBarPathActivity.this);
        setBackButton(backButton);
        rl.addView(backButton);
        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rl.setBackgroundColor(BACKGROUND_COLOR);

        ArrayList<Entry> dataPoints = new ArrayList<>();
        graphDataSet = new LineDataSet(dataPoints, "Test Data");
        // Need to preload graph with something, else negative array size errors get thrown
        // Something weird in the way the library was implemented, not noted in documentation either :shrug:
        graphDataSet.addEntry(new Entry(0, 0));
        setLineDataSetDesign(graphDataSet);

        chartData = new WorkoutData(graphDataSet);
        chartData.setExerciseType(metaData.getString("exercise"));
        chartData.setWeightAmount(Integer.parseInt(metaData.getString("weight")));
        chartData.setMeasurementUnit(metaData.getString("measurementUnit"));
        chart.setData(chartData);
        setChartDesign(chart);
        chart.invalidate();
        //CurveFitHelper ch = new CurveFitHelper();
        //RelativeLayout.LayoutParams chartParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        // TODO Research why the hell the layouts used in documentation to display graph don't work but specifically an absolute layout does???
        // TODO Look into changing the relative layout into something better like a constraint layout
        rl.addView(chart, new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
        this.addContentView(rl, rlParams);

        chartUpdateListener = new LineChartWorker(chartData);
        chartUpdateListener.setRealTimeDataListener(data -> {
            chartData = data;
            // TODO figure this out later chart.moveViewToX(chartData.getEntryCount()/2 - 1);
            chart.notifyDataSetChanged();
            // I might have to call this after every data change?
            // chart.setVisibleXRangeMaximum(2f);
            // chart.centerViewTo(chart.getData().getDataSetByIndex(0).);
            chart.invalidate();
        });
        setCountDown(rl, chartUpdateListener);
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
        yAxis.setDrawAxisLine(false);
        xAxis.setDrawAxisLine(false);
    }
    private void setLineDataSetDesign(LineDataSet lineDataSet)
    {
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setLineWidth(LINE_WIDTH);
        lineDataSet.setColor(LINE_COLOR);
        lineDataSet.setDrawCircles(false);
    }
    private void setChartDesign(LineChart chart)
    {
        chart.setGridBackgroundColor(0xFFFFFFFF);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        setAxisDesign(chart);
    }
    private void setCountDown(RelativeLayout rl, LineChartWorker chartWorker)
    {
        countdownText = new TextView(TrainingBarPathActivity.this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        countdownText.setLayoutParams(params);
        countdownText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        // Gonna be consistent with the shade of red used
        countdownText.setTextColor(LINE_COLOR);
        rl.addView(countdownText);
        cdt = new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long l) {
                countdownText.setText(Long.toString(l/1000));
            }

            @Override
            public void onFinish() {
                countdownText.setText("");
                chartData.setWorkoutStartTime(new Date());
                chartWorker.startChartUpdates();
            }
        };
        cdt.start();
    }
    private void destroyCountDownTimer()
    {
        if(cdt != null)
        {
            cdt.cancel();
        }
    }
    private void setBackButton(FloatingActionButton backButton)
    {
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
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chartData.setWorkoutEndTime(new Date());
                // TODO Ask if the user wants to save their workout
                // TODO Set number of reps into data once I actually get the regression working
                chartData.setNumOfReps(chartUpdateListener.getNumberOfRepsPerformed());
                SaveWorkoutsHelper.createSave(chartData, rl.getContext());
                Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(i);
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyCountDownTimer();
    }
}
