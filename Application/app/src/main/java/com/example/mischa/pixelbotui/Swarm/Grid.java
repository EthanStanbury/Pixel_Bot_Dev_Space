package com.example.mischa.pixelbotui.Swarm;

import android.graphics.Color;
import android.graphics.Point;

import com.example.mischa.pixelbotui.UI.Pixel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.mischa.pixelbotui.Swarm.Type.*;

/**
 * Created by Daniel on 02/04/2018.
 */

// This class represents the virtual grid. It also represents as the 'problem' for the A* to solve.
public class Grid {

    // Grid is defined when the class is instantiated.
    // Possible values of the grid are: E (empty), B (bot) and D (destination).
    private Position[][] Grid;
    private int[] Dimensions;
    private ArrayList<Bot> Bots;
    private ArrayList<Point> Destinations;

    public HashMap<Bot, Point> BotDestPairs;

    public Grid(int width, int height) {
        Dimensions = new int[2];
        Dimensions[0] = width;
        Dimensions[1] = height;

        // Setup an empty grid with dimensions defined above.
        Grid = new Position[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++)
                this.Grid[i][j] = new Position();
        }

        // Init the array arrays
        Bots = new ArrayList<>();
        Destinations = new ArrayList<>();

    }

    public void addBot(Bot bot) {
        Point botCoord = bot.Location;
        if (this.Grid[botCoord.x][botCoord.y].Type != EMPTY)
            throw new IllegalStateException("Position at coordinates: " + botCoord.x + ", " + botCoord.y + " is not empty!");

        Grid[botCoord.x][botCoord.y].Type = BOT;
        Bots.add(bot);
    }

    public void addDestination(Pixel pixel) {
        int x = pixel.location.x;
        int y = pixel.location.y;

        if (this.Grid[x][y].Type != EMPTY)
            throw new IllegalStateException("Position at coordinates: " + x + ", " + y + " is not empty!");

        Grid[x][y].Type = DESTINATION;
        Grid[x][y].Colour = pixel.colour;
        Point addToDestination = new Point(x, y);
        Destinations.add(addToDestination);
    }

    // May be completely redundant depending on implementation.
    public void resetAtCoord(int x, int y) {
        Grid[x][y].Type = EMPTY;
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

class Position {
    Type Type = EMPTY;
    int Colour = Color.TRANSPARENT;

    Position() {}
}

// Each points on the grid will only ever have 3 different states:
// either it is empty, has a bot or is a destination.
enum Type {
    EMPTY,
    BOT,
    DESTINATION
}