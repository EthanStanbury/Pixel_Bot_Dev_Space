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

    public static int xDimension = 12; // horizontal axis
    public static int yDimension = 12; // vertical axis

    int excessSpace;
    int noOfSquares = yDimension * xDimension;
    int squareWidth;
    int newColour = Color.TRANSPARENT;
    int whiteBox;
    HashMap<Integer, Integer> pixelAmounts = new HashMap<>();

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
    int[] saveState = new int[noOfSquares];
    Drawable eraser = getResources().getDrawable(R.drawable.eraserpic);

    int count = 0;

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
        rColourPicked.rect.set(0,0,0,0);
        eraseBG = new LayoutItem(Color.LTGRAY);
        rSubmit = new LayoutItem(Color.LTGRAY);
        rClear = new LayoutItem(Color.LTGRAY);

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

        ClickableItems.add(rErase);
        ClickableItems.add(rRed);
        ClickableItems.add(rYellow);
        ClickableItems.add(rGreen);
        ClickableItems.add(rBlue);
        ClickableItems.add(rPurple);

        pixelAmounts.put(-1162650,    0); //Red
        pixelAmounts.put(-11713,      0); //Yellow
        pixelAmounts.put(-15815319,   0); //Green
        pixelAmounts.put(-12857684,   0); //Blue
        pixelAmounts.put(-11268754,   0); //Purple

    }

    public void clear() {
        for (Pixel p : uiGrid) {
            p.colour = Color.TRANSPARENT;
        }
        postInvalidate();
    }

    public void updatePixelAmounts() {

        for (int key : pixelAmounts.keySet()) {
            //temp = MainActivity.BotAmounts.get(key);
            count = 0;
            for (Pixel p : uiGrid) {
                if (p.colour == key && !isIn(p, border)) {
                    count++;
                }
            }
            pixelAmounts.put(key, count);
            //pixelAmounts.put(key, ); //TODO
            //MainActivity.BotAmounts.put(key, temp);
        }
    }

    public static boolean isIn(Pixel p, ArrayList list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(p)) {
                return true;
            }
        }
        return false;
    }

    /** Every time the screen is drawn, this is called */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStrokeWidth(5);

        /* This is for if the screen is landscape, basically */
        if ((float) canvas.getWidth()/canvas.getHeight() > (float) xDimension/yDimension) {
            // Set all the positions of the rectangles on the screen
            top.rect.set(0,0,200, canvas.getHeight());
            bottom.rect.set(canvas.getWidth() - 200,0, canvas.getWidth(), canvas.getHeight());
            rRed.rect.set(40, (canvas.getHeight()/7) - 60, 160, (canvas.getHeight()/7) + 60);
            rYellow.rect.set(40, 2 * (canvas.getHeight()/7) - 60, 160, 2 * (canvas.getHeight()/7) + 60);
            rGreen.rect.set(40, 3 * (canvas.getHeight()/7) - 60, 160, 3 * (canvas.getHeight()/7) + 60);
            rBlue.rect.set(40, 4 * (canvas.getHeight()/7) - 60, 160, 4 * (canvas.getHeight()/7) + 60);
            rPurple.rect.set(40, 5 * (canvas.getHeight()/7) - 60, 160, 5 * (canvas.getHeight()/7) + 60);
            rErase.rect.set(40, 6 * (canvas.getHeight()/7) - 60, 160, 6 * (canvas.getHeight()/7) + 60);
            eraser.setBounds(rErase.rect.left + 4, rErase.rect.top - 4, rErase.rect.right - 4, rErase.rect.bottom + 4);
            eraseBG.rect.set(rErase.rect);
            rClear.rect.set(canvas.getWidth() - 240, 40, canvas.getWidth() - 40, 160);
            rSubmit.rect.set(canvas.getWidth() - 240, canvas.getHeight() - 160, canvas.getWidth() - 40, canvas.getHeight() - 40);


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
                    if (i == 0 || j == 0 || i == yDimension - 1 || j == xDimension - 1) {
                        border.add(uiGrid[i * xDimension + j]);
                    }
                }
            }
        }
        /* This is for if the screen is portrait */
        else {
            // Set all the positions of the rectangles on the screen
            top.rect.set(0,0,canvas.getWidth(),200);
            bottom.rect.set(0,canvas.getHeight() - 200,canvas.getWidth(),canvas.getHeight());
            rRed.rect.set(canvas.getWidth()/7 - 60, 40, canvas.getWidth()/7 + 60, 160);
            rYellow.rect.set( 2 * (canvas.getWidth()/7) - 60, 40, 2 * (canvas.getWidth()/7) + 60, 160);
            rGreen.rect.set(3 * (canvas.getWidth()/7) - 60, 40, 3 * (canvas.getWidth()/7) + 60, 160);
            rBlue.rect.set(4 * (canvas.getWidth()/7) - 60, 40, 4 * (canvas.getWidth()/7) + 60, 160);
            rPurple.rect.set(5 * (canvas.getWidth()/7) - 60, 40, 5 * (canvas.getWidth()/7) + 60, 160);
            rErase.rect.set(6 * (canvas.getWidth()/7) - 60, 40, 6 * (canvas.getWidth()/7) + 60, 160);
            eraser.setBounds(rErase.rect.left + 4, rErase.rect.top + 4, rErase.rect.right - 4, rErase.rect.bottom - 4);
            eraseBG.rect.set(rErase.rect);
            rClear.rect.set(40, canvas.getHeight() - 160, 240, canvas.getHeight() - 40);
            rSubmit.rect.set(canvas.getWidth() - 240, canvas.getHeight() - 160, canvas.getWidth() - 40, canvas.getHeight() - 40);



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
                    if (i == 0 || j == 0 || i == yDimension - 1 || j == xDimension - 1) {
                        border.add(uiGrid[i * xDimension + j]);
                    }
                }
            }
        }

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
        paint.setColor(Color.WHITE);
        for (LayoutItem item : LayoutItemList) {
            if (pixelAmounts.containsKey(item.colour)) {
                canvas.drawText("" + pixelAmounts.get(item.colour), item.rect.exactCenterX(), item.rect.exactCenterY() + 10, paint);
            }
        }

        // Drawing all the Pixels
        for (Pixel p : uiGrid) {
            paint.setStyle(Paint.Style.FILL);
            if (isIn(p, border)) {
                paint.setColor(Color.DKGRAY);
            } else {
                paint.setColor(p.colour);
            }
            canvas.drawRect(p.rect, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            canvas.drawRect(p.rect, paint);
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // Daniel's test code, do not remove.
        /*
        paint.setTextSize(50);
        paint.setColor(Color.WHITE);

        Grid testGrid = new Grid(5, 5);
        testGrid.addBot(new Bot("-16777216-0", 0, new Point(1, 0)));
        testGrid.addDestination(new Pixel(0, new Point(2, 3)));

        HashMap<String, List<Direction>> Solution = PathFinder.getSolutions(testGrid);


        System.out.println(Solution.size());
        System.out.println(Solution.keySet().toString());
        System.out.println(Solution.get("-16777216-0"));

        /*
        for (String key: Solution.keySet()) {
            for (Direction d: Solution.get(key)) {
                System.out.println(d);

            }

        }*/

        // End of test code

        int xTouch = (int) e.getX();
        int yTouch = (int) e.getY();

        switch (e.getAction()) {
            // For a single press
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < ClickableItems.size(); i++) {
                    //Change the paint colour
                    if (ClickableItems.get(i).rect.contains(xTouch, yTouch)) {
                        newColour = ClickableItems.get(i).colour;
                        rColourPicked.rect.set(ClickableItems.get(i).rect.left - 8, ClickableItems.get(i).rect.top - 8, ClickableItems.get(i).rect.right + 8, ClickableItems.get(i).rect.bottom + 8);
                        whiteBox = i;
                    }
                }
                // Colour the pressed rectangle
                if (pixelAmounts.keySet().contains(newColour) && pixelAmounts.get(newColour) < 10) { //TODO
                    for (int i = 0; i < noOfSquares; i++) {
                        if (uiGrid[i].rect.contains(xTouch, yTouch) && !isIn(uiGrid[i], border)) {
                            uiGrid[i].colour = newColour;
                        }
                    }
                }
                if (rClear.rect.contains(xTouch, yTouch)) {
                    clear();
                }
                if (rSubmit.rect.contains(xTouch, yTouch)) {
                    UIAdapter.createGridWpixel(uiGrid);
                    SwarmAdapter.SwarmCreate(MainActivity.BotAmounts);

                    System.out.println(SwarmAdapter.WholeSwarm.size());
//                    for (Integer key: SwarmAdapter.WholeSwarm.keySet()) {
//
//
//                    }


                    Intent intent = new Intent(context, SimActivity.class);
                    context.startActivity(intent);
                }
                break;
            // For a swipe
            case MotionEvent.ACTION_MOVE:
                // Colour the rectangles it passes through
                if (pixelAmounts.keySet().contains(newColour) && pixelAmounts.get(newColour) < 10) { //TODO
                    for (int i = 0; i < noOfSquares; i++) {
                        if (uiGrid[i].rect.contains(xTouch, yTouch) && !isIn(uiGrid[i], border)) {
                            uiGrid[i].colour = newColour;
                        }
                    }
                }
                break;
        }
        updatePixelAmounts();
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
        rColourPicked.rect.set(ClickableItems.get(whiteBox).rect.left - 8, ClickableItems.get(whiteBox).rect.top - 8, ClickableItems.get(whiteBox).rect.right + 8, ClickableItems.get(whiteBox).rect.bottom + 8);
        postInvalidate();
    }
    /** Called when the user touches the button */
    public void reset(View view) {
        // Do something in response to button click
        clear();
    }
}