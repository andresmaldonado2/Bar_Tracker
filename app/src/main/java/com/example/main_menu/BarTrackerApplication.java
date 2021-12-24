package com.example.main_menu;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BarTrackerApplication extends Application
{
    // Lets me do very abstracted away multithreading
    // May remove in future since its almost too much abstraction tbh
    private final static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_CORES);
    Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());
}
