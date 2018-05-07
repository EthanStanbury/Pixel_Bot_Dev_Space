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
//    int noOfRed;
//    int noOfYellow;
//    int noOfGreen;
//    int noOfBlue;
//    int noOfPurple;
//    int noOfBlack;
//    int totalBots;
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
        System.out.println("Solutions for the paths");
        for (String Id : Solution.keySet()) {
            //System.out.println(Solution.get(Id).toString());
        }
        for (String key:  Solution.keySet()) {

           // System.out.println((key));
            botMoves.put(key, Solution.get(key).toString());
            for (Direction d: Solution.get(key)) {

             //   System.out.println(d);

            }


        }
        createBots(botMoves);

        backButton = new LayoutItem(Color.LTGRAY);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Pixel p : uiGrid) {
            paint.setStyle(Paint.Style.FILL);
            if (PBCanvas.isIn(p, PBCanvas.border)) {
                paint.setColor(Color.DKGRAY);
            } else {
                paint.setColor(Color.TRANSPARENT);
            }
            canvas.drawRect(p.rect, paint);
        }
        paint.setStyle(Paint.Style.FILL);
        for (SimBot bot : unfinishedBots) {
            paint.setColor(bot.pixel.colour);
            //System.out.println(bot.pixel.location);
            canvas.drawRect(pointToRect(bot.pixel.location), paint);
        }
        for (SimBot bot : finishedBots) {
            paint.setColor(bot.pixel.colour);
            canvas.drawRect(pointToRect(bot.pixel.location), paint);
        }
        for (Pixel p : uiGrid) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
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

    public String parseColour(String input) {
        String output = "";
        int i = 0;
        while (input.charAt(i) != '/' && i < input.length() - 1) {
            output = output + input.charAt(i);
            i++;
        }
        return output;
    }

/*    public void getNumbersOfColours() {
        for (Pixel p : uiGrid) {
            if (p.colour == Color.parseColor("#EE4266")) {
                noOfRed++;
                break;
            } else if (p.colour == Color.parseColor("#FFD23F")) {
                noOfYellow++;
                break;
            } else if (p.colour == Color.parseColor("#0EAD69")) {
                noOfGreen++;
                break;
            } else if (p.colour == Color.parseColor("#3BCEAC")) {
                noOfBlue++;
                break;
            } else if (p.colour == Color.parseColor("#540D6E")) {
                noOfPurple++;
                break;
            } else if (p.colour == Color.BLACK) {
                noOfBlack++;
                break;
            } else {
                break;
            }
        }
        totalBots = noOfRed + noOfYellow + noOfGreen + noOfBlue + noOfPurple + noOfBlack;
    }*/

    public void createBots(HashMap<String, String> moves) {
        int botColour;
        for (String key : moves.keySet()) {
            //point should never be 0,0 should throw error if it is this.
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



    public boolean onTouchEvent(MotionEvent e) {
        int xTouch = (int) e.getX();
        int yTouch = (int) e.getY();

        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            if (backButton.rect.contains(xTouch,yTouch)) {
                activity.finish();
                runThread = false;
                /*boolean retry = true;
                while (retry) {
                    try {
                        thread.running = false;
                        thread.join();
                    } catch (InterruptedException f) {
                        f.printStackTrace();
                    }
                    retry = false;
                }*/
            }
        }
        return true;
    }

    public void run() {
        //long origin = System.currentTimeMillis();
        nextMoves();
        postInvalidate();
        try {
         //   System.out.println("wait");
            sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        invalidate();
    }

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
/*        System.out.println("In here");
        wait(1000);

        paint.setTextSize(50);
        paint.setColor(Color.WHITE);
        HashMap<String, List<Direction>> Solution = PathFinder.getSolutions(UIAdapter.destinationGrid);
//        canvas.drawText(, canvas.getWidth()/2, canvas.getHeight() - 300, paint);


        System.out.println(Solution.size());
        System.out.println(Solution.keySet().toString());
        System.out.println(Solution.get("-16777216-0"));
//        for (String key: Solution.keySet()) {
//            for (Direction d: Solution.get(key)) {
//                System.out.println(d);
//
//            }
//
//        }*/

    }
}
