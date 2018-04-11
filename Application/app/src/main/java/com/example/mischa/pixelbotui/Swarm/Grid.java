package com.example.mischa.pixelbotui.Swarm;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Daniel on 02/04/2018.
 */

// This class represents the virtual grid. It also represents as the 'problem' for the A* to solve.
public class Grid {

    // Grid is defined when the class is instantiated.
    // Possible values of the grid are: E (empty), B (bot) and D (destination).
    String[][] Grid;
    int[] Dimensions;
    ArrayList<Bot> Bots;
    ArrayList<Point> Destinations;
    HashMap<Bot, Point> BotDestPairs;

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
        Point botCoord = bot.Location;
        if (!this.Grid[botCoord.x][botCoord.y].equals("E"))
            throw new IllegalStateException("Position at coordinates: " + botCoord.x + ", " + botCoord.y + " is not empty!");

        this.Grid[botCoord.x][botCoord.y] = "B";
        this.Bots.add(bot);
    }

    public void addDestination(int x, int y) {
        if (!this.Grid[x][y].equals("E"))
            throw new IllegalStateException("Position at coordinates: " + x + ", " + y + " is not empty!");

        this.Grid[x][y] = "D";
        Point addToDestination = new Point(x, y);
        Destinations.add(addToDestination);
    }

    // May be completely redundant depending on implementation.
    public void resetAtCoord(int x, int y) {
        this.Grid[x][y] = "E";
    }

    // As of right now, it only maps the first bot with first inputted destination.
    // This is not the intended feature, but this will work for single bot implementation (A* Stage 0).
    void mapBotToDest() {
        BotDestPairs = new HashMap<>();
        BotDestPairs.put(Bots.get(0), Destinations.get(0));
    }

    public Point getDestForBot(Bot bot) {
        mapBotToDest();
        return BotDestPairs.get(bot);
    }

    // Not implemented yet.
    public List returnPossibleMoves(Point coord) {

        List<Node> temp = new ArrayList<>();
        return temp;
    }
    public void updateBoard(List instructions) {

    }

}