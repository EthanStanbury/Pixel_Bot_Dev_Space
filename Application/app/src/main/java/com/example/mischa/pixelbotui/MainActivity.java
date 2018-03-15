package com.example.mischa.pixelbotui;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    PBCanvas canvas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        canvas = new PBCanvas(this);
        canvas.setBackgroundColor(Color.GRAY);
        setContentView(canvas);
    }
}
