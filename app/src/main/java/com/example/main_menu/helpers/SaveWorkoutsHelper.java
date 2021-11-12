package com.example.main_menu.helpers;

import android.os.Environment;

import com.example.main_menu.charting.WorkoutData;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveWorkoutsHelper
{
    public static void createSave(WorkoutData data)
    {
        JSONObject workoutSave = new JSONObject();
        JSONObject workoutMetaData = new JSONObject();
        JSONArray dataPoints = new JSONArray();

        ILineDataSet dataSet = data.getDataSetByIndex(0);
        try
        {
            int i = 0;
            while(data.getEntryCount() > 2)
            {
                // TODO Add these numbers to the JSON file when we get there
                JSONArray dataPoint = new JSONArray();
                dataPoint.put(dataSet.getEntryForIndex(i));
                dataPoint.put(dataSet.getEntryForIndex(i + 1));
                // Worried about memory issues when dealing with the entirety of the data points
                // Already know that having two separate copies of the data in memory at once *will*
                // cause an outofmemory error, need to delete what I copy
                dataSet.removeEntry(i);
                dataSet.removeEntry(i + 1);
                dataPoints.put(dataPoint);
                i += 2;
            }
            workoutSave.put("Workout Meta Data", workoutMetaData);
            workoutSave.put("Workout Data Points", dataPoints);
        }
        catch (JSONException e)
        {
            // TODO Do something useful here rather than just catching the exception
            System.err.println(e);
        }
        try
        {
            String JSONString = workoutSave.toString();
            String fileName = data.getExerciseType() + "_" + data.getWorkoutStartTime();
            if (Environment.getExternalStorageState().equals("MEDIA_MOUNTED"))
            {
                if (!new File(Environment.getExternalStorageDirectory() + "WorkoutData").exists())
                {
                    createWorkoutDataFolder();
                }
                attemptCreateSaveFile(JSONString, fileName);
            }
            else
            {
                throw new IOException();
            }
        }
        catch (IOException e)
        {
            // TODO Do something useful here rather than just catching the exception
            System.err.println(e);
        }
    }
    private static void createWorkoutDataFolder() throws IOException
    {
        if (Environment.getExternalStorageState().equals("MEDIA_MOUNTED"))
        {
            if(new File(Environment.getExternalStorageDirectory() + "WorkoutData").mkdirs())
            {
                return;
            }
            else
            {
                throw new IOException();
            }
        }
    }
    private static void attemptCreateSaveFile(String JSONString, String fileName) throws IOException
    {
        File file = new File(Environment.getExternalStorageDirectory() + "WorkoutData", fileName);
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(JSONString);
        bufferedWriter.close();
    }
}
