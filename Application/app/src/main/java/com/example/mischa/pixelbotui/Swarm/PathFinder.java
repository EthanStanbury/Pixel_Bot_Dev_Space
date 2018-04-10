package com.example.mischa.pixelbotui.Swarm;

import android.graphics.Point;

import java.util.ArrayList;
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
    Grid Problem;
    Bot CurrentBot; // Denotes the current bot the A* is solving the path for.
    Point CurrentDest;

    PathFinder(Grid grid) {
        Problem = grid;
    }

    public void solve(Bot bot) {
        CurrentBot = bot;
        CurrentDest = Problem.getDestForBot(CurrentBot);

        // Create the initial node
        Node currentNode = new Node(CurrentBot.Location, "N/A", 0);

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

            for (Object successor_node : Problem.returnPossibleMoves(CurrentBot)) {

                if (false) {

                }
            }
        }
    }

    Node get_lowest_f_node(List<Node> frontier, HashMap<Point, Integer> f_values) {
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
    Integer heuristic(Point pos) {
        return Math.abs(pos.x - CurrentDest.x) + Math.abs(pos.y - CurrentDest.y);
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