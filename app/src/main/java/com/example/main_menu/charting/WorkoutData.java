package com.example.main_menu.charting;

import com.github.mikephil.charting.data.BaseDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.Date;

public class WorkoutData extends LineData
{
    private String exerciseType;
    private String measurementUnit;
    private int numOfReps;
    private int weightAmount;
    private Date workoutStartTime;
    private Date workoutEndTime;


    public WorkoutData(LineDataSet dataSet)
    {
        super(dataSet);
    }

    public String getExerciseType()
    {
        return exerciseType;
    }
    public int getNumOfReps()
    {
        return numOfReps;
    }
    public Date getWorkoutStartTime()
    {
        return workoutStartTime;
    }
    public Date getWorkoutEndTime()
    {
        return workoutEndTime;
    }
    public int getWeightAmount()
    {
        return weightAmount;
    }
    public String getMeasurementUnit()
    {
        return measurementUnit;
    }
    public void setExerciseType(String exerciseType)
    {
        this.exerciseType = exerciseType;
    }
    public void setWorkoutStartTime(Date workoutStartTime)
    {
        this.workoutStartTime = workoutStartTime;
    }
    public void setWorkoutEndTime(Date workoutEndTime)
    {
        this.workoutEndTime = workoutEndTime;
    }
    public void setNumOfReps(int numOfReps)
    {
        this.numOfReps = numOfReps;
    }
    public void setWeightAmount(int weightAmount)
    {
        this.weightAmount = weightAmount;
    }
    public void setMeasurementUnit(String measurementUnit)
    {
        this.measurementUnit = measurementUnit;
    }
}
