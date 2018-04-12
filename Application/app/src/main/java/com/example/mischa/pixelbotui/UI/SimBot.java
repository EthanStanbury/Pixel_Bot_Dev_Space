package com.example.mischa.pixelbotui.UI;

import android.graphics.Point;

/**
 * Created by User on 12/04/2018.
 */

public class SimBot {

    Pixel pixel;
    String ID;
    Point defaultPoint = new Point(0,0);

    public SimBot(int color, String ID) {
        this.pixel = new Pixel(color, defaultPoint);
        this.ID = ID;
    }
}
