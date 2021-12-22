package com.example.main_menu.interfaces;

import com.example.main_menu.charting.WorkoutData;

public interface RealTimeDataListener
{
    void onLineDataSetUpdate(WorkoutData data, double[] metrics);
}
