package com.example.mischa.pixelbotui.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
<<<<<<< HEAD
import android.graphics.Rect;
import android.view.View;

import java.sql.Time;
import java.time.Clock;
import java.util.ArrayList;
import java.util.HashMap;
=======
import android.view.View;

import com.example.mischa.pixelbotui.Intergration.SwarmAdapter;
import com.example.mischa.pixelbotui.Intergration.UIAdapter;
import com.example.mischa.pixelbotui.Swarm.Direction;
import com.example.mischa.pixelbotui.Swarm.Grid;
import com.example.mischa.pixelbotui.Swarm.PathFinder;
import com.example.mischa.pixelbotui.Swarm.Swarm;

import java.util.HashMap;
import java.util.List;
>>>>>>> 690112f0c89ef60d9030002e33202d274bb32129

import static com.example.mischa.pixelbotui.UI.PBCanvas.uiGrid;

/**
 * Created by User on 11/04/2018.
 */

public class Simulation extends View {



    Paint paint = new Paint();
    int noOfRed;
    int noOfYellow;
    int noOfGreen;
    int noOfBlue;
    int noOfPurple;
    int noOfBlack;
    int totalBots;
    HashMap<String, String> botMoves = new HashMap<>();
    ArrayList<SimBot> unfinishedBots = new ArrayList<>();
    ArrayList<SimBot> finishedBots = new ArrayList<>();


    public Simulation(Context context) {
        super(context);

        botMoves.put("black-1", "DDD");
        botMoves.put("black-2", "RDRRRR");

        createBots(botMoves);
        System.out.println(unfinishedBots.get(0).pixel.location);
        System.out.println(unfinishedBots.get(1).pixel.location);
        while (unfinishedBots.size() > 0) {
            run();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //long start = System.currentTimeMillis();
        //if (System.currentTimeMillis() - start > 1000) {
        for (Pixel p : uiGrid) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.TRANSPARENT);
            canvas.drawRect(p.rect, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            canvas.drawRect(p.rect, paint);
        }
<<<<<<< HEAD
        paint.setStyle(Paint.Style.FILL);
        for (SimBot bot : finishedBots) {
            paint.setColor(bot.pixel.colour);
            canvas.drawRect(pointToRect(bot.pixel.location), paint);
        }
        //}

    }

    public String parseColour(String input) {
        String output = "";
        int i = 0;
        while (input.charAt(i) != '-' && i < input.length()) {
            output = output + input.charAt(i);
            i++;
        }
        return output;
    }

    public void getNumbersOfColours() {
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
            switch (parseColour(key)) {
                case "black": {
                    botColour = Color.BLACK;
                    break;
                }
                case "red": {
                    botColour = Color.parseColor("#EE4266");
                    break;
                }
                case "yellow": {
                    botColour = Color.parseColor("#FFD23F");
                    break;
                }
                case "green": {
                    botColour = Color.parseColor("#0EAD69");
                    break;
                }
                case "blue": {
                    botColour = Color.parseColor("#3BCEAC");
                    break;
                }
                case "purple": {
                    botColour = Color.parseColor("#540D6E");
                    break;
                }
                default: break;
            }
            SimBot newBot = new SimBot(botColour, key, moves.get(key));
            unfinishedBots.add(newBot);
        }
    }

    public void nextMoves() {
        for (int i = 0; i < unfinishedBots.size(); i++) {
            System.out.println(unfinishedBots.get(i).path);
            unfinishedBots.get(i).pixel.location = newPos(unfinishedBots.get(i).pixel.location, unfinishedBots.get(i).path.charAt(0));
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
        long origin = System.currentTimeMillis();
        invalidate();
        nextMoves();
        for (int i = 0; i < unfinishedBots.size(); i++) {
            System.out.println(unfinishedBots.get(i).pixel.location);
        }
        System.out.println("In here");
        wait(1000);
=======

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
//        }

>>>>>>> 690112f0c89ef60d9030002e33202d274bb32129
    }
}
