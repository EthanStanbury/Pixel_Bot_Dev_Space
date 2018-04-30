package com.example.mischa.pixelbotui.Intergration;

import android.graphics.Color;

import com.example.mischa.pixelbotui.Swarm.Swarm;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ethan on 10/04/2018.
 */

public class SwarmAdapter {



    public static HashMap<Integer, Swarm> WholeSwarm = new HashMap<>();

    public static void SwarmCreate(HashMap<Integer, Integer> initialSwarm){

        for (int key : initialSwarm.keySet()) {
            Swarm add = new Swarm(key, initialSwarm.get(key));
            WholeSwarm.put(key, add);
        }

    }
}
