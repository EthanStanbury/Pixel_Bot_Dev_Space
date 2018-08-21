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
import com.example.mischa.pixelbotui.Swarm.Swarm;

import java.util.List;

import static com.example.mischa.pixelbotui.UI.PBCanvas.uiGrid;
import static java.lang.Thread.sleep;

/**
 * Created by User on 11/04/2018.
 */

public class Simulation extends SurfaceView implements SurfaceHolder.Callback {
    long startTime = System.currentTimeMillis();
    HashMap<String, List<Direction>> Solution = PathFinder.getSolutions(UIAdapter.destinationGrid);
    long endTime = System.currentTimeMillis();

    Paint paint = new Paint();
    HashMap<String, String> botMoves = new HashMap<>();
    ArrayList<SimBot> unfinishedBots = new ArrayList<>();
    ArrayList<SimBot> finishedBots = new ArrayList<>();
    boolean runThread = true;

    LayoutItem backButton;

    MainThread thread;

    Activity activity = (Activity) getContext();

    public Simulation(Context context) {

        super(context);

        getHolder().addCallback(this);

        for (String key:  Solution.keySet()) {
            System.out.println(Solution.get(key).toString());
            botMoves.put(key, Solution.get(key).toString());
        }

        createBots(botMoves);

        backButton = new LayoutItem(Color.LTGRAY);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);

    }

    // Every time the app is drawn
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Draw the grid, and border
        for (Pixel p : uiGrid) {
            paint.setStyle(Paint.Style.FILL);
            if (PBCanvas.isIn(p, PBCanvas.border)) {
                paint.setColor(Color.DKGRAY);
            } else {
                paint.setColor(Color.TRANSPARENT);
            }
            canvas.drawRect(p.rect, paint);
        }

        // Draw the bots
        paint.setStyle(Paint.Style.FILL);
        for (SimBot bot : unfinishedBots) {
            paint.setColor(bot.pixel.colour);
            canvas.drawRect(pointToRect(bot.pixel.location), paint);
        }
        for (SimBot bot : finishedBots) {
            paint.setColor(bot.pixel.colour);
            canvas.drawRect(pointToRect(bot.pixel.location), paint);
        }
        for (Pixel p : uiGrid) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(3);
            canvas.drawRect(p.rect, paint);
        }

        backButton.rect.set(50, 50, 400, 175);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(backButton.colour);
        canvas.drawRect(backButton.rect, paint);
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        canvas.drawText("DRAW AGAIN", backButton.rect.exactCenterX() - 150, backButton.rect.exactCenterY() + 20, paint);
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

    public void createBots(HashMap<String, String> moves) {
        int botColour;
        for (String key : moves.keySet()) {
            Point botLocation;

            botColour = Integer.parseInt(parseColour(key));
            //TODO this needs to be changed to a variable
            for (Integer swarmColour: SwarmAdapter.WholeSwarm.keySet()) {
                if (swarmColour == botColour) {
                        botLocation = SwarmAdapter.WholeSwarm.get(swarmColour).SwarmList.get(key).Location;
                        SimBot newBot = new SimBot(botColour, key, moves.get(key), botLocation);
                        unfinishedBots.add(newBot);
                }



            }


        }
    }

    // Update the positions of the bots according to the next moves in their strings
    public void nextMoves() {
        for (int i = 0; i < unfinishedBots.size(); i++) {
         //   System.out.println(unfinishedBots.get(i).path);
            unfinishedBots.get(i).pixel.location = newPos(unfinishedBots.get(i).pixel.location, unfinishedBots.get(i).path.charAt(0));
          //  System.out.println("I am bot " + unfinishedBots.get(i).ID + " and I am at " + unfinishedBots.get(i).pixel.location);

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
        }
        return point;
    }


    // Handles the touch events
    public boolean onTouchEvent(MotionEvent e) {
        int xTouch = (int) e.getX();
        int yTouch = (int) e.getY();

        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            if (backButton.rect.contains(xTouch,yTouch)) {
                activity.finish();
                runThread = false;
            }
        }
        return true;
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
