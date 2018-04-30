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
    public static List<Bot> Bots;
    public static List<Point> Destinations;

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
        BotDestPairs = new HashMap<>();

    }

    public void addBot(Bot bot) {
        Point botCoord = bot.Location;
        // if (this.Grid[botCoord.x][botCoord.y].Type != EMPTY)
        //    throw new IllegalStateException("Px`osition at coordinates: " + botCoord.x + ", " + botCoord.y + " is not empty!");

        Grid[botCoord.x][botCoord.y].Type = BOT;
        Grid[botCoord.x][botCoord.y].Colour = 0; //bot.Colour; BROKEN DUE TO DIFFERENCES IN COLOUR TYPES (int vs Color)
        Bots.add(bot);
    }

    public void addDestination(Pixel pixel) {
        int x = pixel.location.x;
        int y = pixel.location.y;

        if (this.Grid[x][y].Type == OFF_GRID)
            throw new IllegalStateException("Position at coordinates: " + x + ", " + y + " is outside the boundary!");

        Grid[x][y].Type = DESTINATION;
        Grid[x][y].Colour = 0;//pixel.colour;
        Destinations.add(new Point(x, y));
        //System.out.println("GRID.JAVA: " + Destinations.get(0));
    }

    // May be completely redundant depending on implementation.
    public void resetAtCoord(int x, int y) {
        Grid[x][y].Type = EMPTY;
    }

    // Pairs all bots to their individual destinations based on distance.
    // The bot that is closest to a certain destination will be chosen as bot-dest pair.
    public static void mapBotToDest() {
        if (Bots.size() < Destinations.size())
            throw new IllegalStateException("FATAL ERROR: Number of destinations is greater than the number of available bots!");

        List<Point> remainingDest = new ArrayList<>(Destinations);
        List<Bot> remainingBots = new ArrayList<>(Bots);

        // For every destination, find the closest bot by the manhattan distance to it.
        while (remainingDest.size() > 0 ) {
            int lowestManhattanDist = Integer.MAX_VALUE;
            int lowestManDistIndex = 0;
            for (int i = 0; i < remainingBots.size(); i++) {
                int ManhattanDist = Math.abs(remainingBots.get(i).Location.x - remainingDest.get(0).x) + Math.abs(remainingBots.get(i).Location.y - remainingDest.get(0).y);
                if (ManhattanDist < lowestManhattanDist) {
                    lowestManhattanDist = ManhattanDist;
                    lowestManDistIndex = i;
                }
            }
            //System.out.println(remainingBots.get(lowestManDistIndex).Location.y);
            BotDestPairs.put(remainingBots.get(lowestManDistIndex), remainingDest.get(0));

            remainingBots.remove(lowestManDistIndex);
            remainingDest.remove(0);
        }
    }

    // Not implemented yet.
    public List getSuccessorNodes(Node currentNode) {
        List<Node> successors = new ArrayList<>();
        Point coord = currentNode.Coord;
        // System.out.println(translateMove(coord, D));
        // System.out.println(getTypeAtCoord(translateMove(coord, D)));

        Point tempCoord = translateMove(coord, U);
        if (getTypeAtCoord(tempCoord) == EMPTY || getTypeAtCoord(tempCoord) == DESTINATION)
            successors.add(new Node(tempCoord, U, 1));

        tempCoord = translateMove(coord, D);
        if (getTypeAtCoord(tempCoord) == EMPTY || getTypeAtCoord(tempCoord) == DESTINATION)
            successors.add(new Node(tempCoord, D, 1));

        tempCoord = translateMove(coord, L);
        if (getTypeAtCoord(tempCoord) == EMPTY || getTypeAtCoord(tempCoord) == DESTINATION)
            successors.add(new Node(tempCoord, L, 1));

        tempCoord = translateMove(coord, R);
        if (getTypeAtCoord(tempCoord) == EMPTY || getTypeAtCoord(tempCoord) == DESTINATION)
            successors.add(new Node(tempCoord, R, 1));

        return successors;
    }
    public void updateBoard(List instructions) {

    }

    private boolean checkOutsideBoundary(int x, int y) {
        return (x == 0 || y == 0) || (x == (Dimensions[0] - 1) || y == (Dimensions[1] - 1));
    }

    private Point translateMove(Point coord, Direction dir) {
        Point tempCoord = new Point(coord.x, coord.y);
        switch (dir) {
            case U:
                tempCoord.offset(0, -1);
                break;
            case D:
                tempCoord.offset(0, 1);
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
        if ((x < 0 || y < 0) || (x >= Dimensions[0] || y >= Dimensions[1]))
            return OFF_GRID;
        else
            return Grid[x][y].Type;
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

