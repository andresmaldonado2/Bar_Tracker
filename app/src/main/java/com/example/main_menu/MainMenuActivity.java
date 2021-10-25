package com.example.main_menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.example.main_menu.databinding.ActivityMainBinding;
import com.example.main_menu.ui.TrainingBarPathActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainMenuActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.root;
        setContentView(view);
        FloatingActionButton fab = findViewById(R.id.startWorkoutButton);
        //TODO add animation here so the change in view isnt so abrupt
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), TrainingBarPathActivity.class);
                startActivity(i);
            }
        });
    }
}