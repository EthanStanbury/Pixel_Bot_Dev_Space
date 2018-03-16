package com.example.mischa.pixelbotui.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import java.util.jar.Attributes;

/**
 * Created by Mischa on 11/03/2018.
 */

public class Pixel {

    int colour;
    Rect rect;

    public Pixel (int colour) {
        this.colour = colour;
        this.rect = new Rect();
    }
    // This is the testing things that I might need later, they are put in onDraw.
    /*        if (canvas.getWidth() > canvas.getHeight()) monitor.colour = Color.RED;
        if (canvas.getWidth() < canvas.getHeight()) monitor.colour = Color.GREEN;
        paint.setColor(monitor.colour);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        monitor.rect.set(0, canvas.getHeight()/2,50,canvas.getHeight()/2 + 50);
        canvas.drawRect(monitor.rect, paint);
        paint.setTextSize(100);
        Float scale = (float) canvas.getWidth()/canvas.getHeight();
        int height = canvas.getHeight();
        int width = canvas.getWidth();
        canvas.drawText("" + scale + ", " + height + ", " + width + ", " + squareWidth, 200, 200, paint);*/

}
