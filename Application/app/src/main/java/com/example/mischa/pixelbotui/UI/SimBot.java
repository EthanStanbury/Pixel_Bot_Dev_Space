package com.example.mischa.pixelbotui.UI;

import android.graphics.Point;

/**
 * Created by User on 12/04/2018.
 */

public class SimBot {

    Pixel pixel;
    String ID;
    String path;

    public SimBot(int color, String ID, String path, Point coordinates) {
        this.pixel = new Pixel(color, coordinates);
        this.ID = ID;
        this.path = path;
    }
}
