package com.example.main_menu.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.main_menu.R;
import com.example.main_menu.viewmodels.NavigationViewModel;

public class NavViewPastWorkoutsFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.nav_view_body_past_workouts_fragment, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        NavigationViewModel viewModel = new ViewModelProvider(requireActivity()).get((NavigationViewModel.class));
        LinearLayout pastWorkoutsButton = view.findViewById(R.id.pastWorkoutsMenuButtonFragment);
        pastWorkoutsButton.setOnClickListener(v -> {
            viewModel.setPastWorkoutButtonListener(1);
            Log.d("DEBUG", "Button was clicked");
        });
    }
}
