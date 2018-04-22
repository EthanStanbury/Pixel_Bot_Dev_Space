package com.example.mischa.pixelbotui.Swarm;

import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;

import com.example.mischa.pixelbotui.Intergration.SwarmAdapter;
import com.example.mischa.pixelbotui.Intergration.UIAdapter;
import com.example.mischa.pixelbotui.UI.PBCanvas;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ethan on 16/03/2018.
 */

// This Class will be used to build a swarm which consists of individual pixels.

public class Swarm {

    public ArrayList<Bot> SwarmList = new ArrayList<>();

// Creates swarms by amount and by their colour, colour is currently not a feature. However creating with it in mind
    // Swarms can be created in multiple colours but in other methods, but each 'Swarm' is one colour
 // This needs to be called by Mischa's app
    // Mischa's app needs to have someone input in the type of bots that are available, this will have to be before app is used
    // This is up to what mischa thinks is best but it could be an array of what colour and how many of that colour. The rest of the swarm can be created here
    // This function needs to create the swarm. And in the doing this every time a new colour is selected it needs to create a new array and add it to wholeswarm.
    //It should switch on colour and create a unique id for each bot, which will then be placed in it's swarm
    public Swarm(Color Colour, Integer numberOfBots){
        Point defaultLocation = new Point(0, 1);

            for (int i = 0; i < numberOfBots; i++){
                String id = (Colour.toString() +"/"+ i);
                Bot add = new Bot(id, Colour, defaultLocation);
                SwarmList.add(add);
                UIAdapter.destinationGrid.addBot(add);
            }



    }


    // When the type of output of the pathfinding algorithm has been determined, I will change it from 'void'.
    public void solveGrid() {

    }
}
