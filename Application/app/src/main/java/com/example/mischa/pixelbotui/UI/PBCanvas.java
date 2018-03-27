package com.example.mischa.pixelbotui.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

import com.example.mischa.pixelbotui.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Mischa on 10/03/2018.
 */

public class PBCanvas extends View {

    Paint paint;
    Pixel[] grid;  //the grid of squares
    int xDimension = 10; // horizontal axis
    int yDimension = 10; // vertical axis
    int excessSpace;
    int noOfSquares = yDimension * xDimension;
    int squareWidth;
    int newColour = Color.TRANSPARENT;

    Rect top;  //grey bar #1
    Rect bottom; //grey bar #2
    Pixel rRed; // #EE4266
    Pixel rYellow; // #FFD23F
    Pixel rGreen; // #0EAD69
    Pixel rBlue; //#3BCEAC
    Pixel rPurple; // #540D6E
    Rect erase; // eraser, transparent
    Pixel rColourPicked; // Current colour
    int[] saveState = new int[noOfSquares];
    int[] restoreState;
    Drawable eraser = getResources().getDrawable(R.drawable.eraserpic);

    Pixel[] colours = new Pixel[6]; // changed from 5 to 6, after adding an eraser
//    ArrayList<Pixel> coloured = new ArrayList<>();

    public PBCanvas(Context context) {
        super(context);
        paint = new Paint();
        grid = new Pixel[noOfSquares];

        for (int i = 0; i < noOfSquares; i++) {
            grid[i] = new Pixel(Color.TRANSPARENT);
        }
        top = new Rect();
        bottom = new Rect();
        erase = new Rect();
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

    /** Every time the screen is drawn, this is called */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStrokeWidth(5);

        /** This is for if the screen is landscape, basically */
        if ((float) canvas.getWidth()/canvas.getHeight() > (float) xDimension/yDimension) {
            // Set all the positions of the rectangles on the screen
            top.set(0,0,200,canvas.getHeight());
            bottom.set(canvas.getWidth() - 200,0,canvas.getWidth(),canvas.getHeight());
            rRed.rect.set(40, (canvas.getHeight()/7) - 60, 160, (canvas.getHeight()/7) + 60);
            rYellow.rect.set(40, 2 * (canvas.getHeight()/7) - 60, 160, 2 * (canvas.getHeight()/7) + 60);
            rGreen.rect.set(40, 3 * (canvas.getHeight()/7) - 60, 160, 3 * (canvas.getHeight()/7) + 60);
            rBlue.rect.set(40, 4 * (canvas.getHeight()/7) - 60, 160, 4 * (canvas.getHeight()/7) + 60);
            rPurple.rect.set(40, 5 * (canvas.getHeight()/7) - 60, 160, 5 * (canvas.getHeight()/7) + 60);
            erase.set(40, 6 * (canvas.getHeight()/7) - 60, 160, 6 * (canvas.getHeight()/7) + 60);
            eraser.setBounds(erase);

            // Setting all the pixels' bounds, as well as the width of them
            squareWidth = (canvas.getHeight() - 200)/yDimension;
            excessSpace = canvas.getWidth() - (xDimension * squareWidth);
            for (int i = 0; i < yDimension; i++) {
                for (int j = 0; j < xDimension; j++) {
                    grid[i * xDimension + j].rect.set((excessSpace / 2) + (j * squareWidth),
                            100 + i * squareWidth,
                            (excessSpace / 2) + (j * squareWidth) + squareWidth,
                            100 + i * squareWidth + squareWidth);
                }
            }
        }
        /** This is for if the screen is portrait */
        else {
            // Set all the positions of the rectangles on the screen
            top.set(0,0,canvas.getWidth(),200);
            bottom.set(0,canvas.getHeight() - 200,canvas.getWidth(),canvas.getHeight());
            rRed.rect.set(canvas.getWidth()/7 - 60, 40, canvas.getWidth()/7 + 60, 160);
            rYellow.rect.set( 2 * (canvas.getWidth()/7) - 60, 40, 2 * (canvas.getWidth()/7) + 60, 160);
            rGreen.rect.set(3 * (canvas.getWidth()/7) - 60, 40, 3 * (canvas.getWidth()/7) + 60, 160);
            rBlue.rect.set(4 * (canvas.getWidth()/7) - 60, 40, 4 * (canvas.getWidth()/7) + 60, 160);
            rPurple.rect.set(5 * (canvas.getWidth()/7) - 60, 40, 5 * (canvas.getWidth()/7) + 60, 160);
            erase.set(6 * (canvas.getWidth()/7) - 60, 40, 6 * (canvas.getWidth()/7) + 60, 160);
            eraser.setBounds(erase);

            // Setting all the pixels' bounds, as well as the width of them
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

        // Draw the top and bottom rectangles, as well as the colour selected rectangle
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.DKGRAY);
        canvas.drawRect(top, paint);
        canvas.drawRect(bottom, paint);
        paint.setColor(rColourPicked.colour);
        canvas.drawRect(rColourPicked.rect, paint);
        eraser.draw(canvas);
        for (int i = 0; i < colours.length - 1; i++) {
            paint.setColor(colours[i].colour);
            canvas.drawRect(colours[i].rect, paint);
        }

        // Drawing all the Pixels
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
            // For a single press
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < colours.length-1; i++) {
                    //Change the paint colour
                    if (colours[i].rect.contains(xTouch, yTouch)) {
                        newColour = colours[i].colour;
                        rColourPicked.rect.set(colours[i].rect.left - 8, colours[i].rect.top - 8, colours[i].rect.right + 8, colours[i].rect.bottom + 8);
                    }
                }
                // Clear
                if (erase.contains(xTouch,yTouch)) {
                    clear();
                    rColourPicked.rect.set(erase.left - 8, erase.top - 8, erase.right + 8, erase.bottom + 8);
                }
                // Colour the pressed rectangle
                for (int i = 0; i < noOfSquares; i++) {
                    if (grid[i].rect.contains(xTouch,yTouch)) {
                        grid[i].colour = newColour;
                    }
                }
                break;
            // For a swipe
            case MotionEvent.ACTION_MOVE:
                // Colour the rectangles it passes through
                for (int i = 0; i < noOfSquares; i++) {
                    if (grid[i].rect.contains(xTouch,yTouch)) {
                        grid[i].colour = newColour;
                    }
                }
                break;
        }
        postInvalidate(); //Redraw
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
    /** Called when the user touches the button */
    public void reset(View view) {
        // Do something in response to button click
        clear();
    }
}