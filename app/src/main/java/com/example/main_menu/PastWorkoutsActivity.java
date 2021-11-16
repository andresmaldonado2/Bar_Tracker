package com.example.main_menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Pair;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.main_menu.charting.WorkoutData;
import com.example.main_menu.fragments.WorkoutSaveFragment;
import com.example.main_menu.viewmodels.NavigationViewModel;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PastWorkoutsActivity extends AppCompatActivity
{
    private FragmentManager fragmentManager;
    @IdRes private ArrayList<Integer> saveFragmentIds =  new ArrayList<>();
    @IdRes private ArrayList<Integer> saveFragmentContainerIds = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // TODO This seems very hacky, I'm sure theres a way to get an event listener from the viewmodel rather than this
        NavigationViewModel viewModel = new ViewModelProvider(this).get(NavigationViewModel.class);
        fragmentManager = getSupportFragmentManager();

        viewModel.resetListeners();
        viewModel.getPastWorkoutButtonListener().observe(this, new Observer<Pair<Integer, Boolean>>() {
            @Override
            public void onChanged(Pair<Integer, Boolean> signal) {
                DrawerLayout drawerLayout = findViewById(R.id.pastWorkoutsRootView);
                NavigationView navView = findViewById(R.id.navViewBody);
                drawerLayout.closeDrawer(navView);
            }
        });
        viewModel.getTrainingButtonListener().observe(this, new Observer<Pair<Integer, Boolean>>() {
            @Override
            public void onChanged(Pair<Integer, Boolean> signal)
            {
                if(signal.second != null && signal.second)
                {
                    Intent i = new Intent(PastWorkoutsActivity.this, MainMenuActivity.class);
                    startActivity(i);
                }
            }
        });
        viewModel.getSettingsButtonListener().observe(this, new Observer<Pair<Integer, Boolean>>() {
            @Override
            public void onChanged(Pair<Integer, Boolean> signal)
            {
                if(signal.second != null && signal.second)
                {
                    Intent i = new Intent(PastWorkoutsActivity.this, SettingsActivity.class);
                    startActivity(i);
                }
            }
        });

        File[] files = new File(this.getFilesDir() + "/WorkoutData").listFiles();
        if(files != null)
        {
            for (File file : files)
            {
                Bundle metaData = createMetaDataBundle(file);
                metaData.putString("saveFileName", file.getName());
                WorkoutSaveFragment saveFragment = new WorkoutSaveFragment();
                saveFragment.setArguments(metaData);
                fragmentManager.beginTransaction()
                        .add(R.id.workoutSaveFragmentsContainer, saveFragment).commit();
                // Uncomment this if for whatever reason their ends up being some race condition involved
                // In loading the fragments, this forces the program to wait until they're all done executing to continue
                //fragmentManager.executePendingTransactions();
            }
        }
        else
        {
            Log.d("DEBUG", "Couldn't find list of files");
        }
        setContentView(R.layout.activity_past_workouts);
    }
    private Bundle createMetaDataBundle(File file)
    {
        StringBuilder stringBuilder = new StringBuilder();
        Bundle metaDataBundle = new Bundle();
        try
        {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null)
            {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        }
        catch(IOException e)
        {
            //TODO Actually do something here instead of just catching the exception
        }
        try
        {
            JSONObject jsonData = new JSONObject(stringBuilder.toString());
            JSONObject metaDataJson = jsonData.getJSONObject("Workout Meta Data");
            metaDataBundle.putString("weight", metaDataJson.get("Weight Amount").toString());
            metaDataBundle.putString("exercise", metaDataJson.get("Exercise Type").toString());
            metaDataBundle.putString("measurementUnit", metaDataJson.get("Measurement Unit").toString());
            metaDataBundle.putString("numOfReps", metaDataJson.get("Number of Reps").toString());
            metaDataBundle.putString("workoutStartTime", metaDataJson.get("Workout Start Time").toString());
            metaDataBundle.putString("workoutEndTime", metaDataJson.get("Workout End Time").toString());
        }
        catch(JSONException e)
        {
            //TODO Actually do something here instead of just catching the exception
        }
        return metaDataBundle;
    }
}
