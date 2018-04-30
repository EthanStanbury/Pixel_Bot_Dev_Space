package com.example.mischa.pixelbotui.Swarm;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static com.example.mischa.pixelbotui.Swarm.Direction.*;

/**
 * Created by Daniel on 06/04/2018.
 * The design of this A* search algorithm has been influenced by the Wikipedia page:
 * https://en.wikipedia.org/wiki/A*_search_algorithm
 * I've also used the knowledge gained from my COMP3620 Python AI assignment.
 */

// Not completely sure how to implement it yet, but I have a decent idea. I will update this code very soon.
public class PathFinder {
    private static Grid Problem;
    private static Point CurrentDest;

    public static HashMap<String, List<Direction>> getSolutions(Grid problem) {
        // For every bot in the bot -> dest pairs
        Problem = problem;
        Problem.mapBotToDest();
        HashMap<Bot, Point> BotDestPairs = Problem.BotDestPairs;

        HashMap<String, List<Direction>> allBotsSolutions = new HashMap<>();
        for (HashMap.Entry<Bot, Point> pair : BotDestPairs.entrySet()) {

            Bot bot = pair.getKey();
            CurrentDest = pair.getValue();
            allBotsSolutions.put(bot.BotID, solve(bot));
        }

        return allBotsSolutions;
    }

    private static List<Direction> solve(Bot bot) {
        // Bot CurrentBot = bot; // Denotes the current bot the A* is solving the path for.

        // Create the initial node
        Node currentNode = new Node(bot.Location, NA, 0);

        List<Node> frontier = new ArrayList<>();
        frontier.add(currentNode);
        //Trying to make a new Array list that has unique elements
        ArrayList<Point> explored = new ArrayList<>();
        //for (int i = 0; i< explored.size() && (!explored.get(i).equals(currentNode.Coord)); i++){
        //    if (i == explored.size()-1){
        explored.add(currentNode.Coord);
        //    }
        //}


        HashMap<Point, BackTrack> back_track = new HashMap<>();

        HashMap<Point, Integer> f_values = new HashMap<>();
        f_values.put(currentNode.Coord, heuristic(currentNode.Coord));

        HashMap<Point, Integer> g_values = new HashMap<>();
        g_values.put(currentNode.Coord, 0);
        while (frontier.size() > 0) {
            currentNode = get_lowest_f_node(frontier, f_values);
            List<Node> successorNodes = Problem.getSuccessorNodes(currentNode);
            // System.out.println("successor node count: " + successorNodes.size());
            // System.out.println("current coord: " + currentNode.Coord);
            for (int possMoveIndex = 0; possMoveIndex < successorNodes.size(); possMoveIndex++) {
                // System.out.println(successorNodes.get(possMoveIndex).Action);
                Node succNode = successorNodes.get(possMoveIndex);
                System.out.println(!frontier.contains(succNode));
                if (!explored.contains(succNode.Coord) && (!frontier.contains(succNode))) {
                    if (succNode.Coord.equals(CurrentDest)) {
                        System.out.println("DO I GET ANY SOLUTION???");
                        back_track.put(succNode.Coord, new BackTrack(currentNode.Coord, succNode.Action));
                        return derive_move_seq(bot.Location, succNode.Coord, back_track);
                    }

                    frontier.add(succNode);
                    explored.add(succNode.Coord);

                    Integer temp_g_value = g_values.get(currentNode.Coord) + succNode.Cost;

                    if (!g_values.containsKey(succNode.Coord))
                        g_values.put(succNode.Coord, 999999);

                    if (temp_g_value < g_values.get(succNode.Coord)) {
                        back_track.put(succNode.Coord, new BackTrack(currentNode.Coord, succNode.Action));

                        g_values.put(succNode.Coord, temp_g_value);
                        f_values.put(succNode.Coord, (g_values.get(succNode.Coord) + heuristic(succNode.Coord)));
                    }
                }
            }
        }
        return null;
    }

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
        frontier.remove(index_to_remove);
        return node_to_return;
    }

    // This calculates the Manhattan distance from given position to the target destination.
    private static Integer heuristic(Point pos) {
        return Math.abs(pos.x - CurrentDest.x) + Math.abs(pos.y - CurrentDest.y);
    }

    private static List<Direction> derive_move_seq(Point initial_coord, Point state_coord, HashMap<Point, BackTrack> back_track) {
        List<Direction> sequence = new ArrayList<>();

        // PLEASE CHECK THAT THIS WORKS
        while (!state_coord.equals(initial_coord)) {
            BackTrack prev = back_track.get(state_coord);
            state_coord = prev.Coord;
            sequence.add(prev.Action);
        }

        Collections.reverse(sequence);
        return sequence;
    }

}

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

class BackTrack {
    Point Coord;
    Direction Action;

    BackTrack(Point coord, Direction action) {
        Coord = coord;
        Action = action;
    }
}