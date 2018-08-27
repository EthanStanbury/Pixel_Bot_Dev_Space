package com.example.mischa.pixelbotui.UI;

import android.app.Activity;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.mischa.pixelbotui.R;
import com.example.mischa.pixelbotui.Swarm.Solution;

import java.util.HashMap;

public class SimActivity extends Activity {

    Simulation simCanvas;
    ConstraintLayout constraintLayout;
    Button backButton;
    public  static HashMap<String, Solution> Solution  = MainActivity.Solution;


    // Called when activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = getLayoutInflater();
        constraintLayout = (ConstraintLayout) inflater.inflate(R.layout.activity_sim, null);

        if (simCanvas == null) {
            simCanvas = new Simulation(this);
        }
        simCanvas.setBackgroundColor(Color.WHITE);
        constraintLayout.addView(simCanvas);

        backButton = constraintLayout.findViewById(R.id.drawagain);
        backButton.bringToFront();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simCanvas.activity.finish();
                simCanvas.runThread = false;
            }
        });

        setContentView(constraintLayout);
    }

    // Override for the back button, stops the thread running as well as finishing the activity
    @Override
    public void onBackPressed() {
        simCanvas.runThread = false;
        finish();
    }
}
