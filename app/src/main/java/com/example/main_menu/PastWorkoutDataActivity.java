package com.example.main_menu;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.ArrayList;

public class PastWorkoutDataActivity extends AppCompatActivity
{
    private RelativeLayout rl;
    private LineChart chart;
    private LineDataSet graphDataSet;
    private WorkoutData chartData;
    private final int LINE_COLOR = 0xFFB00020;
    private final int BACKGROUND_COLOR = 0xFF121212;
    private final float LINE_WIDTH = 5f;


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        chart = new LineChart(this);
        rl = new RelativeLayout(this);
        rl.setBackgroundColor(BACKGROUND_COLOR);
        Bundle fileData = getIntent().getExtras();
        ArrayList<Entry> dataPoints = new ArrayList<>();
        graphDataSet = new LineDataSet(dataPoints, "Save Data");
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
            JSONObject jsonData = new JSONObject(stringBuilder.toString());
            //JSONObject metaDataJson = jsonData.getJSONObject("Workout Meta Data");
            JSONArray dataPointsJson = jsonData.getJSONArray("Workout Data Points");
            for(int i = 0; i < dataPointsJson.length(); i++)
            {
                JSONArray dataPoint  = dataPointsJson.getJSONArray(i);
                // TODO May have to change these to parse rather than cast
                //Log.d("DEBUG", (String) dataPoint.get(0));
                graphDataSet.addEntry(new Entry(Float.parseFloat(dataPoint.getString(0)), Float.parseFloat(dataPoint.getString(1))));
            }
        }
        catch (JSONException e)
        {
            //TODO Actually do something here instead of just catching the exception
        }
        setLineDataSetDesign(graphDataSet);
        setChartDesign(chart);
        chartData = new WorkoutData(graphDataSet);
        chart.setData(chartData);
        chart.invalidate();
        setAxisDesign(chart);
        RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rl.addView(chart, new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
        this.addContentView(rl, rlParams);
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
}
