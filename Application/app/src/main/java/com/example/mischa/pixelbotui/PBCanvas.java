package com.example.mischa.pixelbotui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Mischa on 10/03/2018.
 */

public class PBCanvas extends View {

    Paint paint;
    Pixel[] grid;
    int xDimension = 10;
    int yDimension = 10;
    int excessSpace;
    int noOfSquares = yDimension * xDimension;
    int squareWidth;
    int newColour = Color.TRANSPARENT;
    Rect top;
    Rect bottom;
    Pixel rRed; // #EE4266
    //Rect rOrange;
    Pixel rYellow; // #FFD23F
    Pixel rGreen; // #0EAD69
    Pixel rBlue; //#3BCEAC
    Pixel rPurple; // #540D6E
    //Rect rPink;
    Pixel rColourPicked;
    public Pixel test = new Pixel(Color.TRANSPARENT);
    int[] saveState = new int[noOfSquares];
    int[] restoreState;

    Pixel[] colours = new Pixel[5];
    ArrayList<Pixel> coloured = new ArrayList<>();


    //    int noOfBots = 4;
    Random random = new Random();
    int red, green, blue;
//    Pixel monitor = new Pixel(Color.BLACK);


    public PBCanvas(Context context) {
        super(context);
        paint = new Paint();
        grid = new Pixel[noOfSquares];
        for (int i = 0; i < noOfSquares; i++) {
            grid[i] = new Pixel(Color.TRANSPARENT);
        }
        top = new Rect();
        bottom = new Rect();
        rRed = new Pixel(Color.parseColor("#EE4266"));
        rYellow = new Pixel(Color.parseColor("#FFD23F"));
        rGreen = new Pixel(Color.parseColor("#0EAD69"));
        rBlue = new Pixel(Color.parseColor("#3BCEAC"));
        rPurple = new Pixel(Color.parseColor("#540D6E"));
        rColourPicked = new Pixel(Color.WHITE);
        rColourPicked.rect.set(0,0,0,0);
        colours[0] = rRed;
        colours[1] = rYellow;
        colours[2] = rGreen;
        colours[3] = rBlue;
        colours[4] = rPurple;
    }

    public boolean isIn(Pixel p, ArrayList<Pixel> list) {
        for (int i = 0; i < list.size(); i++) {
            if (grid[i].equals(p)) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        for (int i = 0; i < grid.length; i++) {
            grid[i].colour = Color.TRANSPARENT;
        }
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStrokeWidth(5);

        if ((float) canvas.getWidth()/canvas.getHeight() > (float) xDimension/yDimension) {
            top.set(0,0,200,canvas.getHeight());
            bottom.set(canvas.getWidth() - 200,0,canvas.getWidth(),canvas.getHeight());
            rRed.rect.set(25, (canvas.getHeight()/6) - 75, 175, (canvas.getHeight()/6) + 75);
            rYellow.rect.set(25, 2 * (canvas.getHeight()/6) - 75, 175, 2 * (canvas.getHeight()/6) + 75);
            rGreen.rect.set(25, 3 * (canvas.getHeight()/6) - 75, 175, 3 * (canvas.getHeight()/6) + 75);
            rBlue.rect.set(25, 4 * (canvas.getHeight()/6) - 75, 175, 4 * (canvas.getHeight()/6) + 75);
            rPurple.rect.set(25, 5 * (canvas.getHeight()/6) - 75, 175, 5 * (canvas.getHeight()/6) + 75);
            squareWidth = (canvas.getHeight() - 200)/yDimension;
            excessSpace = canvas.getWidth() - (xDimension * squareWidth);
            //bRed.setX(0);
            //bRed.setY(200);
            //bClear.setX(0);
            //bClear.setY(400);
            for (int i = 0; i < yDimension; i++) {
                for (int j = 0; j < xDimension; j++) {
                    grid[i * xDimension + j].rect.set((excessSpace / 2) + (j * squareWidth),
                            100 + i * squareWidth,
                            (excessSpace / 2) + (j * squareWidth) + squareWidth,
                            100 + i * squareWidth + squareWidth);
                }
            }
        } else {
            top.set(0,0,canvas.getWidth(),200);
            bottom.set(0,canvas.getHeight() - 200,canvas.getWidth(),canvas.getHeight());
            rRed.rect.set(canvas.getWidth()/6 - 75, 25, canvas.getWidth()/6 + 75, 175);
            rYellow.rect.set( 2 * (canvas.getWidth()/6) - 75, 25, 2 * (canvas.getWidth()/6) + 75, 175);
            rGreen.rect.set(3 * (canvas.getWidth()/6) - 75, 25, 3 * (canvas.getWidth()/6) + 75, 175);
            rBlue.rect.set(4 * (canvas.getWidth()/6) - 75, 25, 4 * (canvas.getWidth()/6) + 75, 175);
            rPurple.rect.set(5 * (canvas.getWidth()/6) - 75, 25, 5 * (canvas.getWidth()/6) + 75, 175);
            squareWidth = (canvas.getWidth() - 200)/xDimension;
            excessSpace = canvas.getHeight() - (yDimension * squareWidth);
            for (int i = 0; i < yDimension; i++) {
                for (int j = 0; j < xDimension; j++) {
                    grid[i * xDimension + j].rect.set(100 + j * squareWidth,
                            (excessSpace / 2) + (i * squareWidth),
                            100 + j * squareWidth + squareWidth,
                            (excessSpace / 2) + (i * squareWidth) + squareWidth);
                }
            }
        }
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.DKGRAY);
        canvas.drawRect(top, paint);
        canvas.drawRect(bottom, paint);
        paint.setColor(rColourPicked.colour);
        canvas.drawRect(rColourPicked.rect, paint);
        for (int i = 0; i < colours.length; i++) {
            paint.setColor(colours[i].colour);
            canvas.drawRect(colours[i].rect, paint);
        }

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

        for (int i = 0; i < grid.length; i++) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(grid[i].colour);
            canvas.drawRect(grid[i].rect, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            canvas.drawRect(grid[i].rect, paint);

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int xTouch = (int) e.getX();
        int yTouch = (int) e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < colours.length; i++) {
                    if (colours[i].rect.contains(xTouch, yTouch)) {
                        newColour = colours[i].colour;
                        rColourPicked.rect.set(colours[i].rect.left - 8, colours[i].rect.top - 8, colours[i].rect.right + 8, colours[i].rect.bottom + 8);
                    }
                }
                for (int i = 0; i < noOfSquares; i++) {
                    if (grid[i].rect.contains(xTouch,yTouch)) {
                        grid[i].colour = newColour;
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < noOfSquares; i++) {
                    if (grid[i].rect.contains(xTouch,yTouch)) {
                        grid[i].colour = newColour;
                        /*if (!isIn(grid[i],coloured)) {
                            coloured.add(grid[i]);
                        }*/
                        /*red = random.nextInt();
                        green = random.nextInt();
                        blue = random.nextInt();
                        grid[i].colour = Color.rgb(0, 0, 0);*/
                    }
                }
                break;
        }
        postInvalidate();
        return true;
    }

    public int[] getSavedState() {
        for (int i = 0; i < grid.length; i++) {
            saveState[i] = grid[i].colour;
        }
        return saveState;
    }

    public void giveRestoreState(int[] state) {
        for (int i = 0; i < grid.length; i++) {
            grid[i].colour = state[i];
        }
        postInvalidate();
    }
}