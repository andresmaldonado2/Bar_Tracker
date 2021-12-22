package com.example.main_menu;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.main_menu.callbacks.LineChartWorker;
import com.example.main_menu.charting.WorkoutData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class PastWorkoutDataActivity extends AppCompatActivity
{
    private RelativeLayout rl;
    private LineChart chart;
    private LineDataSet graphDataSet;
    private WorkoutData chartData;
    private YAxis yAxis;
    private XAxis xAxis;
    private final int LINE_COLOR = 0xFFB00020;
    private final int BACKGROUND_COLOR = 0xFF121212;
    private final float LINE_WIDTH = 5f;
    private final float Y_AXIS_DEFAULT = 18.0f;
    private final float X_AXIS_DEFAULT = 36.0f;
    private TextView[] metricTextViews;
    private TextView[] metricNameTextViews;
    private TextView[] metricUnitTextViews;
    private DecimalFormat df = new DecimalFormat("#.##");


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Bundle fileData = getIntent().getExtras();
        StringBuilder stringBuilder = new StringBuilder();
        try
        {
            File saveFile = new File(this.getFilesDir() + "/WorkoutData/" + fileData.getString("saveFileName"));
            FileReader fileReader = new FileReader(saveFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null)
            {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        }
        catch(IOException e)
        {
            //TODO Actually do something here instead of just catching the exception
        }

        try
        {
            chart = new LineChart(getApplicationContext());
            rl = new RelativeLayout(getApplicationContext());
            rl.setBackgroundColor(BACKGROUND_COLOR);



            String[] metricNames = new String[] {"Average Force: ", "Peak Force: ", "Force During Rep: ", "Average Velocity: ", "Peak Velocity: ", "Current Velocity: ", "Reps Performed: "};
            metricTextViews = new TextView[7];
            metricNameTextViews = new TextView[7];
            metricUnitTextViews = new TextView[6];
            metricNameTextViews[0] = new TextView(PastWorkoutDataActivity.this);
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
                metricNameTextViews[i] = new TextView(PastWorkoutDataActivity.this);
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
            metricTextViews[0] = new TextView(PastWorkoutDataActivity.this);
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
                metricTextViews[i] = new TextView(PastWorkoutDataActivity.this);
                metricTextViews[i].setId(View.generateViewId());
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.RIGHT_OF, metricNameTextViews[i].getId());
                params.addRule(RelativeLayout.BELOW, metricTextViews[i - 1].getId());
                params.setMargins(8,8,8,0);
                metricTextViews[i].setLayoutParams(params);
                metricTextViews[i].setTextColor(ContextCompat.getColor(this, R.color.white));
                rl.addView(metricTextViews[i]);
            }
            String[] metricUnits = new String[]{"N/in/s^2", "N/in/s^2", "N/in/s^2", "in/s", "in/s", "in/s"};
            metricUnitTextViews[0] = new TextView(PastWorkoutDataActivity.this);
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
                metricUnitTextViews[i] = new TextView(PastWorkoutDataActivity.this);
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




            setChartDesign(chart);
            ArrayList<Entry> dataPoints = new ArrayList<>();
            graphDataSet = new LineDataSet(dataPoints, "Save Data");
            graphDataSet.addEntry(new Entry(0,0));
            setLineDataSetDesign(graphDataSet);
            chartData = new WorkoutData(graphDataSet);
            chart.setData(chartData);
            setAxisDesign(chart);
            chart.invalidate();
            RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            rl.addView(chart, new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
            this.addContentView(rl, rlParams);


            JSONObject jsonData = new JSONObject(stringBuilder.toString());
            //JSONObject metaDataJson = jsonData.getJSONObject("Workout Meta Data");
            JSONArray dataPointsJson = jsonData.getJSONArray("Workout Data Points");
            JSONArray dataPoint  = dataPointsJson.getJSONArray(0);

            chart.notifyDataSetChanged();
            chart.invalidate();
            System.out.println("DataPoints: "+ dataPointsJson.length());
            LineChartWorker chartWorker = new LineChartWorker(chartData, dataPointsJson);
            chartWorker.setRealTimeDataListener((data, metrics) -> {
                chartData = data;
                Entry dataEntry = data.getDataSetByIndex(0).getEntryForIndex(0);
                if(dataEntry.getY() > yAxis.getAxisMaximum())
                {
                    yAxis.setAxisMaximum(dataEntry.getY());
                }
                if(dataEntry.getX() > xAxis.getAxisMaximum())
                {
                    // We want the data points to be in the middle of the graph, so I double it for the axis
                    // So the data is roughly center
                    xAxis.setAxisMaximum(dataEntry.getX() * 2);
                }
                System.out.println("ENTRY: " + data.getDataSetByIndex(0).getEntryForIndex(0).toString());
                chart.notifyDataSetChanged();
                chart.invalidate();
            });
            chartWorker.startSaveDataChartUpdates();
        }
        catch (JSONException e)
        {
            //TODO Actually do something here instead of just catching the exception
            System.out.println("Exception");
            System.out.println(e.toString());
        }


    }

    private void setAxisDesign(LineChart chart)
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
    private void setLineDataSetDesign(LineDataSet lineDataSet)
    {
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setLineWidth(LINE_WIDTH);
        lineDataSet.setColor(LINE_COLOR);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setCircleColor(LINE_COLOR);
        lineDataSet.setCircleHoleColor(LINE_COLOR);
        lineDataSet.setCircleRadius(10f);
    }
    private void setChartDesign(LineChart chart)
    {
        chart.setGridBackgroundColor(0xFFFFFFFF);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        setAxisDesign(chart);
    }
    private void updateTextViews(double[] metrics)
    {
        int len = metricTextViews.length;
        for(int i = 0; i < len; i++)
        {
            metricTextViews[i].setText(df.format(metrics[i]));
        }
    }
}
