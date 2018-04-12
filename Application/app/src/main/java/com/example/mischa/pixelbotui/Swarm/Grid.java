package com.example.mischa.pixelbotui.Swarm;

import android.graphics.Color;
import android.graphics.Point;

import com.example.mischa.pixelbotui.UI.Pixel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.mischa.pixelbotui.Swarm.Type.*;
import static com.example.mischa.pixelbotui.Swarm.Direction.*;

/**
 * Created by Daniel on 02/04/2018.
 */

// This class represents the virtual grid. It also represents as the 'problem' for the A* to solve.
public class Grid {



    // Grid is defined when the class is instantiated.
    // Possible values of the grid are: E (empty), B (bot) and D (destination).
    private Position[][] Grid;
    private int[] Dimensions;
    public static ArrayList<Bot> Bots;
    public static ArrayList<Point> Destinations;

    public static HashMap<Bot, Point> BotDestPairs;

    public Grid(int width, int height) {
        Dimensions = new int[2];
        Dimensions[0] = width;
        Dimensions[1] = height;

        // Setup an empty grid with dimensions defined above.
        Grid = new Position[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                this.Grid[i][j] = new Position();
                if (checkOutsideBoundary(i, j)) {
                    this.Grid[i][j].Type = OFF_GRID;
                }
            }
        }

        // Init the array arrays
        Bots = new ArrayList<>();
        Destinations = new ArrayList<>();

    }

    public void addBot(Bot bot) {
        Point botCoord = bot.Location;
        // if (this.Grid[botCoord.x][botCoord.y].Type != EMPTY)
        //    throw new IllegalStateException("Px`osition at coordinates: " + botCoord.x + ", " + botCoord.y + " is not empty!");

        Grid[botCoord.x][botCoord.y].Type = BOT;
        Bots.add(bot);
    }

    public void addDestination(Pixel pixel) {
        int x = pixel.location.x;
        int y = pixel.location.y;

        // if (this.Grid[x][y].Type != EMPTY)
        //    throw new IllegalStateException("Position at coordinates: " + x + ", " + y + " is not empty!");

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
    public static void mapBotToDest() {
        BotDestPairs = new HashMap<>();
        BotDestPairs.put(Bots.get(0), Destinations.get(0));
    }

    // Not implemented yet.
    public List getSuccessorNodes(Node currentNode) {
        List<Node> successors = new ArrayList<>();
        Point coord = currentNode.Coord;

        if (getTypeAtCoord(translateMove(coord, U)) == EMPTY)
            successors.add(new Node(translateMove(coord, U), U, 1));

        if (getTypeAtCoord(translateMove(coord, D)) == EMPTY)
            successors.add(new Node(translateMove(coord, D), D, 1));

        if (getTypeAtCoord(translateMove(coord, L)) == EMPTY)
            successors.add(new Node(translateMove(coord, L), L, 1));

        if (getTypeAtCoord(translateMove(coord, R)) == EMPTY)
            successors.add(new Node(translateMove(coord, R), R, 1));

        return successors;
    }
    public void updateBoard(List instructions) {

    }

    private boolean checkOutsideBoundary(int x, int y) {
        return (x == 0 || y == 0) || (x == (Dimensions[0] - 1) || y == (Dimensions[1] - 1));
    }

    private Point translateMove(Point coord, Direction dir) {
        Point tempCoord = coord;
        switch (dir) {
            case U:
                tempCoord.offset(0, 1);
                break;
            case D:
                tempCoord.offset(0, -1);
                break;
            case L:
                tempCoord.offset(-1, 0);
                break;
            case R:
                tempCoord.offset(1, 0);
                break;
            default:
                throw new IllegalArgumentException("Invalid direction provided! Can only accept U, D, L and R");
        }
        return tempCoord;
    }

    private Type getTypeAtCoord(Point coord) {
        int x = coord.x;
        int y = coord.y;
        if ((x >= 0 && y >= 0) && (x < Dimensions[0] && y < Dimensions[1]))
            return Grid[x][y].Type;
        else
            return OFF_GRID;
    }

}

class Position {
    Type Type = EMPTY;
    int Colour = Color.TRANSPARENT;

    Position() {}
}

// Each points on the grid will only ever have 4 different states:
// either it is empty, off the grid, has a bot or is a destination.
enum Type {
    EMPTY,
    OFF_GRID,
    BOT,
    DESTINATION
}

