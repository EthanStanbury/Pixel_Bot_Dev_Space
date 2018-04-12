package com.example.mischa.pixelbotui.UI;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import static com.example.mischa.pixelbotui.UI.PBCanvas.uiGrid;

/**
 * Created by User on 11/04/2018.
 */

public class Simulation extends View {

    Paint paint = new Paint();

    public Simulation(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Pixel p : uiGrid) {
            paint.setStyle(Paint.Style.FILL);
            //paint.setColor(p.colour);
            paint.setColor(Color.CYAN);
            canvas.drawRect(p.rect, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            canvas.drawRect(p.rect, paint);
        }
    }
}
