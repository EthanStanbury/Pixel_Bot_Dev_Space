package com.example.mischa.pixelbotui.Swarm;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Daniel on 06/04/2018.
 * The design of this A* search algorithm has been influenced by the Wikipedia page:
 * https://en.wikipedia.org/wiki/A*_search_algorithm
 * I've also used the knowledge gained from my COMP3620 Python AI assignment.
 */

// Not completely sure how to implement it yet, but I have a decent idea. I will update this code very soon.
public class PathFinder {
    private Grid Problem;
    private Point CurrentDest;

    PathFinder(Grid grid) {
        Problem = grid;
    }

    public HashMap<String, List<String>> getSolutions(HashMap<Bot, Point> BotDestPairs) {
        // For every bot in the bot -> dest pairs
        HashMap<String, List<String>> allBotsSolutions = new HashMap<>();
        for (HashMap.Entry<Bot, Point> pair : BotDestPairs.entrySet()) {
            Bot bot = pair.getKey();
            CurrentDest = pair.getValue();
            allBotsSolutions.put(bot.BotID, solve(bot));
        }

        return allBotsSolutions;
    }

    private List<String> solve(Bot bot) {
        // Bot CurrentBot = bot; // Denotes the current bot the A* is solving the path for.

        // Create the initial node
        Node currentNode = new Node(bot.Location, "N/A", 0);

        List<Node> frontier = new ArrayList<>();
        frontier.add(currentNode);

        Set<Point> explored = new TreeSet<>();
        explored.add(currentNode.Coord);

        HashMap<Point, BackTrack> back_track = new HashMap<>();

        HashMap<Point, Integer> f_values = new HashMap<>();
        f_values.put(currentNode.Coord, heuristic(currentNode.Coord));

        HashMap<Point, Integer> g_values = new HashMap<>();
        g_values.put(currentNode.Coord, 0);

        while (frontier.size() > 0) {
            currentNode = get_lowest_f_node(frontier, f_values);
            List<Node> successorNodes = Problem.returnPossibleMoves(currentNode.Coord);
            for (int possMoveIndex = 0; possMoveIndex < successorNodes.size(); possMoveIndex++) {
                Node succNode = successorNodes.get(possMoveIndex);
                if (!explored.contains(succNode.Coord) && (!frontier.contains(succNode))) {
                    if (currentNode.Coord == CurrentDest) {
                        back_track.put(succNode.Coord, new BackTrack(currentNode.Coord, currentNode.Action));
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

    private Node get_lowest_f_node(List<Node> frontier, HashMap<Point, Integer> f_values) {
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
    private Integer heuristic(Point pos) {
        return Math.abs(pos.x - CurrentDest.x) + Math.abs(pos.y - CurrentDest.y);
    }

    private List<String> derive_move_seq(Point initial_coord, Point state_coord, HashMap<Point, BackTrack> back_track) {
        List<String> sequence = new ArrayList<>();

        // PLEASE CHECK THAT THIS WORKS
        while (!state_coord.equals(initial_coord)) {
            BackTrack prev = back_track.get(state_coord);
            state_coord = prev.Coord;
            sequence.add(prev.Action);
        }

        // Is copying necessary the sequence list necessary?
        List<String> sequence_reverse = sequence.subList(0, sequence.size());
        Collections.reverse(sequence_reverse);
        return sequence_reverse;
    }

}

class Node {
    Point Coord;
    String Action;
    int Cost;

    Node() {}

    Node(Point coord, String action, int cost) {
        Coord = coord;
        Action = action;
        Cost = cost;
    }
}

class BackTrack {
    Point Coord;
    String Action;

    BackTrack(Point coord, String action) {
        Coord = coord;
        Action = action;
    }
}