package com.example.mischa.pixelbotui.Swarm;

import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Ethan on 16/03/2018.
 */

// This Class will be used to build a swarm which consists of individual pixels.

public class Swarm {

    public ArrayList<Bot> SwarmList = new ArrayList<>();
    Grid BotGrid;
// Creates swarms by amount and by their colour, colour is currently not a feature. However creating with it in mind
    // Swarms can be created in multiple colours but in other methods, but each 'Swarm' is one colour

    Swarm(int SwarmAmount, Color SwarmColour, int gridWidth, int gridHeight){
        BotGrid = new Grid(gridWidth, gridHeight);

        for(int i = 0; i <= SwarmAmount; i++){
            Point DefaultLoc = new Point(0,0);
            Bot Add = new Bot(i, SwarmColour, DefaultLoc);
            SwarmList.add(Add);
            BotGrid.addBot(Add);
        }
    }

    public void addDestinationsToGrid(int x, int y) {
        this.BotGrid.addDestination(x, y);
    }
    // When the type of output of the pathfinding algorithm has been determined, I will change it from 'void'.
    public void solveGrid() {

    }
}
