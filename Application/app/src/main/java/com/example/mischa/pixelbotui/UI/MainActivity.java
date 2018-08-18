package com.example.mischa.pixelbotui.UI;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.mischa.pixelbotui.Intergration.SwarmAdapter;
import com.example.mischa.pixelbotui.Swarm.Bot;

import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends Activity {

    PBCanvas canvas;
    int[] saveState;
    int[] restoreState;
    public static HashMap<Integer, Integer> BotAmounts = new HashMap<>();


    // Called when activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        canvas = new PBCanvas(this);
        canvas.setBackgroundColor(Color.WHITE);
        setContentView(canvas);

        // The amount of bots we have to work with
        BotAmounts.put(-1162650,    30); //Red
        BotAmounts.put(-11713,      0); //Yellow
        BotAmounts.put(-15815319,   0); //Green
        BotAmounts.put(-12857684,   0); //Blue
        BotAmounts.put(-11268754,   0); //Purple
        BotAmounts.put(Color.BLACK, 0); //Black


    }

    // Save the state of the grid in the Bundle (similar to HashMap)
    @Override
    protected void onSaveInstanceState (Bundle state) {
        saveState = canvas.getSavedState();
        for (int i = 0; i < saveState.length; i++) {
            state.putInt("" + i, saveState[i]);
        }
        state.putInt("colour", canvas.selectedColour);
        System.out.println("Save: " + canvas.whiteBox);
        state.putInt("whiteBox", canvas.whiteBox);
    }

    // Get the saved state of the grid from the Bundle
    protected void onRestoreInstanceState (Bundle state) {
        restoreState = new int[state.size()];
        for (int i = 0; i < state.size(); i++) {
            restoreState[i] = state.getInt("" + i);
        }
        canvas.selectedColour = state.getInt("colour");
        canvas.whiteBox = state.getInt("whiteBox");
        canvas.giveRestoreState(restoreState);


    }
}
