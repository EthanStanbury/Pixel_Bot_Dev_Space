package com.example.mischa.pixelbotui.UI;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SimActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Simulation simCanvas = new Simulation(this);
        simCanvas.setBackgroundColor(Color.GRAY);
        setContentView(simCanvas);
    }
}
