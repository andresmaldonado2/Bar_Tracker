package com.example.main_menu.callbacks;

import com.example.main_menu.interfaces.NavPastWorkoutButtonListener;

public class NavPastWorkoutButtonWorker
{
    private NavPastWorkoutButtonListener listener;
    public void buttonPress()
    {
        listener.onButtonPress();
    }
    public void setListener(NavPastWorkoutButtonListener listener)
    {
        this.listener = listener;
    }
}
