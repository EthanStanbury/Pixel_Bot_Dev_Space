package com.example.mischa.pixelbotui.Swarm;

import java.util.List;

/**
 * Created by Ethan on 25/08/2018.
 */


public class Solution {
    public int Colour;
    public List<Direction> Moves;

    Solution(int colour, List<Direction> moves) {
        Colour = colour;
        Moves = moves;
    }
}
