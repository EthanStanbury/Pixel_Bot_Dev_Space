package com.example.mischa.pixelbotui.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.mischa.pixelbotui.Intergration.SwarmAdapter;
import com.example.mischa.pixelbotui.Intergration.UIAdapter;
import com.example.mischa.pixelbotui.R;
import com.example.mischa.pixelbotui.Swarm.Bot;
import com.example.mischa.pixelbotui.Swarm.Direction;
import com.example.mischa.pixelbotui.Swarm.Grid;
import com.example.mischa.pixelbotui.Swarm.PathFinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mischa on 10/03/2018.
 */

public class PBCanvas extends SurfaceView {

    Paint paint;
    public static Pixel[] uiGrid;  //the grid of squares

    public static int xDimension = 25; // horizontal axis
    public static int yDimension = 16; // vertical axis
    int xDimWOBorder = xDimension - 2;
    int yDimWOBorder = yDimension - 2;

    int excessSpace;
    int noOfSquares = yDimension * xDimension;
    int squareWidth;
    int selectedColour = Color.TRANSPARENT;
    int whiteBox = 1;
    int botsTotal = 322;
    int currentBotAmount;

    Context context;

    LayoutItem top;  //grey bar #1
    LayoutItem bottom; //grey bar #2
    LayoutItem rRed; // #EE4266
    LayoutItem rYellow; // #FFD23F
    LayoutItem rGreen; // #0EAD69
    LayoutItem rBlue; //#3BCEAC
    LayoutItem rPurple; // #540D6E
    LayoutItem rErase; // eraser, transparent
    LayoutItem rColourPicked; // Current colour
    LayoutItem eraseBG;
    LayoutItem rSubmit;
    LayoutItem rClear;
    LayoutItem gridBG;
    LayoutItem rBT;
    int[] saveState = new int[noOfSquares];
    Drawable eraser = getResources().getDrawable(R.drawable.eraserpic);

    ArrayList<LayoutItem> LayoutItemList = new ArrayList<>();
    ArrayList<LayoutItem> ClickableItems = new ArrayList<>();

    public static ArrayList<Pixel> border = new ArrayList<>();

    public PBCanvas(Context context) {
        super(context);
        this.context = context;
        paint = new Paint();
        uiGrid = new Pixel[noOfSquares];



        if (xDimension < 10 || yDimension < 10){
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setMessage("Grid dimension below 10. Please restart the app and select grid dimension over 10. ");
            alert.show();
        }

        for (int i = 0; i < noOfSquares; i++) {
            uiGrid[i] = new Pixel(Color.TRANSPARENT, new Point(0,0));
        }
        top = new LayoutItem(Color.DKGRAY);
        bottom = new LayoutItem(Color.DKGRAY);
        rErase = new LayoutItem(Color.TRANSPARENT);
        rRed = new LayoutItem(Color.parseColor("#EE4266")); // -1162650
        rYellow = new LayoutItem(Color.parseColor("#FFD23F")); // -11713
        rGreen = new LayoutItem(Color.parseColor("#0EAD69")); // -15815319
        rBlue = new LayoutItem(Color.parseColor("#3BCEAC")); // -12857684
        rPurple = new LayoutItem(Color.parseColor("#540D6E")); // -11268754
        rColourPicked = new LayoutItem(Color.WHITE);
        rColourPicked.rect.set(-10,-10,-9,-9);
        eraseBG = new LayoutItem(Color.LTGRAY);
        rSubmit = new LayoutItem(Color.LTGRAY);
        rClear = new LayoutItem(Color.LTGRAY);
        rBT = new LayoutItem(Color.LTGRAY);
        gridBG = new LayoutItem(Color.LTGRAY);

        LayoutItemList.add(top);
        LayoutItemList.add(bottom);
        LayoutItemList.add(rColourPicked);
        LayoutItemList.add(rRed);
        LayoutItemList.add(rYellow);
        LayoutItemList.add(rGreen);
        LayoutItemList.add(rBlue);
        LayoutItemList.add(rPurple);
        LayoutItemList.add(rErase);
        LayoutItemList.add(eraseBG);
        LayoutItemList.add(rSubmit);
        LayoutItemList.add(rClear);
        LayoutItemList.add(gridBG);
        LayoutItemList.add(rBT);

        ClickableItems.add(rErase);
        ClickableItems.add(rRed);
        ClickableItems.add(rYellow);
        ClickableItems.add(rGreen);
        ClickableItems.add(rBlue);
        ClickableItems.add(rPurple);
    }

    // Clears the grid of any current drawing
    public void clear() {
        for (Pixel p : uiGrid) {
            p.colour = Color.TRANSPARENT;
        }
        postInvalidate();
    }

    // This counts how many of each colour are on the grid at the current time
    public void updateBotAmount() {
        int count = 0;
        for (Pixel p : uiGrid) {
            if (p.colour != Color.TRANSPARENT) {
                count++;
            }
        }
        currentBotAmount = botsTotal - count;
    }

    // Test if a pixel is in a list
    public static boolean isIn(Pixel p, ArrayList list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(p)) {
                return true;
            }
        }
        return false;
    }

    // Every time the screen is drawn, this is called
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStrokeWidth(5);
        updateBotAmount();

        /* This is for if the screen is landscape, basically */
        if ((float) canvas.getWidth()/canvas.getHeight() > (float) xDimWOBorder/yDimWOBorder) {
            // Set all the positions of the rectangles on the screen
            top.rect.set(0,0,300, canvas.getHeight());
            bottom.rect.set(canvas.getWidth() - 300,0, canvas.getWidth(), canvas.getHeight());
            rRed.rect.set(70, (canvas.getHeight()/7) - 80, 230, (canvas.getHeight()/7) + 80);
            rYellow.rect.set(70, 2 * (canvas.getHeight()/7) - 80, 230, 2 * (canvas.getHeight()/7) + 80);
            rGreen.rect.set(70, 3 * (canvas.getHeight()/7) - 80, 230, 3 * (canvas.getHeight()/7) + 80);
            rBlue.rect.set(70, 4 * (canvas.getHeight()/7) - 80, 230, 4 * (canvas.getHeight()/7) + 80);
            rPurple.rect.set(70, 5 * (canvas.getHeight()/7) - 80, 230, 5 * (canvas.getHeight()/7) + 80);
            rErase.rect.set(70, 6 * (canvas.getHeight()/7) - 80, 230, 6 * (canvas.getHeight()/7) + 80);
            eraser.setBounds(rErase.rect.left + 4, rErase.rect.top - 4, rErase.rect.right - 4, rErase.rect.bottom + 4);
            eraseBG.rect.set(rErase.rect);
            rClear.rect.set(canvas.getWidth() - 240, 40, canvas.getWidth() - 40, 160);
            rSubmit.rect.set(canvas.getWidth() - 240, canvas.getHeight() - 160, canvas.getWidth() - 40, canvas.getHeight() - 40);
            rBT.rect.set(canvas.getWidth() - 240, canvas.getHeight()/3 + 60, canvas.getWidth() - 40, canvas.getHeight()/3 - 60);


            // Setting all the pixels' bounds, as well as the width of them
            squareWidth = (canvas.getHeight() - 200)/yDimWOBorder;
            excessSpace = canvas.getWidth() - (xDimWOBorder * squareWidth);
            for (int i = 1; i < yDimension - 1; i++) {
                for (int j = 1; j < xDimension - 1; j++) {
                    uiGrid[i * xDimension + j].rect.set((excessSpace / 2) + ((j - 1) * squareWidth),
                            100 + (i - 1) * squareWidth,
                            (excessSpace / 2) + ((j - 1) * squareWidth) + squareWidth,
                            100 + ((i - 1) * squareWidth) + squareWidth);
                    uiGrid[i * xDimension + j].location.set(j,i);
                }
            }
            gridBG.rect.set(canvas.getWidth()/2 - (xDimWOBorder * squareWidth/2), canvas.getHeight()/2 - (yDimWOBorder * squareWidth/2), canvas.getWidth()/2 + (xDimWOBorder * squareWidth/2), canvas.getHeight()/2 + (yDimWOBorder * squareWidth/2));
        }
        /* This is for if the screen is portrait */
        else {
            // Set all the positions of the rectangles on the screen
            top.rect.set(0,0,canvas.getWidth(),300);
            bottom.rect.set(0,canvas.getHeight() - 300,canvas.getWidth(),canvas.getHeight());
            rRed.rect.set(canvas.getWidth()/7 - 80, 70, canvas.getWidth()/7 + 80, 230);
            rYellow.rect.set( 2 * (canvas.getWidth()/7) - 80, 70, 2 * (canvas.getWidth()/7) + 80, 230);
            rGreen.rect.set(3 * (canvas.getWidth()/7) - 80, 70, 3 * (canvas.getWidth()/7) + 80, 230);
            rBlue.rect.set(4 * (canvas.getWidth()/7) - 80, 70, 4 * (canvas.getWidth()/7) + 80, 230);
            rPurple.rect.set(5 * (canvas.getWidth()/7) - 80, 70, 5 * (canvas.getWidth()/7) + 80, 230);
            rErase.rect.set(6 * (canvas.getWidth()/7) - 80, 70, 6 * (canvas.getWidth()/7) + 80, 230);
            eraser.setBounds(rErase.rect.left + 4, rErase.rect.top + 4, rErase.rect.right - 4, rErase.rect.bottom - 4);
            eraseBG.rect.set(rErase.rect);
            rClear.rect.set(40, canvas.getHeight() - 160, 240, canvas.getHeight() - 40);
            rSubmit.rect.set(canvas.getWidth() - 240, canvas.getHeight() - 160, canvas.getWidth() - 40, canvas.getHeight() - 40);
            rBT.rect.set(2 * canvas.getWidth() / 3 - 100, canvas.getHeight() - 160, 2 * canvas.getWidth() / 3 + 100, canvas.getHeight() - 40);



            // Setting all the pixels' bounds, as well as the width of them
            squareWidth = (canvas.getWidth() - 200)/xDimWOBorder;
            excessSpace = canvas.getHeight() - (yDimWOBorder * squareWidth);
            for (int i = 1; i < yDimension - 1; i++) {
                for (int j = 1; j < xDimension - 1; j++) {
                    uiGrid[i * xDimension + j].rect.set(100 + (j - 1) * squareWidth,
                            (excessSpace / 2) + ((i - 1) * squareWidth),
                            100 + ((j - 1) * squareWidth) + squareWidth,
                            (excessSpace / 2) + ((i - 1) * squareWidth) + squareWidth);
                    uiGrid[i * xDimension + j].location.set(j,i);
                }
            }
            gridBG.rect.set(canvas.getWidth()/2 - (xDimWOBorder * squareWidth/2), canvas.getHeight()/2 - (yDimWOBorder * squareWidth/2), canvas.getWidth()/2 + (xDimWOBorder * squareWidth/2), canvas.getHeight()/2 + (yDimWOBorder * squareWidth/2));
        }

        rColourPicked.rect.set(ClickableItems.get(whiteBox).rect.left - 10, ClickableItems.get(whiteBox).rect.top - 10, ClickableItems.get(whiteBox).rect.right + 10, ClickableItems.get(whiteBox).rect.bottom + 10);

        // Draw all of the LayoutItems
        for (int i = 0; i < LayoutItemList.size(); i++) {
            paint.setColor(LayoutItemList.get(i).colour);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawRect(LayoutItemList.get(i).rect, paint);
        }
        eraser.draw(canvas);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(50);
        paint.setColor(Color.BLACK);
        canvas.drawText("CLEAR", rClear.rect.exactCenterX(), rClear.rect.exactCenterY() + 20, paint);
        canvas.drawText("SUBMIT", rSubmit.rect.exactCenterX(), rSubmit.rect.exactCenterY() + 20, paint);
        canvas.drawText("BT", rBT.rect.exactCenterX(), rBT.rect.exactCenterY() + 20, paint);

        paint.setColor(Color.WHITE);
        paint.setTextSize(75);
        // If the screen is landscape
        if ((float) canvas.getWidth()/canvas.getHeight() > (float) xDimWOBorder/yDimWOBorder) {
            canvas.drawText(currentBotAmount + "", bottom.rect.exactCenterX(), 4 * bottom.rect.exactCenterY() / 3 - 50, paint);
            canvas.drawText("bots left", bottom.rect.exactCenterX(), 4 * bottom.rect.exactCenterY() / 3 + 50, paint);
        } else { // If the screen is portrait
            canvas.drawText(currentBotAmount + "", 2 * bottom.rect.exactCenterX() / 3, bottom.rect.exactCenterY() - 50, paint);
            canvas.drawText("bots left", 2 * bottom.rect.exactCenterX() / 3, bottom.rect.exactCenterY() + 50, paint);
        }

        // Drawing all the Pixels
        for (Pixel p : uiGrid) {
            paint.setStyle(Paint.Style.FILL);
            if (isIn(p, border)) {
                paint.setColor(Color.LTGRAY);
            } else {
                paint.setColor(p.colour);
            }
            canvas.drawRect(p.rect, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(3);
            canvas.drawRect(p.rect, paint);
        }


    }

    // This is where touch events are handled, swiping, tapping etc
    @Override
    public boolean onTouchEvent(MotionEvent e) {

        int xTouch = (int) e.getX();
        int yTouch = (int) e.getY();

        switch (e.getAction()) {
            // For a single press
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < ClickableItems.size(); i++) {
                    //Change the paint colour
                    if (ClickableItems.get(i).rect.contains(xTouch, yTouch)) {
                        selectedColour = ClickableItems.get(i).colour;
                        rColourPicked.rect.set(ClickableItems.get(i).rect.left - 10, ClickableItems.get(i).rect.top - 10, ClickableItems.get(i).rect.right + 10, ClickableItems.get(i).rect.bottom + 10);
                        whiteBox = i;
                        System.out.println("Change: " + whiteBox);
                    }
                }
                // Colour the pressed rectangle
                if (currentBotAmount > 0 || selectedColour == Color.TRANSPARENT) {
                    for (int i = 0; i < noOfSquares; i++) {
                        if (uiGrid[i].rect.contains(xTouch, yTouch) && !isIn(uiGrid[i], border)) {
                            uiGrid[i].colour = selectedColour;
                        }
                    }
                }
                // if they tap clear or submit or BT
                if (rClear.rect.contains(xTouch, yTouch)) {
                    clear();
                }
                if (rSubmit.rect.contains(xTouch, yTouch)) {
                    UIAdapter.createGridWpixel(uiGrid);
                    SwarmAdapter.SwarmCreate(MainActivity.BotAmounts);

                    //System.out.println(SwarmAdapter.WholeSwarm.size());
//                    for (Integer key: SwarmAdapter.WholeSwarm.keySet()) {
//
//
//                    }

                    // start the new activity
                    Intent intent = new Intent(context, SimActivity.class);
                    context.startActivity(intent);
                }
                if (rBT.rect.contains(xTouch, yTouch)) {
                    //ETHAN PUT YOUR CODE HERE
                    //ATTENTION
                    //IDK HOW TO GET YOUR ATTENTION IN A COMMENT
                    //RAWR XD
                }
                break;
            // For a swipe
            case MotionEvent.ACTION_MOVE:
                // Colour the rectangles it passes through
                if (currentBotAmount > 0 || selectedColour == Color.TRANSPARENT) {
                    for (int i = 0; i < noOfSquares; i++) {
                        if (uiGrid[i].rect.contains(xTouch, yTouch) && !isIn(uiGrid[i], border)) {
                            uiGrid[i].colour = selectedColour;
                        }
                    }
                }
                break;
        }
        updateBotAmount();
        postInvalidate(); //Redraw
        return true;
    }

    // Save the current state of the app
    public int[] getSavedState() {
        for (int i = 0; i < uiGrid.length; i++) {
            saveState[i] = uiGrid[i].colour;
        }
        return saveState;
    }

    // Update the state with what has been saved
    public void giveRestoreState(int[] state) {
        for (int i = 0; i < uiGrid.length; i++) {
            uiGrid[i].colour = state[i];
        }
        System.out.println("RestoreState: " + whiteBox);
        postInvalidate();
    }
}