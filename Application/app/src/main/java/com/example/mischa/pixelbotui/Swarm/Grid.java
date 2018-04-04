package com.example.mischa.pixelbotui.Swarm;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 16/03/2018.
 */

// This class represents the virtual grid. It also represents as the 'problem' for the A* to solve.
public class Grid {

    // Grid is defined when the class is instantiated.
    // Possible values of the grid are: E (empty), B (bot) and D (destination).
    String[][] Grid;
    int[] Dimensions;
    ArrayList<Bot> Bots;

    Grid(int width, int height) {
        this.Dimensions = new int[2];
        this.Dimensions[0] = width;
        this.Dimensions[1] = height;

        // Setup an empty grid with dimensions defined above.
        this.Grid = new String[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++)
                this.Grid[i][j] = "E";
        }

        // Init the bots array
        this.Bots = new ArrayList<>();

    }

    public void addBot(Bot bot) {
        Point botCoord = bot.getLocation();
        if (!this.Grid[botCoord.x][botCoord.y].equals("E"))
            throw new IllegalStateException("Position at coordinates: " + botCoord.x + ", " + botCoord.y + " is not empty!");

        this.Grid[botCoord.x][botCoord.y] = "B";
        this.Bots.add(bot);
    }

    public void addDestination(int x, int y) {
        if (!this.Grid[x][y].equals("E"))
            throw new IllegalStateException("Position at coordinates: " + x + ", " + y + " is not empty!");

        this.Grid[x][y] = "D";
    }

    // May be completely redundant depending on implementation.
    public void resetAtCoord(int x, int y) {
        this.Grid[x][y] = "E";
    }

    // Not implemented yet.
    public List returnPossibleMoves(Bot bot) {
        return new ArrayList();
    }
    public void updateBoard(List instructions) {

    }

}
