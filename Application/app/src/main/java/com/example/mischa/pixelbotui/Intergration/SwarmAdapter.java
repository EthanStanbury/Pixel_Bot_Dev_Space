package com.example.mischa.pixelbotui.Intergration;

import com.example.mischa.pixelbotui.Swarm.Swarm;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ethan on 10/04/2018.
 */

public class SwarmAdapter {

    public static ArrayList<Swarm> WholeSwarm = new ArrayList<>();

    public static void SwarmCreate(HashMap<Integer, Integer> intialSwarm){
        Swarm add = new Swarm(intialSwarm);
        WholeSwarm.add(add);

    }
}
