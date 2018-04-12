package com.example.mischa.pixelbotui.UI;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;

import com.example.mischa.pixelbotui.Intergration.SwarmAdapter;
import com.example.mischa.pixelbotui.Intergration.UIAdapter;
import com.example.mischa.pixelbotui.Swarm.Direction;
import com.example.mischa.pixelbotui.Swarm.Grid;
import com.example.mischa.pixelbotui.Swarm.PathFinder;
import com.example.mischa.pixelbotui.Swarm.Swarm;

import java.util.HashMap;
import java.util.List;

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
            paint.setColor(Color.TRANSPARENT);
            canvas.drawRect(p.rect, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            canvas.drawRect(p.rect, paint);
        }

        paint.setTextSize(50);
        paint.setColor(Color.WHITE);
        HashMap<String, List<Direction>> Solution = PathFinder.getSolutions(UIAdapter.destinationGrid);
//        canvas.drawText(, canvas.getWidth()/2, canvas.getHeight() - 300, paint);


        System.out.println(Solution.size());
        System.out.println(Solution.get("-16777216-0"));
//        for (String key: Solution.keySet()) {
//            for (Direction d: Solution.get(key)) {
//                System.out.println(d);
//
//            }
//
//        }

    }
}
