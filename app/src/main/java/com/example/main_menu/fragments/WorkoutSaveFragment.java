package com.example.main_menu.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import com.example.main_menu.PastWorkoutDataActivity;
import com.example.main_menu.PastWorkoutsActivity;
import com.example.main_menu.R;
import com.example.main_menu.SettingsActivity;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class WorkoutSaveFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.workout_save_fragment, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        HashMap<String, Integer> viewIds = new HashMap<>();
        SimpleDateFormat defaultFormatParser = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm", Locale.US);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm", Locale.US);
        String timeDurationString = "";
        String dateString = "";
        int idHolder;
        Bundle metaData = this.getArguments();

        ViewGroup rootView = (ViewGroup) getView();
        int childViewCount = 0;
        if (rootView != null)
        {
            childViewCount = rootView.getChildCount();
        }
        else
        {
            throw new RuntimeException("Codes fucked here, root view was null in runtime???");
        }
        for (int i = 0; i < childViewCount; i++)
        {
            View viewInFragment = rootView.getChildAt(i);
            idHolder = View.generateViewId();
            // Look at workout_save_fragment.xml for the original ID names, they are the keys in the hash map
            // No matter which fragment you are looking at
            String resourceName = view.getResources().getResourceName(viewInFragment.getId());
            // The getResourceName method returns back the entire package path it came from, which is not needed
            // So we need to remove it, hence this:
            resourceName = resourceName.substring(resourceName.indexOf("/") + 1);
            viewIds.put(resourceName, idHolder);
            viewInFragment.setId(idHolder);
        }
        // TODO Could possibly also put this into loop by changing bundle key to resourceName, investigate later
        ((TextView) view.findViewById(viewIds.get("workoutSaveExerciseTypeText"))).setText(metaData.getString("exercise"));
        try
        {
            Date timeStart = defaultFormatParser.parse(metaData.getString("workoutStartTime"));
            Date timeEnd = defaultFormatParser.parse(metaData.getString("workoutEndTime"));
            long timeDuration = timeEnd.getTime() - timeStart.getTime();
            timeDurationString  = calculateTimeDifference(timeDuration);

            dateString = dateFormat.format(defaultFormatParser.parse(metaData.getString("workoutStartTime")));
        } catch (ParseException e)
        {
            //TODO Do something here other than just catching exception
        }
        ((TextView) view.findViewById(viewIds.get("workoutSaveExerciseDateText"))).setText(dateString);
        ((TextView) view.findViewById(viewIds.get("workoutSaveExerciseDurationText"))).setText(timeDurationString);

        Log.d("DEBUG", "Date String: " + dateString);
        Log.d("DEBUG", "Bundle Date String: " + metaData.getString("workoutStartTime"));
        Log.d("DEBUG", "Duration String: " + timeDurationString);

        // TODO Replaces "Reps: " with strings.xml resource for translations
        ((TextView) view.findViewById(viewIds.get("workoutSaveExerciseRepsText"))).setText("Reps: " + metaData.getString("numOfReps"));

        setLayoutConstraints((ConstraintLayout) view, viewIds);
        setUpClickListener((ConstraintLayout) view, metaData);
    }

    private String calculateTimeDifference(long duration)
    {
        long timeDuration = duration;
        long secInMs = 1000;
        long minInMs = secInMs * 60;
        long elapsedMinutes = timeDuration / minInMs;
        timeDuration = timeDuration % minInMs;
        long elapsedSeconds = timeDuration / secInMs;
        return elapsedMinutes + ":" + elapsedSeconds;
    }
    private void setLayoutConstraints(ConstraintLayout view, HashMap<String, Integer> viewIds)
    {
        ConstraintSet constraints = new ConstraintSet();
        constraints.clone(view);
        constraints.connect(viewIds.get("workoutSaveExerciseTypeText"), ConstraintSet.START, viewIds.get("workoutSaveExerciseTypeImage"), ConstraintSet.END);
        constraints.connect(viewIds.get("workoutSaveExerciseDateText"), ConstraintSet.START, viewIds.get("workoutSaveExerciseTypeImage"), ConstraintSet.END);
        constraints.applyTo(view);
    }
    private void setUpClickListener(ConstraintLayout view, Bundle saveData)
    {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), PastWorkoutDataActivity.class);
                i.putExtras(saveData);
                startActivity(i);
            }
        });
    }
}
