package com.example.mischa.pixelbotui.UI;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.mischa.pixelbotui.Swarm.Bot;

import java.util.HashMap;

public class MainActivity extends Activity {

    PBCanvas canvas;
    int[] saveState;
    int[] restoreState;
    public static  HashMap<Integer, Integer> BotAmounts = new HashMap<>();


    public static HashMap<Integer, Integer> BotAmounts(){
        return  BotAmounts;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        canvas = new PBCanvas(this);
        canvas.setBackgroundColor(Color.GRAY);
        setContentView(canvas);
        BotAmounts.put(Color.BLACK, 1);

    }

    // Save the state of the grid in the Bundle (similar to HashMap)
    @Override
    protected void onSaveInstanceState (Bundle state) {
        saveState = canvas.getSavedState();
        for (int i = 0; i < saveState.length; i++) {
            state.putInt("" + i, saveState[i]);
        }
        state.putInt("colour", canvas.newColour);
    }

    // Get the saved state of the grid from the Bundle
    protected void onRestoreInstanceState (Bundle state) {
        restoreState = new int[state.size()];
        for (int i = 0; i < state.size(); i++) {
            restoreState[i] = state.getInt("" + i);
        }
        canvas.giveRestoreState(restoreState);
        canvas.newColour = state.getInt("colour");

    }
}
