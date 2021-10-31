package com.example.main_menu.interfaces;

import com.github.mikephil.charting.data.LineData;

public interface RealTimeDataListener
{
    void onLineDataSetUpdate(LineData data);
}
