package com.example.main_menu.interfaces;

import com.github.mikephil.charting.data.LineDataSet;

public interface RealTimeDataListener
{
    void onLineDataSetUpdate(LineDataSet data);
}
