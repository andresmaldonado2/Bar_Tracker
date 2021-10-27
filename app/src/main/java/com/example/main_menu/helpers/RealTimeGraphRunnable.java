package com.example.main_menu.helpers;

import com.github.mikephil.charting.data.LineDataSet;

public class RealTimeGraphRunnable implements Runnable
{
    private LineDataSet data;
    DataSimulationHelper sim;
    public RealTimeGraphRunnable(LineDataSet data, DataSimulationHelper sim)
    {
        super();
        this.data = data;
        this.sim = sim;
    }

    @Override
    public void run()
    {

    }
}
