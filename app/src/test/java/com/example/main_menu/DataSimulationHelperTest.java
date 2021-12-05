package com.example.main_menu;

import com.example.main_menu.helpers.DataSimulationHelper;

import org.junit.Test;

import java.util.Arrays;

public class DataSimulationHelperTest
{
    @Test
    public void createSimulationDataTest()
    {
        DataSimulationHelper sim = new DataSimulationHelper(5, 12);
        double[] data = sim.nextDataPoint();
        System.out.println(Arrays.toString(data));
    }
}
