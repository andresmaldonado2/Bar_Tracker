package com.example.main_menu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Pair;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.main_menu.databinding.ActivityMainBinding;
import com.example.main_menu.viewmodels.NavigationViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class MainMenuActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        DrawerLayout view = binding.root;
        setContentView(view);
        NavigationViewModel viewModel = new ViewModelProvider(this).get(NavigationViewModel.class);
        viewModel.resetListeners();

        EditText weightInput = findViewById(R.id.weight_input);
        AutoCompleteTextView exerciseInput = findViewById(R.id.exercise_input);
        SwitchMaterial lbSwitch = findViewById(R.id.lbMeasurementSwitch);
        SwitchMaterial kgSwitch = findViewById(R.id.kgMeasurementSwitch);

        // TODO I'm pretty sure these dont actually have to be fragments negating all this shit, replace it with just a normal xml
        viewModel.getPastWorkoutButtonListener().observe(this, new Observer<Pair<Integer, Boolean>>() {
            @Override
            public void onChanged(Pair<Integer, Boolean> signal)
            {
                if(signal.second != null && signal.second)
                {
                    Intent i = new Intent(view.getContext(), PastWorkoutsActivity.class);
                    startActivity(i);
                }
            }
        });

        viewModel.getTrainingButtonListener().observe(this, new Observer<Pair<Integer, Boolean>>() {
            @Override
            public void onChanged(Pair<Integer, Boolean> signal)
            {
                if(signal.second != null && signal.second)
                {
                    NavigationView navView = findViewById(R.id.navViewBody);
                    view.closeDrawer(navView);
                }
            }
        });

        viewModel.getSettingsButtonListener().observe(this, new Observer<Pair<Integer, Boolean>>() {
            @Override
            public void onChanged(Pair<Integer, Boolean> signal)
            {
                if(signal.second != null && signal.second)
                {
                    Intent i = new Intent(view.getContext(), SettingsActivity.class);
                    startActivity(i);
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.startWorkoutButton);
        //TODO add animation here so the change in view isnt so abrupt
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!weightInput.getText().toString().equals(""))
                {
                    if(!exerciseInput.getText().toString().equals(""))
                    {
                        if(lbSwitch.isChecked() ^ kgSwitch.isChecked())
                        {
                            Bundle workoutMetaData = new Bundle();
                            workoutMetaData.putString("weight", weightInput.getText().toString());
                            workoutMetaData.putString("exercise", exerciseInput.getText().toString());
                            workoutMetaData.putString("measurementUnit", getMeasurementUnit(lbSwitch, kgSwitch));
                            Intent i = new Intent(getApplicationContext(), TrainingBarPathActivity.class);
                            i.putExtras(workoutMetaData);
                            startActivity(i);
                        }
                        else
                        {
                            Snackbar.make(view, R.string.incorrectMeasurementUnitInputSnackbar, Snackbar.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Snackbar.make(view, R.string.incorrectExerciseInputSnackbar, Snackbar.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Snackbar.make(view, R.string.incorrectWeightInputSnackbar, Snackbar.LENGTH_LONG).show();
                }
            }
        });

        ImageButton navBarButton = findViewById(R.id.navBarButton);
        navBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.openDrawer(GravityCompat.START);
            }
        });


        AutoCompleteTextView actv = findViewById(R.id.exercise_input);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.exerciseAutofillHints));
        actv.setAdapter(adapter);
    }
    private String getMeasurementUnit(SwitchMaterial lbSwitch, SwitchMaterial kgSwitch)
    {
        if(lbSwitch.isChecked())
        {
            return "lb";
        }
        else
        {
            return "kg";
        }
    }
}