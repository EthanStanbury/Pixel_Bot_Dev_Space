package com.example.mischa.pixelbotui.Swarm;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.example.mischa.pixelbotui.Swarm.Direction.*;

/**
 * Created by Daniel on 06/04/2018.
 * The design of this A* search algorithm has been influenced by the Wikipedia page:
 * https://en.wikipedia.org/wiki/A*_search_algorithm
 * I've also used the knowledge gained from my COMP3620 Python AI assignment.
 */

public class PathFinder {
    private static Grid Problem;
    // private static Point CurrentDest;

    public static HashMap<String, Solution> getSolutions(Grid problem) {
        // For every bot in the bot -> dest pairs, solve for a solution with the A* algorithm.
        Problem = problem;      // Grid with all the bots and destinations are defined as the 'Problem' that needs solving.
        Problem.mapBotToDest(); // When getSolutions() method was called, it is assumed that all bots and destinations were added.
                                // Hence, it is now acceptable to link all destinations to a bot.

        // Save the bot-dest pair hashmap to a local variable.
        HashMap<Bot, Point> BotDestPairs = Problem.BotDestPairs;

        // Saves a list of all bots that have been paired to a destination.
        List<Bot> pairedBotList = new ArrayList<>();
        // Saves a virtual copy of the bots, so that we can perform push functionality
        List<Bot> botDuplicates = new ArrayList<>();

        // Stores the solution (which contains colour and sequence of moves).
        HashMap<String, Solution> finalSolution = new HashMap<>();
        // Stores the moves history of each bots (that are moving).
        HashMap<String, List<Direction>> allBotsSolutions = new HashMap<>();
        // Stores the position history of each bots (that are moving).
        HashMap<String, List<Point>> allBotsPositions = new HashMap<>();
        // Stores the colour of each bot.
        HashMap<String, Integer> allBotsColour = new HashMap<>();

        for (HashMap.Entry<Bot, Point> pair : BotDestPairs.entrySet()) {

            Bot bot = pair.getKey();
            Bot botCopy = new Bot(bot.BotID, bot.Location);

            pairedBotList.add(bot);
            botDuplicates.add(botCopy);

            CoordActionOutput aStarOutput = solve(bot, pair.getValue());

            allBotsSolutions.put(bot.BotID, aStarOutput.Actions);
            allBotsPositions.put(bot.BotID, aStarOutput.Coordinates);
            allBotsColour.put(bot.BotID, 0); // Set all bots' colour to 0 for now. This will be changed in the step-by-step analysis.
            // May not even be necessary.
        }

        // This is a step by step checker: it checks the bots' positions for any collisions and resolves them.
        boolean anyStepsLeft = true;
        int timeStep = 1; // Ignore saving the initial step, as it is already done (during bot initialisation)
        while (anyStepsLeft) {
            anyStepsLeft = false;   // Assume that there are no actions left, unless found.
           // System.out.println("TIME STEP: " + timeStep);

            List<String> pushingBotsID = new ArrayList<>();
            List<Point> pushingBots = new ArrayList<>();
            List<String> pushedBotsID = new ArrayList<>();
            List<Point> pushedBotsPos = new ArrayList<>();

            for (int i = 0; i < pairedBotList.size(); i++) {
                String currentBotID = pairedBotList.get(i).BotID;
                // Don't simply copy the contents of the lists, but instead enable direct editing to the list that is about to be sent.
                List<Point> currentBotPositions = allBotsPositions.get(currentBotID);
                List<Direction> currentBotPath = allBotsSolutions.get(currentBotID);
                /*
                System.out.println(currentBotPositions.size() + " " + currentBotPath.size());
                for (int x = 0; x < currentBotPositions.size(); x++) {
                    System.out.println(currentBotPositions.get(x) + " " + currentBotPath.get(x));
                }
                */
                if (timeStep < currentBotPositions.size()) {
                    anyStepsLeft = true;

                    Point currentPos = currentBotPositions.get(timeStep);
                    // Check if position at time step x is free:
                    boolean posAvailable = Problem.checkAvailability(currentPos, timeStep);
                    // System.out.println(currentBotID + " : " + currentBotPositions.get(timeStep));
                    boolean reachedDestination = timeStep == currentBotPositions.size() - 1;
                    //System.out.println(reachedDestination);

                    if (posAvailable) {
                        Problem.updateBoard(currentPos, timeStep, currentBotID, reachedDestination);
                        botDuplicates.get(i).Location = currentPos;
//                        pairedBotList.get(i).Colour = Problem.getColourFromPos(currentPos);
                        allBotsColour.put(currentBotID, Problem.getColourFromPos(currentPos));
                    } else {
                       // System.out.println("COLLISION DETECTED FOR BOT: " + currentBotID + " " + currentPos + " AT TIME STEP " + timeStep);

                        // If the bot in the way is not 'resting', then simply add an extra stop in the path sequence.
                        if (!Problem.getPushableStatus(currentPos)) {
                            // Add an additional step to the bot's path sequence (stop for 1 time step)
                            //System.out.println("Adding a 'stop' command in the sequence at this timestep(" + timeStep + ")...");
                            currentBotPositions.add(timeStep, currentBotPositions.get(timeStep - 1));
                            currentBotPath.add(timeStep, S);
                        } else { // A temporary solution - until the 'push' method is implemented, the bot should just walk over other bots that have stopped to prevent the program from 'hanging'.
                            // Problem.updateBoard(currentPos, timeStep, currentBotID, reachedDestination);

                            // Match the sequence length of the pushing and pushed bot so they are in sync (in the right time step).
                            String pushedBotID = Problem.returnPushableBotID(currentPos);
                            int indexOfLastEvent = allBotsPositions.get(pushedBotID).size() - 1;
                            int catchupsCounter = (timeStep - 1) - indexOfLastEvent;
                            List<Point> pushBotPosList = allBotsPositions.get(pushedBotID);
                            List<Direction> pushBotPathList = allBotsSolutions.get(pushedBotID);

                            while (catchupsCounter > 0) {
                                pushBotPosList.add(pushBotPosList.get(indexOfLastEvent));
                                pushBotPathList.add(S);
                                Problem.updateBoard(pushBotPosList.get(indexOfLastEvent), (timeStep - catchupsCounter), pushedBotID, true);
                                catchupsCounter -= 1;
                            }

                            pushingBotsID.add(currentBotID);
                            pushingBots.add(currentBotPositions.get(timeStep - 1));
                            pushedBotsID.add(pushedBotID);
                            pushedBotsPos.add(new Point(pushBotPosList.get(timeStep - 1)));

                           // System.out.println("PUSHED BOT " + pushedBotID + " AT POS: " + pushBotPosList.get(indexOfLastEvent));

                            Problem.removeOccupation(pushBotPosList.get(indexOfLastEvent), timeStep);

                            // Initially set it false, so that the new destination pairing doesn't try to pair bots to one of the other pushing bots' destinations.
                            // This will be set to true once all pairing has completed.
                            Problem.updateBoard(currentPos, timeStep, currentBotID, true);
                        }
                    }
                }
            }

            for (int j = 0; j < pushingBots.size(); j++) {
                String pushedID = pushedBotsID.get(j);
                Point newTarget = Problem.determineNewDestination(pushingBots.get(j), pushedBotsPos.get(j), pushingBotsID.get(j), pushedBotsID.get(j), timeStep);
                Bot pushedBot = getBotObject(pushedID, botDuplicates);

                CoordActionOutput aStarOutput = solve(pushedBot, newTarget);
                pushedBot.Location = pushedBotsPos.get(j);
             //   System.out.println(pushedBot.Location);
              //  System.out.println(newTarget);

                allBotsPositions.get(pushedID).addAll(aStarOutput.Coordinates);
                allBotsSolutions.get(pushedID).addAll(aStarOutput.Actions);
              //  System.out.println(allBotsPositions.get(pushedID).get(allBotsPositions.get(pushedID).size() - 1));
              //  System.out.println(aStarOutput.Coordinates.size());
            }

            for (int j = 0; j < pushingBots.size(); j++) {
                //Problem.updateIsPushableFlag(pushingBots.get(j), true);
            }

            if (pushingBots.size() == 0) {
                /*
                for (int i = 0; i < pairedBotList.size(); i++) {
                    String currentBotID = pairedBotList.get(i).BotID;
                    List<Point> currentBotPositions = allBotsPositions.get(currentBotID);
                    Point currentPos = currentBotPositions.get(currentBotPositions.size() - 1);
                    botDuplicates.get(i).Location = currentPos;
                    pairedBotList.get(i).Colour = Problem.getColourFromPos(currentPos);
                }
                */
                timeStep += 1;
            }


            if (timeStep > 50) // Artificial cap for step-by-step so that app doesn't hang if checks get stuck.
                break;
        }

        for (HashMap.Entry<String, List<Direction>> pair : allBotsSolutions.entrySet()) {
            String currentBotID = pair.getKey();
            List<Direction> currentBotPathSequence = pair.getValue();
            int currentBotColour = allBotsColour.get(currentBotID);

            Solution colourMovePair = new Solution(currentBotColour, currentBotPathSequence);

            finalSolution.put(currentBotID, colourMovePair);
        }

        return finalSolution;
    }

    private static Bot getBotObject(String botID, List<Bot> botList) {
        for (int i = 0; i < botList.size(); i++) {
            if (botList.get(i).BotID.equals(botID)) {
                return botList.get(i);
            }
        }
        return null; // If no match is found
    }

    private static CoordActionOutput solve(Bot bot, Point dest) {
        // Create the initial node
        Node currentNode = new Node(bot.Location, NA, 0);

        // Store the nodes in the frontier. The function 'get_lowest_f_node' will select the next node.
        List<Node> frontier = new ArrayList<>();
        frontier.add(currentNode); // Frontier starts off with the starting coordinates.

        // Keep track of states that has been visited.
        // Adds the first coordinate to the list as it is already 'visited'
        ArrayList<Point> explored = new ArrayList<>();
        explored.add(currentNode.Coord);

        // Keeps track of all states and the last movement.
        // It is used in 'derive_move_seq' to generate the final sequence of moves.
        HashMap<Point, BackTrack> back_track = new HashMap<>();

        // Store estimated costs to the goal (f = g + h).
        // For the first node, the f value is entirely heuristic.
        HashMap<Point, Integer> f_values = new HashMap<>();
        f_values.put(currentNode.Coord, heuristic(currentNode.Coord, dest));
        // Store the current cost so far to get to the current location
        HashMap<Point, Integer> g_values = new HashMap<>();
        g_values.put(currentNode.Coord, 0);

        // Until no more nodes are left to search, keep searching
        while (frontier.size() > 0) {
            // Get the node with the lowest f value.
            currentNode = get_lowest_f_node(frontier, f_values);

            // Get all the 'successor nodes', which basically are all possible moves from current location.
            List<Node> successorNodes = Problem.getSuccessorNodes(currentNode);
            for (int possMoveIndex = 0; possMoveIndex < successorNodes.size(); possMoveIndex++) {
                Node succNode = successorNodes.get(possMoveIndex);

                // If successor node is not explored yet AND is not already in the frontier,
                if (!explored.contains(succNode.Coord) && (!frontier.contains(succNode))) {
                    // If goal is found (landed on destination),
                    if (succNode.Coord.equals(dest)) {
                        // Add this final goal state for backtracking
                        back_track.put(succNode.Coord, new BackTrack(currentNode.Coord, succNode.Action));
                        return derive_move_seq(bot.Location, succNode.Coord, back_track);
                    }

                    // # Add node to frontier and explored if goal is not found.
                    frontier.add(succNode);
                    explored.add(succNode.Coord);

                    // Provision a new g value.
                    Integer temp_g_value = g_values.get(currentNode.Coord) + succNode.Cost;
                    // If the successor state does not have a value,
                    // set it to a high number so that it passes the next test.
                    if (!g_values.containsKey(succNode.Coord))
                        g_values.put(succNode.Coord, 999999);
                    // If the provisioned g value is less than the previously known lowest g value,
                    // replace it and use the new path to get to this state instead.
                    if (temp_g_value < g_values.get(succNode.Coord)) {
                        // Add state to back_track for later.
                        back_track.put(succNode.Coord, new BackTrack(currentNode.Coord, succNode.Action));

                        g_values.put(succNode.Coord, temp_g_value);
                        f_values.put(succNode.Coord, (g_values.get(succNode.Coord) + heuristic(succNode.Coord, dest)));
                    }
                }
            }
        }
        return new CoordActionOutput(new ArrayList<Point>(), new ArrayList<Direction>());
        //return null;
    }

    // Returns the node with the lowest known f_value.
    // Always tries the node with lowest cost estimate first.
    private static Node get_lowest_f_node(List<Node> frontier, HashMap<Point, Integer> f_values) {
        int current_f_value = 999999;
        int index_to_remove = 0;
        Node node_to_return = new Node();
        for (int i = 0; i < frontier.size(); i ++) {
            Node checkNode = frontier.get(i);
            if (f_values.get(checkNode.Coord) < current_f_value) {
                current_f_value = f_values.get(checkNode.Coord);
                node_to_return = checkNode;
                index_to_remove = i;
            }
        }

        // Nodes that are picked are removed from the frontier, so they don't get selected again.
        frontier.remove(index_to_remove);
        return node_to_return;
    }

    // Heuristic is an estimate of the most optimistic (lowest) cost that the solver should expect.
    // This way, the solver doesn't try moves that veer away from the goal.
    // This calculates the Manhattan distance from given position to the target destination.
    private static Integer heuristic(Point pos, Point dest) {
        return Math.abs(pos.x - dest.x) + Math.abs(pos.y - dest.y);
    }

    // The idea to to reverse engineer the sequence of moves to get to the destination came from:
    // https://en.wikipedia.org/wiki/A*_search_algorithm (function called 'reconstruct_path')
    private static CoordActionOutput derive_move_seq(Point initial_coord, Point state_coord, HashMap<Point, BackTrack> back_track) {
        List<Point> pos = new ArrayList<>();
        List<Direction> sequence = new ArrayList<>();

        // Keep searching until we have determined the link from the finish to the start and its sequence of moves.
        // Saved in reverse order though, since we are searching from finish to start.
        while (!state_coord.equals(initial_coord)) {
            BackTrack prev = back_track.get(state_coord);
            pos.add(state_coord);
            state_coord = prev.Coord;
            sequence.add(prev.Action);
        }

        // Reverse the reversed sequence
        Collections.reverse(pos);
        Collections.reverse(sequence);
        return new CoordActionOutput(pos, sequence);
    }

}

// Each node consists of a coordinate, an action and the cost to move
class Node {
    Point Coord;
    Direction Action;
    int Cost;

    Node() {}

    Node(Point coord, Direction action, int cost) {
        Coord = coord;
        Action = action;
        Cost = cost;
    }
}

// In order for back tracking to work, only the coordinate and action is required.
class BackTrack {
    Point Coord;
    Direction Action;

    BackTrack(Point coord, Direction action) {
        Coord = coord;
        Action = action;
    }
}

// Saves both the positions and actions the bot will take.
class CoordActionOutput {
    List<Point> Coordinates;
    List<Direction> Actions;

    CoordActionOutput(List<Point> coords, List<Direction> actions) {
        Coordinates = new ArrayList<>(coords);
        Actions = new ArrayList<>(actions);
    }
}
