package com.example.mischa.pixelbotui.UI;

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

import java.util.ArrayList;
import java.util.HashMap;

import com.example.mischa.pixelbotui.Intergration.UIAdapter;
import com.example.mischa.pixelbotui.Swarm.Direction;
import com.example.mischa.pixelbotui.Swarm.PathFinder;
import java.util.List;

import static com.example.mischa.pixelbotui.UI.PBCanvas.uiGrid;
import static com.example.mischa.pixelbotui.UI.PBCanvas.xDimension;
import static com.example.mischa.pixelbotui.UI.PBCanvas.yDimension;
import static java.lang.Thread.sleep;

/**
 * Created by User on 11/04/2018.
 */

public class Simulation extends SurfaceView implements SurfaceHolder.Callback {
    //HashMap<String, List<Direction>> Solution = PathFinder.getSolutions(UIAdapter.destinationGrid);

    Paint paint = new Paint();
    int noOfRed;
    int noOfYellow;
    int noOfGreen;
    int noOfBlue;
    int noOfPurple;
    int noOfBlack;
    int totalBots;
    int squareWidth;
    int excessSpace;
    HashMap<String, String> botMoves = new HashMap<>();
    ArrayList<SimBot> unfinishedBots = new ArrayList<>();
    ArrayList<SimBot> finishedBots = new ArrayList<>();

    MainThread thread;


    public Simulation(Context context) {
        super(context);

        getHolder().addCallback(this);
//        for (String key:  Solution.keySet()) {
//            System.out.println((key));
//            botMoves.put(key, Solution.get(key).toString());
//            for (Direction d: Solution.get(key)) {
//
//                System.out.println(d);
//
//            }
//
//
//        }

        //botMoves.put("-11268754/1", "D"); //purple
        //botMoves.put("-12857684/1", "DD"); //blue
        //botMoves.put("-15815319/1", "DDD"); //green
        //botMoves.put("-11713/1", "DDDD"); //yellow
        botMoves.put("-1162650/0", "RRR"); //red
        botMoves.put("-1162650/1", "RRRR"); //red
        botMoves.put("-1162650/2", "RRRRR"); //red
        botMoves.put("-1162650/3", "DRRRRR"); //red
        botMoves.put("-1162650/4", "DRRRRRR"); //red
        botMoves.put("-1162650/5", "DRDRRRRR"); //red
        botMoves.put("-1162650/6", "DRDRDRRRR"); //red
        botMoves.put("-1162650/7", "DRDRDRRR"); //red
        botMoves.put("-1162650/8", "DRDRDRDRR"); //red
        botMoves.put("-1162650/9", "DRDRDRDR"); //red
        botMoves.put("-1162650/10", "DRDRDRD"); //red
        botMoves.put("-1162650/11", "DRDRDR"); //red
        botMoves.put("-1162650/12", "DRDRD"); //red
        botMoves.put("-1162650/13", "DDRR"); //red
        botMoves.put("-1162650/14", "DRR"); //red
        botMoves.put("-1162650/15", "DRRR"); //red
        botMoves.put("-11713/0", "DRRRR"); //yellow
        botMoves.put("-11713/1", "DDRRR"); //yellow
        botMoves.put("-11713/2", "DDRRRR"); //yellow
        botMoves.put("-11713/3", "DRDRRRR"); //yellow
        botMoves.put("-11713/4", "DRDRDRR"); //yellow
        botMoves.put("-15815319/0", "DDDDRDRRR"); //green
        botMoves.put("-15815319/1", "DDDDDRRR"); //green
        botMoves.put("-15815319/2", "DDDDDRRRDDR"); //green
        botMoves.put("-15815319/3", "DDDDDRRRDR"); //green

        createBots(botMoves);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setStyle(Paint.Style.FILL);
        for (SimBot bot : unfinishedBots) {
            paint.setColor(bot.pixel.colour);
            canvas.drawRect(pointToRect(bot.pixel.location), paint);
        }
        for (SimBot bot : finishedBots) {
            paint.setColor(bot.pixel.colour);
            canvas.drawRect(pointToRect(bot.pixel.location), paint);
        }
        if ((float) canvas.getWidth()/canvas.getHeight() > (float) xDimension/ yDimension) {
            squareWidth = (canvas.getHeight() - 200)/yDimension;
            excessSpace = canvas.getWidth() - (xDimension * squareWidth);
            for (int i = 0; i < yDimension; i++) {
                for (int j = 0; j < xDimension; j++) {
                    uiGrid[i * xDimension + j].rect.set((excessSpace / 2) + (j * squareWidth),
                            100 + i * squareWidth,
                            (excessSpace / 2) + (j * squareWidth) + squareWidth,
                            100 + (i * squareWidth) + squareWidth);
                    uiGrid[i * xDimension + j].location.set(j,i);
                }
            }
        } else {
            squareWidth = (canvas.getWidth() - 200)/xDimension;
            excessSpace = canvas.getHeight() - (yDimension * squareWidth);
            for (int i = 0; i < yDimension; i++) {
                for (int j = 0; j < xDimension; j++) {
                    uiGrid[i * xDimension + j].rect.set(100 + j * squareWidth,
                            (excessSpace / 2) + (i * squareWidth),
                            100 + (j * squareWidth) + squareWidth,
                            (excessSpace / 2) + (i * squareWidth) + squareWidth);
                    uiGrid[i * xDimension + j].location.set(j,i);
                }
            }

        }
        for (Pixel p : uiGrid) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.TRANSPARENT);
            canvas.drawRect(p.rect, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            canvas.drawRect(p.rect, paint);
        }
        //}

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

    public void getNumbersOfColours() { //unused, possibly delete?
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
    }

    public void createBots(HashMap<String, String> moves) {
        int botColour = Color.MAGENTA;
        for (String key : moves.keySet()) {
            botColour = Integer.parseInt(parseColour(key));
            SimBot newBot = new SimBot(botColour, key, moves.get(key));
            unfinishedBots.add(newBot);
        }
    }

    public void nextMoves() {
        for (int i = 0; i < unfinishedBots.size(); i++) {
            System.out.println(unfinishedBots.get(i).path);
            unfinishedBots.get(i).pixel.location = newPos(unfinishedBots.get(i).pixel.location, unfinishedBots.get(i).path.charAt(0));
            System.out.println("I am bot " + unfinishedBots.get(i).ID + " and I am at " + unfinishedBots.get(i).pixel.location);

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

    public void run() {
        nextMoves();
        postInvalidate();
        try {
            System.out.println("wait");
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
