package com.example.mischa.pixelbotui.UI;

import android.graphics.Color;
import android.graphics.Rect;

/**
 * Created by User on 11/04/2018.
 */

public class LayoutItem {

    int colour;
    Rect rect;

    public LayoutItem (int colour) {
        this.colour = colour;
        this.rect = new Rect();
    }
}
