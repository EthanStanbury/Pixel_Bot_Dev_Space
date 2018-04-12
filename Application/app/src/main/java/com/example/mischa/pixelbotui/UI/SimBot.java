package com.example.mischa.pixelbotui.UI;

import android.graphics.Point;

/**
 * Created by User on 12/04/2018.
 */

public class SimBot {

    Pixel pixel;
    String ID;
    Point defaultPoint = new Point(0,1);
    String path;

    public SimBot(int color, String ID, String path) {
        this.pixel = new Pixel(color, defaultPoint);
        this.ID = ID;
        this.path = path;
    }
}
