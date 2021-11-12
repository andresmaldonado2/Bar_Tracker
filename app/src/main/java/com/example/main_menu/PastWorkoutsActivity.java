package com.example.main_menu;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class PastWorkoutsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_workouts);
        File[] files = new File("/path/to/the/directory").listFiles();
        if(files != null)
        {
            for (File file : files)
            {

            }
        }
    }
}
