package com.example.main_menu.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.main_menu.PastWorkoutsActivity;
import com.example.main_menu.R;

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
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        LinearLayout pastWorkoutsButton = view.findViewById(R.id.pastWorkoutsMenuButtonFragment);
        pastWorkoutsButton.setOnClickListener(v -> {
            if(v.getRootView().getId() != R.id.pastWorkoutsRootView)
            {
                Intent i = new Intent(view.getContext(), PastWorkoutsActivity.class);
                startActivity(i);
            }
            else
            {
                // I know that every one of my screens at the moment has a drawer as the root view
                // Rn, so this technically does work
                // Very ugly, but does work
                // TODO figure out a more elegant way to do this instead of just casting to a drawer layout
                DrawerLayout temp = (DrawerLayout) view.getRootView();
                temp.closeDrawers();
            }
        });
    }
}
