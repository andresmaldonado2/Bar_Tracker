package com.example.main_menu;

import com.example.main_menu.helpers.DataSimulationHelper;

import org.junit.Test;

import java.util.Arrays;

public class DataSimulationHelperTest
{
    @Test
    public void createSimulationDataTest()
    {
        double[][] data = DataSimulationHelper.createSimulationData(1);
        System.out.println(Arrays.deepToString(data));
    }
}
