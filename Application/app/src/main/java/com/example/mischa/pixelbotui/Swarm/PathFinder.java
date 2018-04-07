package com.example.mischa.pixelbotui.Swarm;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Daniel on 06/04/2018.
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