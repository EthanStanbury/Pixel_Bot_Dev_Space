package com.example.mischa.pixelbotui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.mischa.pixelbotui.Intergration.SwarmAdapter;
import com.example.mischa.pixelbotui.Intergration.UIAdapter;
import com.example.mischa.pixelbotui.Swarm.Direction;
import com.example.mischa.pixelbotui.Swarm.PathFinder;
import com.example.mischa.pixelbotui.UI.MainActivity;
import com.example.mischa.pixelbotui.UI.Pixel;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    List<Direction> wrongSolution;
    public static HashMap<Integer, Integer> BotAmounts = new HashMap<>();



    private Boolean solutionsCheck(HashMap<String, List<Direction>> solutions){


        Boolean valid = false;
        for (String key: solutions.keySet()) {
            List<Direction> list = solutions.get(key);
            for (int i = 0; i < list.size();  i++) {
                String direction = list.get(i).toString();
                switch (direction){
                    case "L":valid = true;
                        break;
                    case "D":valid = true;
                        break;
                    case "R":valid = true;
                        break;
                    case "U":valid = true;
                        break;
                    case "[":valid = true;
                        break;
                    case",": valid = true;
                        break;
                    case "]":valid = true;
                        break;
                    case " ":valid = true;
                        break;
                    default: valid = false;
                        break;
                }
                if (!valid)break;

            }
            wrongSolution = list;
            if (!valid)break;

        }
        return valid;
    }

    @Test
    public void pathFindCalcTime() throws Exception {


        // add the bots
        BotAmounts.put(-1162650, 4); //Red
        BotAmounts.put(-11713, 4); //Yellow
        BotAmounts.put(-15815319, 4); //Green
        BotAmounts.put(-12857684, 4); //Blue
        BotAmounts.put(-11268754, 4); //Purple
        BotAmounts.put(Color.BLACK, 4); //Black
        //set dimensions
        int yDimension = 10;
        int xDimension = 10;
        int noOfSquares = yDimension*xDimension;
        Pixel[] uiGrid;
        uiGrid = new Pixel[noOfSquares];

        // create all the null grid
        for (int i = 0; i < noOfSquares; i++) {
            uiGrid[i] = new Pixel(Color.TRANSPARENT, new Point(0,0));
        }

        //set the pixel location to change depending on what pixel it is
        for (int i = 0; i < yDimension; i++) {
            for (int j = 0; j < xDimension; j++) {
                uiGrid[i * xDimension + j].rect.set((j * 10),
                        100 + i * 10,
                        (j * 10) + 10,
                        100 + (i * 10) + 10);
                uiGrid[i * xDimension + j].location.set(j,i);
            }
        }
        //create 4 random 'on' pixels
        for (int i = 4; i <= 4; i++){
            Random random = new Random();
            int select = random.nextInt(8) + 1;

            Pixel pixelEdit = uiGrid[select * xDimension + select];
            pixelEdit.colour = Color.RED;
            uiGrid[select * xDimension + select]  = pixelEdit;
        }

        //create the swarm
        SwarmAdapter.SwarmCreate(BotAmounts);
        //create the grid
        UIAdapter.createGridWpixel(uiGrid);
        //find solutions
        long start = System.currentTimeMillis(); //To start to measure the time it takes to get the solutions
        HashMap<String, List<Direction>> solutions =  PathFinder.getSolutions(UIAdapter.destinationGrid);
        long end = System.currentTimeMillis();

        List<Direction> solutionsTest = solutions.get(Color.RED);

        System.out.println("OIOIOIOI" + solutions);

        assertTrue("Got a time greater than 10seconds which was: " + (start - end), start - end < 10000);
        assertTrue("Got an invalid solution string, it was: " + uiGrid.length, solutionsCheck(solutions));
        assertTrue("Output a different amount of solutions than bots got "+  solutions.size() + " solutions", solutions.size() >= 4 );




    }
}
