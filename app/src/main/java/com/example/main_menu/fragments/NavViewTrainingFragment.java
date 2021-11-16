package com.example.main_menu.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.main_menu.MainMenuActivity;
import com.example.main_menu.R;
import com.example.main_menu.viewmodels.NavigationViewModel;

public class NavViewTrainingFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.nav_view_body_training_fragment, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavigationViewModel viewModel = new ViewModelProvider(requireActivity()).get((NavigationViewModel.class));
        LinearLayout trainingMenuButton = view.findViewById(R.id.trainingMenuButtonFragment);
        trainingMenuButton.setOnClickListener(v -> {
            viewModel.setTrainingButtonListener(1);
        });
    }
}
