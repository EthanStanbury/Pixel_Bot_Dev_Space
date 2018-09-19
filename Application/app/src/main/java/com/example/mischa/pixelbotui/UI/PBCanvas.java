package com.example.mischa.pixelbotui.UI;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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

    int noOfSquares = yDimension * xDimension;
    int squareWidth;
    int selectedColour;
    public int TotalBots = 7;

    int currentBotAmount;
    LayoutItem gridBG;

    Context context;

    int[] saveState = new int[noOfSquares];

    public static ArrayList<Pixel> border = new ArrayList<>();

    public PBCanvas(Context context) {  //,  int botsTotal) {
        super(context);
        this.context = context;
        paint = new Paint();
        uiGrid = new Pixel[noOfSquares];
        gridBG = new LayoutItem(Color.LTGRAY);

        if (xDimension < 10 || yDimension < 10){
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setMessage("Grid dimension below 10. Please restart the app and select grid dimension over 10. ");
            alert.show();
        }

        for (int i = 0; i < noOfSquares; i++) {
            uiGrid[i] = new Pixel(Color.TRANSPARENT, new Point(0,0));
        }
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

            // Setting all the pixels' bounds, as well as the width of them
            squareWidth = (canvas.getHeight() - canvas.getHeight()/10)/yDimWOBorder;
            gridBG.rect.set(canvas.getWidth()/2 - (xDimWOBorder * squareWidth/2), canvas.getHeight()/2 - (yDimWOBorder * squareWidth/2), canvas.getWidth()/2 + (xDimWOBorder * squareWidth/2), canvas.getHeight()/2 + (yDimWOBorder * squareWidth/2));

            for (int i = 1; i < yDimension - 1; i++) {
                for (int j = 1; j < xDimension - 1; j++) {
                    uiGrid[i * xDimension + j].rect.set(
                            gridBG.rect.left + ((j - 1) * squareWidth),
                            gridBG.rect.top + (i - 1) * squareWidth,
                            gridBG.rect.left + ((j - 1) * squareWidth) + squareWidth,
                            gridBG.rect.top + ((i - 1) * squareWidth) + squareWidth);
                    uiGrid[i * xDimension + j].location.set(j,i);
                }
            }
        }

        /* This is for if the screen is portrait */
        else {
            // Setting all the pixels' bounds, as well as the width of them
            squareWidth = (canvas.getWidth() - canvas.getWidth()/10)/xDimWOBorder;
            gridBG.rect.set(canvas.getWidth()/2 - (xDimWOBorder * squareWidth/2), canvas.getHeight()/2 - (yDimWOBorder * squareWidth/2), canvas.getWidth()/2 + (xDimWOBorder * squareWidth/2), canvas.getHeight()/2 + (yDimWOBorder * squareWidth/2));
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

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(50);
        paint.setColor(Color.BLACK);
        paint.setColor(Color.WHITE);
        paint.setTextSize(75);
        // If the screen is landscape
//        if ((float) canvas.getWidth()/canvas.getHeight() > (float) xDimWOBorder/yDimWOBorder) {
//            canvas.drawText(currentBotAmount + "", bottom.rect.exactCenterX(), 4 * bottom.rect.exactCenterY() / 3 - 50, paint);
//            canvas.drawText("bots left", bottom.rect.exactCenterX(), 4 * bottom.rect.exactCenterY() / 3 + 50, paint);
//        } else { // If the screen is portrait
//            canvas.drawText(currentBotAmount + "", 2 * bottom.rect.exactCenterX() / 3, bottom.rect.exactCenterY() - 50, paint);
//            canvas.drawText("bots left", 2 * bottom.rect.exactCenterX() / 3, bottom.rect.exactCenterY() + 50, paint);
//        }

        // Drawing all the Pixels + BG
        paint.setColor(gridBG.colour);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRect(gridBG.rect, paint);
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