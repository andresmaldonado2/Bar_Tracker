package com.example.main_menu.helpers;

import android.content.Context;
import android.os.Environment;

import com.example.main_menu.charting.WorkoutData;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;

public class SaveWorkoutsHelper
{
    public static void createSave(WorkoutData data, Context context)
    {
        JSONObject workoutSave = new JSONObject();
        JSONObject workoutMetaData = new JSONObject();
        JSONArray dataPoints = new JSONArray();

        ILineDataSet dataSet = data.getDataSetByIndex(0);
        try
        {
            while(data.getEntryCount() >= 1)
            {
                JSONArray dataPoint = new JSONArray();
                dataPoint.put(dataSet.getEntryForIndex(0).getX());
                dataPoint.put(dataSet.getEntryForIndex(0).getY());
                // TODO Seems like every single is a x y pair and not just a singular, change this...
                // fuck me...
                // Worried about memory issues when dealing with the entirety of the data points
                // Already know that having two separate copies of the data in memory at once *will*
                // cause an outofmemory error, need to delete what I copy
                // I delete 0 twice because once I delete the x point, the y point which was at index 1 is now at index 0
                dataSet.removeEntry(0);
                dataPoints.put(dataPoint);
            }


            workoutMetaData.put("Weight Amount",data.getWeightAmount());
            workoutMetaData.put("Exercise Type", data.getExerciseType());
            workoutMetaData.put("Measurement Unit", data.getMeasurementUnit());
            workoutMetaData.put("Number of Reps", data.getNumOfReps());
            workoutMetaData.put("Workout Start Time", data.getWorkoutStartTime());
            workoutMetaData.put("Workout End Time", data.getWorkoutEndTime());


            workoutSave.put("Workout Meta Data", workoutMetaData);
            workoutSave.put("Workout Data Points", dataPoints);
        }
        catch (JSONException e)
        {
            // TODO Do something useful here rather than just catching the exception
            e.printStackTrace(System.out);
        }
        try
        {
            String JSONString = workoutSave.toString();
            String fileName = data.getExerciseType() + "_" + data.getWorkoutStartTime();
            if (!new File(context.getFilesDir() + "/WorkoutData").exists())
            {
                new File(context.getFilesDir() + "/WorkoutData").mkdirs();
            }
            attemptCreateSaveFile(JSONString, fileName, context);
        }
        catch (IOException e)
        {
            // TODO Do something useful here rather than just catching the exception
            e.printStackTrace(System.out);
        }
    }
    private static void attemptCreateSaveFile(String JSONString, String fileName, Context context) throws IOException
    {
        FileWriter fileWriter = new FileWriter(new File(context.getFilesDir() + "/WorkoutData/" + fileName));
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(JSONString);
        bufferedWriter.close();
    }
}
