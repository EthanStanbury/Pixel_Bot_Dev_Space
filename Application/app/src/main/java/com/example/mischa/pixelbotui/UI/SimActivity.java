package com.example.mischa.pixelbotui.UI;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by User on 11/04/2018.
 */

public class SimActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(new Simulation(this));
    }
}
