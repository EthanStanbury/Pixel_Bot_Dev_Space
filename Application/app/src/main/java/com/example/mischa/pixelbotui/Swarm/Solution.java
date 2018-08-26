package com.example.mischa.pixelbotui.Swarm;

import java.util.List;

/**
 * Created by Ethan on 25/08/2018.
 */


public class Solution {
    public int colour;
    public List<Direction> moves;

    Solution(int colour, List<Direction> moves) {
        this.colour = colour;
        this.moves = moves;
    }
}
