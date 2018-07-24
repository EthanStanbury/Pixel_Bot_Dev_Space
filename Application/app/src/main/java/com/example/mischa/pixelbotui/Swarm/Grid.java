package com.example.mischa.pixelbotui.Swarm;

import android.graphics.Color;
import android.graphics.Point;

import com.example.mischa.pixelbotui.UI.Pixel;

import java.util.ArrayList;
import java.util.EmptyStackException;
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
    // Possible values of the grid are: EMPTY, OFF_GRID, BOT, DESTINATION.
    private Position[][] Grid;
    private int[] Dimensions;               // Stores the size of the grid in (x, y) format, including the OFF_GRID boundary
    private static List<Bot> Bots;          // Stores a list of all the available bots and all of its relevant information
    public static List<Point> Destinations; // Stores all destination points (which is determined by the coloured pixels on the screen

    public static HashMap<Bot, Point> BotDestPairs; // Once the bots are mapped to their respective destinations, the pair is stored in here

    // Init Grid object here
    public Grid(int width, int height) {
        // Save the dimensions of the grid
        Dimensions = new int[2];
        Dimensions[0] = width;
        Dimensions[1] = height;

        // Setup an empty grid with dimensions parsed in.
        Grid = new Position[width][height];

        // Basically labels all of the edge coordinates as 'OFF_GRID', as the grid is set up like this:
        /*
        O_G     = Off Grid (where bots can be stored in)
        E       = Empty (Where bots can travel across)

        O_G O_G O_G O_G O_G
        O_G  E   E   E  O_G
        O_G  E   E   E  O_G
        O_G  E   E   E  O_G
        O_G O_G O_G O_G O_G
         */
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // Create new Position object for every coordinate. The default type is 'EMPTY'.
                this.Grid[i][j] = new Position();
                // Change type to OFF_GRID for all edge coordinates.
                if (checkOutsideBoundary(i, j))
                    this.Grid[i][j].Type = OFF_GRID;
            }
        }

        // Init the array arrays
        Bots = new ArrayList<>();
        Destinations = new ArrayList<>();
        BotDestPairs = new HashMap<>();

    }

    // This method is called every time a bot is to be added
    public void addBot(Bot bot) {  //, int timeStep) {
        // Extract the bot coordinates from the bot object
        Point botCoord = bot.Location;

        // Set the grid at certain position to be of type Bot
        Grid[botCoord.x][botCoord.y].Type = BOT;
        // Set colour
        Grid[botCoord.x][botCoord.y].Colour = bot.Colour;
        // Add time step with bot ID.
        Grid[botCoord.x][botCoord.y].OccupiedTimeSteps.put(0, bot.BotID);
        // Add to Bots list
        Bots.add(bot);
    }

    // This is called every time a destination (basically a coloured pixel on the grid) is to be added
    public void addDestination(Pixel pixel) {
        // Extract pixel coordinates
        int x = pixel.location.x;
        int y = pixel.location.y;

        // If coordinates are not actually on the board, throw an exception.
        if (this.Grid[x][y].Type == OFF_GRID)
            throw new IllegalStateException("Position at coordinates: " + x + ", " + y + " is outside the boundary!");

        // Set grid at coord to be destination
        Grid[x][y].IsDestination = true;
        // Set colour of it
        Grid[x][y].Colour = pixel.colour;
        // Add each pixel to the Destinations list
        Destinations.add(new Point(x, y));
    }

    // Not used yet. It is advised that this is not to be used yet as tests has not been done with it
    public void resetAtCoord(int x, int y) {
        Grid[x][y].Type = EMPTY;
    }

    // Pairs all bots to their individual destinations based on distance.
    // The bot that is closest to a certain destination will be chosen as bot-dest pair.
    public void mapBotToDest() {
        // Store all detected unique colours that are used (for bots and destinations)
        List<Integer>                   colours =       new ArrayList<>();

        // Temporarily store the bots and destinations that are yet to be mapped.
        // Not every bot may be used from remainingBots, but everything in remainingDest will be used.
        // This hashmap is split according to the colour codes (Integer). For example, all of red bots will be in the same list.
        HashMap<Integer, List<Bot>>     remainingBots = new HashMap<>();
        HashMap<Integer, List<Point>>   remainingDest = new HashMap<>();

        /* ----- Detect all unique colours, bots and destinations ----- */
        for (int i = 0; i < Bots.size(); i++) {
            int botColour = Bots.get(i).Colour;
            // If colour doesn't exist in the colours list, then add it.
            // Note that this is only done in the bots section,
            // as there will always be a more variety of colours of bots than destinations.
            // (no. of destinations is always going to be less than or equal to no. of bots of same colour)
            if (!colours.contains(botColour))
                colours.add(botColour);

            // Init the list with colour key if the list has not been created yet
            if (!remainingBots.containsKey(botColour))
                remainingBots.put(botColour, new ArrayList<Bot>());

            remainingBots.get(botColour).add(Bots.get(i));
        }

        // Repeat the above for all destinations too
        for (int i = 0; i < Destinations.size(); i++) {
            Point destCoord = Destinations.get(i);
            int destColour = this.Grid[destCoord.x][destCoord.y].Colour;

            if (!remainingDest.containsKey(destColour))
                remainingDest.put(destColour, new ArrayList<Point>());

            remainingDest.get(destColour).add(new Point(destCoord));
        }
        /* -------------------------------------------------------------- */

        // Make sure that there are less than or equal number of destinations to bots for every colour
        for (int i = 0; i < colours.size(); i++) {
            if (remainingBots.containsKey(colours.get(i)) && remainingDest.containsKey(colours.get(i))) {
                if (remainingBots.get(colours.get(i)).size() < remainingDest.get(colours.get(i)).size())
                    throw new IllegalStateException("FATAL ERROR: Number of destinations is greater than the number of available bots!");
            }
        }

        // Basic idea: Pair every bot to the closest compatible destination. Please note that this method can be changed later.
        for (int i = 0; i < colours.size(); i++) {
            if (remainingBots.containsKey(colours.get(i)) && remainingDest.containsKey(colours.get(i))) {
                // Get the list of bots and destinations with matching colours
                List <Bot>   CSremainBots = new ArrayList<>(remainingBots.get(colours.get(i)));
                List <Point> CSremainDest = new ArrayList<>(remainingDest.get(colours.get(i)));

                // For every destination, find the closest bot by the manhattan distance to it
                while (CSremainDest.size() > 0 ) {
                    int lowestManhattanDist = Integer.MAX_VALUE;
                    int lowestManDistIndex = 0;
                    for (int j = 0; j < CSremainBots.size(); j++) {

                        int ManhattanDist = Math.abs(CSremainBots.get(j).Location.x - CSremainDest.get(0).x) +
                                            Math.abs(CSremainBots.get(j).Location.y - CSremainDest.get(0).y);

                        // If dist is lower than the lowest known, then replace it with new values
                        if (ManhattanDist < lowestManhattanDist) {
                            // Set the comparing distance value to the newly detected lowest value
                            lowestManhattanDist = ManhattanDist;
                            lowestManDistIndex = j; // Save the index
                        }
                    }

                    // Pair the bot with destination with the lowest known manhattan distance.
                    BotDestPairs.put(CSremainBots.get(lowestManDistIndex), CSremainDest.get(0));

                    // Remove the bot and dest from the 'remaining' lists
                    CSremainBots.remove(lowestManDistIndex);
                    // Removing the first element will shift all contents towards index 0 by 1, hence this method will work and every destination will be paired.
                    CSremainDest.remove(0);
                }
            }
        }
    }

    // Return every possible move from a certain coordinate position.
    public List getSuccessorNodes(Node currentNode) {
        // Save a list of all moves possible.
        List<Node> successors = new ArrayList<>();
        // Extract the coordinate passed in
        Point coord = currentNode.Coord;

        // Change coordinate according to the specified move (in this case, U)
        Point tempCoord = translateMove(coord, U);
        // Check if this new coordinate is somewhere the bot can move to
        if (getTypeAtCoord(tempCoord) == EMPTY)
            // Add the new 'Node' to the successors list. Save the new coordinate, direction taken and the cost of movement
            successors.add(new Node(tempCoord, U, 1));

        // Repeat for all other directions
        tempCoord = translateMove(coord, D);
        if (getTypeAtCoord(tempCoord) == EMPTY)
            successors.add(new Node(tempCoord, D, 1));

        tempCoord = translateMove(coord, L);
        if (getTypeAtCoord(tempCoord) == EMPTY)
            successors.add(new Node(tempCoord, L, 1));

        tempCoord = translateMove(coord, R);
        if (getTypeAtCoord(tempCoord) == EMPTY)
            successors.add(new Node(tempCoord, R, 1));

        // Staying at current position will always be a valid move.
        successors.add(new Node(coord, S, 1));

        return successors;
    }

    // Not implemented yet, but will be for A* 'Stage 4' (anti collision features)
    // Basic idea is to save the time step of the bots' locations
    // so that it could be used to make that coordinate inaccessible at certain time steps.
    public void updateBoard(Point currentPos, int timeStep, String botID, boolean reachedDestFlag) {
        Position pos = Grid[currentPos.x][currentPos.y];
        pos.Type = BOT;
        pos.OccupiedTimeSteps.put(timeStep, botID);

        pos.IsPushable = reachedDestFlag;

        if (timeStep > pos.lastOccupiedTimeStep) {
            pos.lastOccupiedTimeStep = timeStep;
            pos.lastOccupiedID = botID;
        }
    }

    // Checks if a certain position is available to be occupied at a certain time step.
    public boolean checkAvailability(Point position, int timeStep) {
        Position pos = Grid[position.x][position.y];

        if (pos.Type != EMPTY && pos.Type != BOT)
            return false;
        else if (pos.Type == EMPTY)
            return true;
        else {
            // Return true if not occupied at the given time step, return false if it is occupied.
            return !pos.OccupiedTimeSteps.containsKey(timeStep);
        }
    }

    //-------------------------
    //-----Private Methods-----
    //-------------------------

    // Check if the given set of coordinates are on the edge of the grid
    private boolean checkOutsideBoundary(int x, int y) {
        return (x == 0 || y == 0) || (x == (Dimensions[0] - 1) || y == (Dimensions[1] - 1));
    }

    // Edit the given coordinates accordingly based on provided direction.
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
            case S:
                break;
            default:
                throw new IllegalArgumentException("Invalid direction provided! Can only accept U, D, L, R and S");
        }
        return tempCoord;
    }

    // Return type at a specific point on the grid.
    private Type getTypeAtCoord(Point coord) {
        int x = coord.x;
        int y = coord.y;
        if ((x < 0 || y < 0) || (x >= Dimensions[0] || y >= Dimensions[1]))
            return OFF_GRID;
        else
            return Grid[x][y].Type;
    }

}

// For every position on the grid, it will have a type and a colour.
class Position {
    // Default values for every new Position object are as follows:
    Type Type = EMPTY;
    boolean IsDestination = false;
    int Colour = Color.TRANSPARENT; // Only should be considered when IsDestination flag is set to true.

    // Used and modified when Type = BOT
    // Time step -> ID of occupying bot.
    HashMap<Integer, String> OccupiedTimeSteps = new HashMap<>();
    String lastOccupiedID;
    int lastOccupiedTimeStep;

    boolean IsPushable = false; // Initially, bot is unpushable. It changes to true when the bot is resting.

    Position() {}
}

// Each points on the grid will only ever have 3 different states:
// either it is empty, off the grid or contains a bot for at least 1 time step.
enum Type {
    EMPTY,      // Used if a position is NEVER ever occupied.
    OFF_GRID,
    BOT         // Used if a position is occupied by a bot at least for 1 time step.
}

