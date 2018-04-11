package com.example.mischa.pixelbotui.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

import com.example.mischa.pixelbotui.Intergration.SwarmAdapter;
import com.example.mischa.pixelbotui.R;
import com.example.mischa.pixelbotui.Swarm.Swarm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Mischa on 10/03/2018.
 */

public class PBCanvas extends View {

    Paint paint;
    public static Pixel[] uiGrid;  //the grid of squares
    public static int xDimension = 16; // horizontal axis
    public static int yDimension = 48; // vertical axis
    int excessSpace;
    int noOfSquares = yDimension * xDimension;
    int squareWidth;
    int newColour = Color.TRANSPARENT;

    LayoutItem top;  //grey bar #1
    LayoutItem bottom; //grey bar #2
    LayoutItem rRed; // #EE4266
    LayoutItem rYellow; // #FFD23F
    LayoutItem rGreen; // #0EAD69
    LayoutItem rBlue; //#3BCEAC
    LayoutItem rPurple; // #540D6E
    LayoutItem erase; // eraser, transparent
    LayoutItem rColourPicked; // Current colour
    int[] saveState = new int[noOfSquares];
    Drawable eraser = getResources().getDrawable(R.drawable.eraserpic);

    ArrayList<LayoutItem> LayoutItemList = new ArrayList<>();
    ArrayList<LayoutItem> ClickableItems = new ArrayList<>();
    public static HashMap<Integer, Integer> BotAmounts = new HashMap<>();

    public PBCanvas(Context context) {
        super(context);
        paint = new Paint();
        uiGrid = new Pixel[noOfSquares];

        BotAmounts.put(Color.BLACK, 1);
        // This needs to be called after all the bots are added to intialize the swarm
        SwarmAdapter.SwarmCreate(BotAmounts);



        for (int i = 0; i < noOfSquares; i++) {
            uiGrid[i] = new Pixel(Color.TRANSPARENT, new Point(0,0));
        }
        top = new LayoutItem(Color.DKGRAY);
        bottom = new LayoutItem(Color.DKGRAY);
        erase = new LayoutItem(Color.LTGRAY);
        rRed = new LayoutItem(Color.parseColor("#EE4266"));
        rYellow = new LayoutItem(Color.parseColor("#FFD23F"));
        rGreen = new LayoutItem(Color.parseColor("#0EAD69"));
        rBlue = new LayoutItem(Color.parseColor("#3BCEAC"));
        rPurple = new LayoutItem(Color.parseColor("#540D6E"));
        rColourPicked = new LayoutItem(Color.WHITE);
        rColourPicked.rect.set(0,0,0,0);

        LayoutItemList.add(top);
        LayoutItemList.add(bottom);
        LayoutItemList.add(erase);
        LayoutItemList.add(rRed);
        LayoutItemList.add(rYellow);
        LayoutItemList.add(rGreen);
        LayoutItemList.add(rBlue);
        LayoutItemList.add(rPurple);
        LayoutItemList.add(rColourPicked);

        ClickableItems.add(erase);
        ClickableItems.add(rRed);
        ClickableItems.add(rYellow);
        ClickableItems.add(rGreen);
        ClickableItems.add(rBlue);
        ClickableItems.add(rPurple);

    }

    public boolean isIn(Pixel p, ArrayList<Pixel> list) {
        for (int i = 0; i < list.size(); i++) {
            if (uiGrid[i].equals(p)) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        for (int i = 0; i < uiGrid.length; i++) {
            uiGrid[i].colour = Color.TRANSPARENT;
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
            top.rect.set(0,0,200,canvas.getHeight());
            bottom.rect.set(canvas.getWidth() - 200,0,canvas.getWidth(),canvas.getHeight());
            rRed.rect.set(40, (canvas.getHeight()/7) - 60, 160, (canvas.getHeight()/7) + 60);
            rYellow.rect.set(40, 2 * (canvas.getHeight()/7) - 60, 160, 2 * (canvas.getHeight()/7) + 60);
            rGreen.rect.set(40, 3 * (canvas.getHeight()/7) - 60, 160, 3 * (canvas.getHeight()/7) + 60);
            rBlue.rect.set(40, 4 * (canvas.getHeight()/7) - 60, 160, 4 * (canvas.getHeight()/7) + 60);
            rPurple.rect.set(40, 5 * (canvas.getHeight()/7) - 60, 160, 5 * (canvas.getHeight()/7) + 60);
            erase.rect.set(40, 6 * (canvas.getHeight()/7) - 60, 160, 6 * (canvas.getHeight()/7) + 60);
            eraser.setBounds(erase.rect);

            // Setting all the pixels' bounds, as well as the width of them
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
        }
        /** This is for if the screen is portrait */
        else {
            // Set all the positions of the rectangles on the screen
            top.rect.set(0,0,canvas.getWidth(),200);
            bottom.rect.set(0,canvas.getHeight() - 200,canvas.getWidth(),canvas.getHeight());
            rRed.rect.set(canvas.getWidth()/7 - 60, 40, canvas.getWidth()/7 + 60, 160);
            rYellow.rect.set( 2 * (canvas.getWidth()/7) - 60, 40, 2 * (canvas.getWidth()/7) + 60, 160);
            rGreen.rect.set(3 * (canvas.getWidth()/7) - 60, 40, 3 * (canvas.getWidth()/7) + 60, 160);
            rBlue.rect.set(4 * (canvas.getWidth()/7) - 60, 40, 4 * (canvas.getWidth()/7) + 60, 160);
            rPurple.rect.set(5 * (canvas.getWidth()/7) - 60, 40, 5 * (canvas.getWidth()/7) + 60, 160);
            erase.rect.set(6 * (canvas.getWidth()/7) - 60, 40, 6 * (canvas.getWidth()/7) + 60, 160);
            eraser.setBounds(erase.rect);

            // Setting all the pixels' bounds, as well as the width of them
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

        // Draw the top and bottom rectangles, as well as the colour selected rectangle
        eraser.draw(canvas);
        for (int i = 0; i < LayoutItemList.size(); i++) {
            paint.setColor(LayoutItemList.get(i).colour);
            canvas.drawRect(LayoutItemList.get(i).rect, paint);
        }

        // Drawing all the Pixels
        for (int i = 0; i < uiGrid.length; i++) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(uiGrid[i].colour);
            canvas.drawRect(uiGrid[i].rect, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            canvas.drawRect(uiGrid[i].rect, paint);

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int xTouch = (int) e.getX();
        int yTouch = (int) e.getY();

        switch (e.getAction()) {
            // For a single press
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < ClickableItems.size(); i++) {
                    //Change the paint colour, set eraser, etc
                    if (ClickableItems.get(i).rect.contains(xTouch, yTouch)) {
                        newColour = ClickableItems.get(i).colour;
                        rColourPicked.rect.set(ClickableItems.get(i).rect.left - 8, ClickableItems.get(i).rect.top - 8, ClickableItems.get(i).rect.right + 8, ClickableItems.get(i).rect.bottom + 8);
                    }
                }
                // Colour the pressed rectangle
                for (int i = 0; i < noOfSquares; i++) {
                    if (uiGrid[i].rect.contains(xTouch,yTouch)) {
                        uiGrid[i].colour = newColour;
                    }
                }
                break;
            // For a swipe
            case MotionEvent.ACTION_MOVE:
                // Colour the rectangles it passes through
                for (int i = 0; i < noOfSquares; i++) {
                    if (uiGrid[i].rect.contains(xTouch,yTouch)) {
                        uiGrid[i].colour = newColour;
                    }
                }
                break;
        }
        postInvalidate(); //Redraw
        return true;
    }

    public int[] getSavedState() {
        for (int i = 0; i < uiGrid.length; i++) {
            saveState[i] = uiGrid[i].colour;
        }
        return saveState;
    }

    public void giveRestoreState(int[] state) {
        for (int i = 0; i < uiGrid.length; i++) {
            uiGrid[i].colour = state[i];
        }
        postInvalidate();
    }
    /** Called when the user touches the button */
    public void reset(View view) {
        // Do something in response to button click
        clear();
    }
}