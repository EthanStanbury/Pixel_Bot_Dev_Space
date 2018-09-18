package com.example.mischa.pixelbotui.UI;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mischa.pixelbotui.Intergration.SwarmAdapter;
import com.example.mischa.pixelbotui.Intergration.UIAdapter;
import com.example.mischa.pixelbotui.R;
import com.example.mischa.pixelbotui.Swarm.Bot;
import com.example.mischa.pixelbotui.Swarm.Direction;
import com.example.mischa.pixelbotui.Swarm.Grid;
import com.example.mischa.pixelbotui.Swarm.PathFinder;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Mischa on 10/03/2018.
 */

public class PBCanvas extends SurfaceView {

     Paint paint;
    public static Pixel[] uiGrid;  //the grid of squares

    public static int xDimension = 23; // horizontal axis
    public static int yDimension = 16; // vertical axis
    int xDimWOBorder = xDimension - 2;
    int yDimWOBorder = yDimension - 2;

    int excessSpace;
    int noOfSquares = yDimension * xDimension;
    int squareWidth;
    int selectedColour;
    int whiteBox = 1;
    public int TotalBots = 7;


    int currentBotAmount;

    Context context;

    LayoutItem top;  //grey bar #1
    LayoutItem bottom; //grey bar #2
    LayoutItem rRed; // #EE4266
    LayoutItem rYellow; // #FFD23F
    LayoutItem rGreen; // #0EAD69
    LayoutItem rBlue; //#3BCEAC
    LayoutItem rCyan; // #540D6E Old correct hex values
    LayoutItem rWhite;
    LayoutItem rPink;
    LayoutItem rErase; // eraser, transparent
    LayoutItem rColourPicked; // Current colour
    LayoutItem eraseBG;
    LayoutItem gridBG;
    int[] saveState = new int[noOfSquares];
    Drawable eraser = getResources().getDrawable(R.drawable.eraserpic);

    ArrayList<LayoutItem> LayoutItemList = new ArrayList<>();
    ArrayList<LayoutItem> ClickableItems = new ArrayList<>();

    public static ArrayList<Pixel> border = new ArrayList<>();

    public PBCanvas(Context context) {  //,  int botsTotal) {
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
        rWhite = new LayoutItem(Color.rgb(255,255,255)); // -1162650 OR #EE4266
        rYellow = new LayoutItem(Color.rgb(255,255,0)); // -11713 ) OR #FFD23F
        rPink = new LayoutItem(Color.rgb(255,0,255)); // -15815319 OR #0EAD69
        rRed = new LayoutItem(Color.rgb(255,0,0)); // -12857684 OR #3BCEAC
        rCyan = new LayoutItem(Color.rgb(0,255,255)); // -11268754 #540D6E
        rGreen = new LayoutItem(Color.rgb(0,255,0)); // -11268754 #540D6E
        rBlue = new LayoutItem(Color.rgb(0,0,255)); // -11268754 #540D6E

        rColourPicked = new LayoutItem(Color.WHITE);
        rColourPicked.rect.set(-10,-10,-9,-9);
        eraseBG = new LayoutItem(Color.LTGRAY);
        gridBG = new LayoutItem(Color.LTGRAY);

        LayoutItemList.add(top);
        LayoutItemList.add(bottom);
        LayoutItemList.add(rColourPicked);
        LayoutItemList.add(rRed);
        LayoutItemList.add(rYellow);
        LayoutItemList.add(rGreen);
        LayoutItemList.add(rBlue);
        LayoutItemList.add(rPink);
        LayoutItemList.add(rCyan);
        LayoutItemList.add(rWhite);
        LayoutItemList.add(rErase);
        LayoutItemList.add(eraseBG);
        LayoutItemList.add(gridBG);

        ClickableItems.add(rErase);
        ClickableItems.add(rRed);
        ClickableItems.add(rYellow);
        ClickableItems.add(rGreen);
        ClickableItems.add(rBlue);
        ClickableItems.add(rPink);
        ClickableItems.add(rWhite);
        ClickableItems.add(rCyan);

        selectedColour = rRed.colour;
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
        currentBotAmount = TotalBots - count;
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
            top.rect.set(0,0,canvas.getWidth()/6, canvas.getHeight());
            bottom.rect.set(7 * canvas.getWidth()/8,0, canvas.getWidth(), canvas.getHeight());
            rRed.rect.set(top.rect.width()/9, (canvas.getHeight()/9) - 80, 4 * top.rect.width()/9, (canvas.getHeight()/9) + 80);
            rYellow.rect.set(5 * top.rect.width()/9, 2 * (canvas.getHeight()/9) - 80, 8 * top.rect.width()/9, 2 * (canvas.getHeight()/9) + 80);
            rGreen.rect.set(top.rect.width()/9, 3 * (canvas.getHeight()/9) - 80, 4 * top.rect.width()/9, 3 * (canvas.getHeight()/9) + 80);
            rCyan.rect.set(5 * top.rect.width()/9, 4 * (canvas.getHeight()/9) - 80, 8 * top.rect.width()/9, 4 * (canvas.getHeight()/9) + 80);
            rBlue.rect.set(top.rect.width()/9, 5 * (canvas.getHeight()/9) - 80, 4 * top.rect.width()/9, 5 * (canvas.getHeight()/9) + 80);
            rPink.rect.set(5 * top.rect.width()/9, 6 * (canvas.getHeight()/9) - 80, 8 * top.rect.width()/9, 6 * (canvas.getHeight()/9) + 80);
            rWhite.rect.set(top.rect.width()/9, 7 * (canvas.getHeight()/9) - 80, 4 * top.rect.width()/9, 7 * (canvas.getHeight()/9) + 80);
            rErase.rect.set(5 * top.rect.width()/9, 8 * (canvas.getHeight()/9) - 80, 8 * top.rect.width()/9, 8 * (canvas.getHeight()/9) + 80);
            eraser.setBounds(rErase.rect.left, rErase.rect.top, rErase.rect.right, rErase.rect.bottom);
            eraseBG.rect.set(rErase.rect);

            // Setting all the pixels' bounds, as well as the width of them
            squareWidth = (canvas.getHeight() - (top.rect.width() + bottom.rect.width())/2)/yDimWOBorder;
            excessSpace = canvas.getWidth() - (xDimWOBorder * squareWidth);
            gridBG.rect.set(((bottom.rect.left - top.rect.right)/2 + top.rect.width()) - (xDimWOBorder * squareWidth/2), canvas.getHeight()/2 - (yDimWOBorder * squareWidth/2), ((bottom.rect.left - top.rect.right)/2 + top.rect.width()) + (xDimWOBorder * squareWidth/2), canvas.getHeight()/2 + (yDimWOBorder * squareWidth/2));

            for (int i = 1; i < yDimension - 1; i++) {
                for (int j = 1; j < xDimension - 1; j++) {
                    uiGrid[i * xDimension + j].rect.set(gridBG.rect.left + ((j - 1) * squareWidth),
                            gridBG.rect.top + (i - 1) * squareWidth,
                            gridBG.rect.left + ((j - 1) * squareWidth) + squareWidth,
                            gridBG.rect.top + ((i - 1) * squareWidth) + squareWidth);
                    uiGrid[i * xDimension + j].location.set(j,i);
                }
            }
//            for (int i = 1; i < yDimension - 1; i++) {
//                for (int j = 1; j < xDimension - 1; j++) {
//                    uiGrid[i * xDimension + j].rect.set((excessSpace / 2) + ((j - 1) * squareWidth),
//                            100 + (i - 1) * squareWidth,
//                            (excessSpace / 2) + ((j - 1) * squareWidth) + squareWidth,
//                            100 + ((i - 1) * squareWidth) + squareWidth);
//                    uiGrid[i * xDimension + j].location.set(j,i);
//                }
//            }

        }
        /* This is for if the screen is portrait */
        else {
            // Set all the positions of the rectangles on the screen
            top.rect.set(0,0,canvas.getWidth(),300);
            bottom.rect.set(0,canvas.getHeight() - 300,canvas.getWidth(),canvas.getHeight());
            rRed.rect.set(        canvas.getWidth()/9 - 80, 70, canvas.getWidth()/9 + 80, 230);
            rYellow.rect.set( 2 * (canvas.getWidth()/9) - 80, 70, 2 * (canvas.getWidth()/9) + 80, 230);
            rGreen.rect.set(3 * (canvas.getWidth()/9) - 80, 70, 3 * (canvas.getWidth()/9) + 80, 230);
            rCyan.rect.set(4 * (canvas.getWidth()/9) - 80, 70, 4 * (canvas.getWidth()/9) + 80, 230);
            rBlue.rect.set(5 * (canvas.getWidth()/9) - 80, 70, 5 * (canvas.getWidth()/9) + 80, 230);
            rPink.rect.set(6 * (canvas.getWidth()/9) - 80, 70, 6 * (canvas.getWidth()/9) + 80, 230);
            rWhite.rect.set(7 * (canvas.getWidth()/9) - 80, 70, 7 * (canvas.getWidth()/9) + 80, 230);
            rErase.rect.set(8 * (canvas.getWidth()/9) - 80, 70, 8 * (canvas.getWidth()/9) + 80, 230);
            eraser.setBounds(rErase.rect.left + 4, rErase.rect.top + 4, rErase.rect.right - 4, rErase.rect.bottom - 4);
            eraseBG.rect.set(rErase.rect);

            // Setting all the pixels' bounds, as well as the width of them
            squareWidth = (canvas.getWidth() - 200)/xDimWOBorder;
            excessSpace = canvas.getHeight() - (yDimWOBorder * squareWidth);
            gridBG.rect.set(canvas.getWidth()/2 - (xDimWOBorder * squareWidth/2), ((bottom.rect.top - top.rect.bottom)/2 + top.rect.height()) - (yDimWOBorder * squareWidth/2), canvas.getWidth()/2 + (xDimWOBorder * squareWidth/2), (((bottom.rect.top - top.rect.bottom)/2) + top.rect.height()) + (yDimWOBorder * squareWidth/2));

            for (int i = 1; i < yDimension - 1; i++) {
                for (int j = 1; j < xDimension - 1; j++) {
                    uiGrid[i * xDimension + j].rect.set(gridBG.rect.left + ((j - 1) * squareWidth),
                            gridBG.rect.top + (i - 1) * squareWidth,
                            gridBG.rect.left + ((j - 1) * squareWidth) + squareWidth,
                            gridBG.rect.top + ((i - 1) * squareWidth) + squareWidth);
                    uiGrid[i * xDimension + j].location.set(j,i);
                }
            }
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
        postInvalidate();
    }


}