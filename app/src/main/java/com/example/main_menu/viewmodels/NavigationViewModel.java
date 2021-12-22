package com.example.main_menu.viewmodels;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NavigationViewModel extends ViewModel
{
    // TODO Figure out way to reset these to zero without screwing up other observers, cuz otherwise this is a theoretical overflow just waiting to happen
    // NOTE FROM ANDRES TO PROFESSOR: I am pretty certain I overcomplicated this, and don't need to do all of this
    // I was trying to make it so instead of reloading an activity that was already loaded it would
    // Just close the drawer, but knowing how convoluted this solution is I am like 99% certain theres
    // A proper way to do this I just did not find in my research
    private final MutableLiveData<Pair<Integer, Boolean>> pastWorkoutButtonListener = new MutableLiveData<>(new Pair<>(0, Boolean.FALSE));
    private final MutableLiveData<Pair<Integer, Boolean>> trainingButtonListener = new MutableLiveData<>(new Pair<>(0, Boolean.FALSE));
    private final MutableLiveData<Pair<Integer, Boolean>> settingsButtonListener = new MutableLiveData<>(new Pair<>(0, Boolean.FALSE));

    public LiveData<Pair<Integer, Boolean>> getPastWorkoutButtonListener()
    {
        return pastWorkoutButtonListener;
    }

    public MutableLiveData<Pair<Integer, Boolean>> getSettingsButtonListener()
    {
        return settingsButtonListener;
    }

    public MutableLiveData<Pair<Integer, Boolean>> getTrainingButtonListener()
    {
        return trainingButtonListener;
    }

    public void setPastWorkoutButtonListener(Integer signal)
    {
        pastWorkoutButtonListener.setValue(new Pair<>(signal, Boolean.TRUE));
    }

    public void setSettingsButtonListener(Integer signal)
    {
        settingsButtonListener.setValue(new Pair<>(signal, Boolean.TRUE));
    }

    public void setTrainingButtonListener(Integer signal)
    {
        trainingButtonListener.setValue(new Pair<>(signal, Boolean.TRUE));
    }
    public void resetListeners()
    {
        pastWorkoutButtonListener.setValue(new Pair<>(0, Boolean.FALSE));
        settingsButtonListener.setValue(new Pair<>(0, Boolean.FALSE));
        trainingButtonListener.setValue(new Pair<>(0, Boolean.FALSE));
    }
}
