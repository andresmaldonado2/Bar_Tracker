package com.example.main_menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.main_menu.fragments.SettingsMenuFragment;
import com.example.main_menu.viewmodels.NavigationViewModel;
import com.google.android.material.navigation.NavigationView;

public class SettingsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTheme(R.style.PreferenceCategoryTheme);
        setContentView(R.layout.settings_main);

        NavigationViewModel viewModel = new ViewModelProvider(this).get(NavigationViewModel.class);
        DrawerLayout rootView = (DrawerLayout) findViewById(R.id.settingsRootView);
        viewModel.resetListeners();
        setNavMenuButtonListeners(viewModel, rootView);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settingsMenu, new SettingsMenuFragment())
                .commit();
    }
    private void setNavMenuButtonListeners(NavigationViewModel viewModel, DrawerLayout rootView)
    {
        ImageButton navBarButton = findViewById(R.id.navBarButton);
        navBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootView.openDrawer(GravityCompat.START);
            }
        });

        viewModel.getPastWorkoutButtonListener().observe(this, new Observer<Pair<Integer, Boolean>>() {
            @Override
            public void onChanged(Pair<Integer, Boolean> signal)
            {
                if(signal.second != null && signal.second)
                {
                    Intent i = new Intent(rootView.getContext(), PastWorkoutsActivity.class);
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
                    Intent i = new Intent(rootView.getContext(), MainMenuActivity.class);
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
                    NavigationView navView = findViewById(R.id.navViewBody);
                    rootView.closeDrawer(navView);
                }
            }
        });
    }
}
