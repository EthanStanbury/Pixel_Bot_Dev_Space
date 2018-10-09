package com.example.mischa.pixelbotui.UI;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.time.Clock;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.mischa.pixelbotui.Intergration.SwarmAdapter;
import com.example.mischa.pixelbotui.Intergration.UIAdapter;
import com.example.mischa.pixelbotui.Swarm.Direction;
import com.example.mischa.pixelbotui.Swarm.PathFinder;
import com.example.mischa.pixelbotui.Swarm.Solution;
import com.example.mischa.pixelbotui.Swarm.Swarm;
import com.example.mischa.pixelbotui.Swarm.Solution;

import java.util.List;

import static com.example.mischa.pixelbotui.UI.PBCanvas.uiGrid;
import static java.lang.Thread.sleep;

/**
 * Created by User on 11/04/2018.
 */

public class Simulation extends SurfaceView implements SurfaceHolder.Callback {
    long startTime = System.currentTimeMillis();
    HashMap<String, Solution> solutions;
    long endTime = System.currentTimeMillis();

    Paint paint = new Paint();
    ArrayList<SimBot> unfinishedBots = new ArrayList<>();
    ArrayList<SimBot> finishedBots = new ArrayList<>();
    boolean runThread = true;

    MainThread thread;

    Activity activity = (Activity) getContext();

    public Simulation(Context context) { //, HashMap<String, Solution> Solution) {

        super(context);
        solutions = MainActivity.Solution;
        getHolder().addCallback(this);

<<<<<<< HEAD
        for (String key:  Solution.keySet()) {
            System.out.println(Solution.get(key).toString());
            botMoves.put(key, Solution.get(key).toString());
        }

        createBots(botMoves);
=======
//        for (String key:  Solution.keySet()) {
//            botMoves.put(key, Solution.get(key).toString());
//        }
        System.out.println("Solutions size: " + solutions.size());
        createBots(solutions);
>>>>>>> Bugfixn'

        thread = new MainThread(getHolder(), this);
        setFocusable(true);

    }

    // Every time the app is drawn
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.TRANSPARENT);
        //Draw the grid, and border
        for (Pixel p : uiGrid) {
            canvas.drawRect(p.rect, paint);
        }

        // Draw the bots
        paint.setStyle(Paint.Style.FILL);
        for (SimBot bot : unfinishedBots) {
            if (bot.pixel.colour == Color.TRANSPARENT) {
                paint.setColor(Color.MAGENTA);
            } else { paint.setColor(bot.pixel.colour); }
            canvas.drawRect(pointToRect(bot.pixel.location), paint);
            System.out.println("Unfinished: " + bot.ID + " and " + bot.pixel.colour);
        }
        for (SimBot bot : finishedBots) {
            if (bot.pixel.colour == Color.TRANSPARENT) {
                paint.setColor(Color.MAGENTA);
            } else { paint.setColor(bot.pixel.colour); }
            canvas.drawRect(pointToRect(bot.pixel.location), paint);
            System.out.println("Bot and colour: " + bot.ID + " and " + bot.pixel.colour);
        }
        for (Pixel p : uiGrid) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(3);
            canvas.drawRect(p.rect, paint);
        }
    }

    // Get the colour from an input string, up until '/'
    public String parseColour(String input) {
        String output = "";
        int i = 0;
        while (input.charAt(i) != '/' && i < input.length() - 1) {
            output = output + input.charAt(i);
            i++;
        }
        return output;
    }

    public String convertToString(List<Direction> list) {
        String output = "";
        for (Direction d : list) {
            output = output + d;
        }
        System.out.println("Full Path: " + output);
        return output;
    }

    public void createBots(HashMap<String, Solution> sols) {
        for (String key : sols.keySet()) {
            Point botLocation;
            botLocation = Swarm.currentSwarm.get(key).Location;
            System.out.println("Starting point: " + botLocation);
            SimBot newBot = new SimBot(sols.get(key).Colour, key, convertToString(sols.get(key).Moves), botLocation);
            unfinishedBots.add(newBot);




        }
        System.out.println("Createbots size: " + unfinishedBots.size());
    }
    // Update the positions of the bots according to the next moves in their strings
    public void nextMoves() {
        for (int i = 0; i < unfinishedBots.size(); i++) {
            unfinishedBots.get(i).pixel.location = newPos(unfinishedBots.get(i).pixel.location, unfinishedBots.get(i).path.charAt(0));
            System.out.println("I am bot " + unfinishedBots.get(i).ID + " and I am at " + unfinishedBots.get(i).pixel.location);
            System.out.println("I am bot " + unfinishedBots.get(i).ID + " and my path is " + unfinishedBots.get(i).path);

            if (unfinishedBots.get(i).path.length() == 1) {
                finishedBots.add(unfinishedBots.get(i));
                unfinishedBots.remove(unfinishedBots.get(i));
            } else {
                unfinishedBots.get(i).path = unfinishedBots.get(i).path.substring(1);
            }
        }

    }

    // Convert a point (x,y) to the rectangle there.
    public Rect pointToRect(Point point) {
        Rect rec = new Rect();
        for (Pixel p : uiGrid) {
            if (p.location.equals(point)) {
                return p.rect;
            }
        }
        return rec;
    }

    public Point newPos(Point point, Character dir) {
        switch (dir) {
            case 'L': {
                point.x += -1;
                break;
            }
            case 'U': {
                point.y += -1;
                break;
            }
            case 'R': {
                point.x += 1;
                break;
            }
            case 'D': {
                point.y += 1;
                break;
            }
<<<<<<< HEAD
=======
            case 'S': {
                break;
            }
>>>>>>> Bugfixn'
        }
        return point;
    }

    // The method that is called over and over to move the bots
    public void run() {
        nextMoves();
        postInvalidate();
        try {
            sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        invalidate();
    }

    // Start the thread
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                thread.running = true;
                thread.start();
            }
        }, 1000);
    }

    // End the thread
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.running = false;
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }

    }
}
