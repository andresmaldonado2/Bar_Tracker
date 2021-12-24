package com.example.main_menu.exceptions;

import android.view.View;
import com.google.android.material.snackbar.Snackbar;

public class MainMenuInputDataException extends RuntimeException
{
    // R.id, R.strings, etc. are all just ints under the hood hence the passing of a string as an int
    public MainMenuInputDataException(View view, int errorMessage)
    {
        super();
        Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show();
    }
}
