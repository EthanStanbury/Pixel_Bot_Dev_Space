package com.example.mischa.pixelbotui.Swarm;

import java.util.ArrayList;
import java.util.List;

// This class represents the virtual grid. It also represents as the 'problem' for the A* to solve.
public class Grid {

    // Grid is defined when the class is instantiated.
    // Possible values of the grid are: E (empty), B (bot) and D (destination).
    String[][] Grid;
    int[] Dimensions;

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

    }

    public void addBotToGrid(int x, int y) {
        if (this.Grid[x][y].equals("E"))
            throw new IllegalStateException("Position at coordinates: " + x + ", " + y + " is not empty!");

        this.Grid[x][y] = "B";
    }

    public void addDestinationToGrid(int x, int y) {
        if (!this.Grid[x][y].equals("E"))
            throw new IllegalStateException("Position at coordinates: " + x + ", " + y + " is not empty!");

        this.Grid[x][y] = "D";
    }

    // May be completely redundant depending on implementation.
    public void resetAtCoord(int x, int y) {
        this.Grid[x][y] = "E";
    }

    // Not implemented yet.
    public List returnPossibleMoves() {
        return new ArrayList();
    }

}
