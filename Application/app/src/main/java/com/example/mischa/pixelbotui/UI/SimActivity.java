package com.example.mischa.pixelbotui.UI;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SimActivity extends Activity {

    Simulation simCanvas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (simCanvas == null) {
            simCanvas = new Simulation(this);
        }
        simCanvas.setBackgroundColor(Color.GRAY);
        setContentView(simCanvas);
    }
}
